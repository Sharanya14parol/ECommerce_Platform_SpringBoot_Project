package com.ecommerce.platform.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder encoder;
	
	 private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	public void registerUser(User user) {
		if(userRepository.findByUsername(user.getUsername())!=null) {
			logger.warn("User transaction failed: Registration failed - Username already exists");
			throw new RuntimeException("User already exists");
		}
		user.setRole("user");
		user.setPassword(encoder.encode(user.getPassword()));
		logger.info("User transaction: Successfully registered user");
		userRepository.save(user);
	}
	
}
