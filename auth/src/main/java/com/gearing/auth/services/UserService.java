package com.gearing.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gearing.auth.models.User;
import com.gearing.auth.repositories.RoleRepository;
import com.gearing.auth.repositories.UserRepository;

@Service
public class UserService {
	// No I'm not making a constructor if I don't have to, are you high?
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private BCryptPasswordEncoder bCryptPassEncoder;

	public void saveWithUserRole(User user) {
		user.setPassword(bCryptPassEncoder.encode(user.getPassword()));
		user.setRoles(roleRepo.findByName("ROLE_USER"));
		
		userRepo.save(user);
	}
	
	public void saveUserWithAdminRole(User user) {
		user.setPassword(bCryptPassEncoder.encode(user.getPassword()));
		user.setRoles(roleRepo.findByName("ROLE_ADMIN"));
		
		userRepo.save(user);
	}
	
	public User findByUsername(String username) {
		return userRepo.findByUsername(username);
	}
}
