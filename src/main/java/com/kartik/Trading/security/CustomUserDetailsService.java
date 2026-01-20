package com.kartik.Trading.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kartik.Trading.model.Role;
import com.kartik.Trading.model.User;
import com.kartik.Trading.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		Role role = user.getRole();
		if (role == null) {
		    throw new IllegalStateException("User role is null for " + user.getEmail());
		}
		
		String roleWithoutPrefix = user.getRole().name().replace("ROLE_", "");
		
		return org.springframework.security.core.userdetails.User
				.withUsername(user.getEmail())
				.password(user.getPassword())
				.roles(roleWithoutPrefix)
				.build();
	}
	
}
