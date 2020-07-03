package jp.co.sample.emp_management.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/login").usernameParameter("mailAddress").passwordParameter("password")
				.defaultSuccessUrl("/employee/showList").failureUrl("/login?error=true").permitAll();
//		http
//				// AUTHORIZE
//				.authorizeRequests()/* */.mvcMatchers("/").permitAll()/* */.anyRequest()/*    */.authenticated().and()
//				// LOGIN
//				.formLogin()/* */.defaultSuccessUrl("/success")
//		// end
//		;
//		http.authorizeRequests().anyRequest().permitAll();
		http.authorizeRequests().antMatchers("/", "/toInsert", "/insert").permitAll()
				.antMatchers("/img/*", "/css/*", "/js/*", "/static/js/*").permitAll().anyRequest().authenticated();

		http.logout().logoutSuccessUrl("/login").permitAll();
	}

	@Autowired
	void configureAuthenticationManager(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());

	}

	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
