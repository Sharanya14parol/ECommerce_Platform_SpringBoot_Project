package com.ecommerce.platform;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf ->csrf.disable())
			.authorizeHttpRequests(auth ->auth
					.requestMatchers(HttpMethod.POST, "/register").permitAll()
					.requestMatchers(HttpMethod.POST, "/reviews/addReview").permitAll()
					.requestMatchers(HttpMethod.POST, "/payment/**").permitAll()
					.requestMatchers("/login","/public/products", "/perform_login", "/loginFailed").permitAll()
					.requestMatchers("/admin/products").hasRole("ADMIN")
					.anyRequest().authenticated()
					)
			
		.formLogin(form ->form
				.loginPage("/login")
				.loginProcessingUrl("/perform_login")
				.defaultSuccessUrl("/home",true)
				.failureUrl("/loginFailed")
				.permitAll()
		);
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
