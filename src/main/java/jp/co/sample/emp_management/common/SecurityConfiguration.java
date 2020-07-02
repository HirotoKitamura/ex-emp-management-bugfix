package jp.co.sample.emp_management.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/").loginProcessingUrl("/authenticate").usernameParameter("mailAddress")
				.passwordParameter("password").defaultSuccessUrl("/employee/showList").permitAll();
//		http
//				// AUTHORIZE
//				.authorizeRequests()/* */.mvcMatchers("/").permitAll()/* */.anyRequest()/*    */.authenticated().and()
//				// LOGIN
//				.formLogin()/* */.defaultSuccessUrl("/success")
//		// end
//		;
		http.authorizeRequests().anyRequest().permitAll();
//		http.authorizeRequests().antMatchers("/toInsert").permitAll().antMatchers("/insert").permitAll()
//				.antMatchers("/img/header_logo.png").permitAll().antMatchers("/css/bootstrap.css").permitAll()
//				.antMatchers("/css/style.css").permitAll().anyRequest().authenticated();
	}

	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
