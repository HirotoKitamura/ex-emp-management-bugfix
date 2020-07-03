package jp.co.sample.emp_management.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthAdmin implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Administrator admin;
	private Collection<GrantedAuthority> authorities;

	public AuthAdmin() {
	}

	public AuthAdmin(Administrator admin, Collection<GrantedAuthority> authorities) {
		super();
		this.admin = admin;
		this.authorities = authorities;
	};

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return admin.getPassword();
	}

	@Override
	public String getUsername() {
		return admin.getMailAddress();
	}

	public String getName() {
		return admin.getName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
