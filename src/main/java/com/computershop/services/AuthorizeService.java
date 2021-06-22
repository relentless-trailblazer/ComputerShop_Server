package com.computershop.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.computershop.exceptions.ForbiddenException;
import com.computershop.repositories.UserRepository;

@Service("authorizeService")
public class AuthorizeService {

	@Autowired
	private UserRepository userRepository;
	
	public Boolean authorizeAdmin(Authentication authentication, String role) {
		Object[] objectAuthentication = authentication.getAuthorities().toArray();
		if (objectAuthentication[0].toString().compareTo(role) == 0) {
			return true; 
		}
		throw new ForbiddenException("Access denied");
	}
	
	public boolean authorizeGetUserById(Authentication authentication, String role, Long userId) {
		Object[] objectAuthentication = authentication.getAuthorities().toArray();
		if (objectAuthentication[0].toString().compareTo(role) == 0) {
			return true;
		}

		User userAuth = (User) authentication.getPrincipal(); 
		com.computershop.dao.User user = userRepository.findByUsername(userAuth.getUsername());
	
		if(user.getId()!=userId)
			throw new ForbiddenException("Access denied");
		return true;
	}
	
	public Boolean authorizeUser(Authentication authentication, Long userId) {
		User userAuth = (User) authentication.getPrincipal(); 
		com.computershop.dao.User user = userRepository.findByUsername(userAuth.getUsername());
	
		if(user.getId()!=userId)
				throw new ForbiddenException("Access denied!");
		return true;
	}
	
	
}
