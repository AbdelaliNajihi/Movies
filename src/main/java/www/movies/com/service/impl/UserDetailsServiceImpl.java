package www.movies.com.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import www.movies.com.dao.IUserAppRepository;
import www.movies.com.model.UserApp;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final IUserAppRepository userAppRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserApp userApp = userAppRepository.findByUsername(username);
		if (userApp == null) {
			throw new UsernameNotFoundException(username);
		}
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		userApp.getRoles().forEach(r -> grantedAuthorities.add(new SimpleGrantedAuthority(r.getRoleName())));
		return new User(userApp.getUsername(), userApp.getPassword(), grantedAuthorities);
	}

}
