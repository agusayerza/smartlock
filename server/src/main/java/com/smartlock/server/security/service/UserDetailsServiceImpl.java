package com.smartlock.server.security.service;


import com.smartlock.server.user.persistence.model.User;
import com.smartlock.server.user.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	@Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		final User user = userRepository.findFirstByEmailAndActiveTrue(email);
		if(user == null){
			throw new UsernameNotFoundException("User Not Found with email : " + email);
		}

		return UserPrinciple.build(user);
	}


}