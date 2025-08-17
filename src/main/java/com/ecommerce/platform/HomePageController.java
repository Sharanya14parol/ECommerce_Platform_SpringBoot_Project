package com.ecommerce.platform;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {
	
	@GetMapping("/home")
	public String homePage() {
		return "Login Successful, Welcome !!!";
	}

}
