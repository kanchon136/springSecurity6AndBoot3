package springsecurity.com.repositoryImpl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import springsecurity.com.entity.User;
import springsecurity.com.repository.UserRepository;
import springsecurity.com.service.CustomUserDetailsService;

@Service
public class CustomUserDetailServiceImpl implements CustomUserDetailsService {
	@Autowired
	private UserRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    User user = repo.findUserByUserName(username).orElseThrow(()-> new UsernameNotFoundException("User not found"+username));
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), getAuthorities(user));
	}
	
	private static Collection<? extends GrantedAuthority> getAuthorities(User user){
		return user.getRoles().stream().map(role-> new SimpleGrantedAuthority(role.getRoleName())).toList();	
	}
	
	
}