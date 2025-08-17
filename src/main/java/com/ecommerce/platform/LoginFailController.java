package com.ecommerce.platform;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginFailController {

	@GetMapping("/loginFailed")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String loginFailed() {
		return "Login Failed, please check your username and password "
				+ "Register if you are an new user and then login";
	}
}
