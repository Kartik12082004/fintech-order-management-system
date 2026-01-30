package com.kartik.Trading.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kartik.Trading.dto.request.LoginRequest;
import com.kartik.Trading.dto.request.RegisterRequest;
import com.kartik.Trading.dto.response.AuthResponse;
import com.kartik.Trading.exception.ConflictException;
import com.kartik.Trading.model.Role;
import com.kartik.Trading.model.User;
import com.kartik.Trading.model.Wallet;
import com.kartik.Trading.repository.UserRepository;
import com.kartik.Trading.repository.WalletRepository;
import com.kartik.Trading.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User registration and login APIs")
public class AuthController {
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
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
	
	@Operation(
			summary = "Register a new user",
			description = "Creates a new user account and initializes wallet with zero balance"
	)
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
		
		log.info("Registration attempt for email={}", request.getEmail());
		
		if (userRepository.existsByEmail(request.getEmail())) {
		    log.warn("Registration failed — email already exists={}", request.getEmail());
		    throw new ConflictException("Email already registered");
		}

		
		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.ROLE_USER);
		userRepository.save(user);
		
		Wallet wallet = new Wallet(user);
		walletRepository.save(wallet);
		
		log.info("User registered successfully email={}", request.getEmail());
		
		return ResponseEntity.ok("User registered successfully");
	}
	
	@Operation(
		    summary = "Login user",
		    description = "Authenticates user and returns JWT token"
	)
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
	    
		log.info("Login attempt for email={}", request.getEmail());
		
		Authentication authentication = authenticationManager.authenticate(
		        new UsernamePasswordAuthenticationToken(
		            request.getEmail(),
		            request.getPassword()
		        )
		    );

		    String token = jwtUtil.generateToken(authentication.getName());
		    
		    log.info("Login successful for email={}", request.getEmail());
		    
		    return ResponseEntity.ok(new AuthResponse(token));
		    
	}
	
}
