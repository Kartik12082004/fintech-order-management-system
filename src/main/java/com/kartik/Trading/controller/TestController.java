package com.kartik.Trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
	
	@GetMapping("/secure")
	public String securedEndpoint() {
		return "JWT is working. You are authenticated.";
	}
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/auth/debug/encode")
	public String encode(@RequestBody String raw) {
	    return passwordEncoder.encode(raw);
	}

}
