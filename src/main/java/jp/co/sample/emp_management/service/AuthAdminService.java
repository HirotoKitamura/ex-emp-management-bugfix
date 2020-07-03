package jp.co.sample.emp_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.domain.AuthAdmin;
import jp.co.sample.emp_management.repository.AdministratorRepository;

@Service
public class AuthAdminService implements UserDetailsService {
	@Autowired
	private AdministratorRepository repository;
//	@Autowired
//	private BCryptPasswordEncoder encoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username == null || "".equals(username)) {
			throw new UsernameNotFoundException("メールアドレスまたはパスワードが不正です。");
		}
		Administrator ad = repository.findByMailAddress(username);
		if (ad == null) {
			throw new UsernameNotFoundException("メールアドレスまたはパスワードが不正です。");
		}
		AuthAdmin admin = new AuthAdmin(ad, AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
		return admin;
	}

}
