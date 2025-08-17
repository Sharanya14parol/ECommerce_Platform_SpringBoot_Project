package com.ecommerce.platform.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class UserController {
	
	@Autowired
    private UserService userService;

	 private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	 
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUser(@RequestBody User user) {
    	userService.registerUser(user);
    	return "User registered successfully";
    }
    
    @PostMapping("/login")
    public String loginUser() {
    	logger.info("API Call: GET /login - Displaying login page information.");
    	return "This is the login page which contanis login form";
    
    }
}