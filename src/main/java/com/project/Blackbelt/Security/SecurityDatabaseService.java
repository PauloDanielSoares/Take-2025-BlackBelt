package com.project.Blackbelt.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.Blackbelt.Model.Users;
import com.project.Blackbelt.Repository.UserRepository;

@Service
public class SecurityDatabaseService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			throw new UsernameNotFoundException("Usuário não foi encontrado");
		}
		System.out.println("Usuário encontrado: " + userEntity.getUsername());
		return userEntity;
	}
	

}
