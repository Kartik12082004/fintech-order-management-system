package com.kartik.Trading.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kartik.Trading.model.User;
import com.kartik.Trading.model.Wallet;
import com.kartik.Trading.repository.UserRepository;
import com.kartik.Trading.repository.WalletRepository;
import com.kartik.Trading.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final UserRepository userRepository;
	private final WalletRepository walletRepository;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	
	public AuthController(UserRepository userRepository,
            WalletRepository walletRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
		
		this.userRepository = userRepository;
		this.walletRepository = walletRepository;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		
	}
	
	@PostMapping("/register")
	public String register(@RequestBody Map<String, String> req) {
		User user = new User();
		user.setEmail(req.get("email"));
		user.setPassword(passwordEncoder.encode(req.get("password")));
		userRepository.save(user);
		
		Wallet wallet = new Wallet(user);
		wallet.credit(BigDecimal.valueOf(10000));
		walletRepository.save(wallet);
		
		return "User Registered";
	}
	
	@PostMapping("/login")
	public String login(@RequestBody Map<String, String> req) {
	    System.out.println("Login attempt for: " + req.get("email"));

	    Authentication auth = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(
	            req.get("email"), req.get("password"))
	    );

	    return jwtUtil.generateToken(req.get("email"));
	}
	
}
