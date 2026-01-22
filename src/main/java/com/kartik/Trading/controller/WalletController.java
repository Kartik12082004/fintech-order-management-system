package com.kartik.Trading.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kartik.Trading.dto.response.WalletResponse;
import com.kartik.Trading.model.User;
import com.kartik.Trading.model.Wallet;
import com.kartik.Trading.repository.UserRepository;
import com.kartik.Trading.service.WalletService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/wallet")
@Tag(name = "Wallet", description = "Wallet bslsnce APIs")
public class WalletController {

	private final WalletService walletService;
	private final UserRepository userRepository;
	
	public WalletController(WalletService walletService,
							UserRepository userRepository) {
		this.walletService = walletService;
		this.userRepository = userRepository;
	}
	
	@Operation(summary = "Get wallet balance")
	@GetMapping
	public ResponseEntity<WalletResponse> getWallet(@AuthenticationPrincipal UserDetails userDetails) {
	    
		User user = userRepository.findByEmail(userDetails.getUsername())
	            .orElseThrow(() -> new IllegalArgumentException("User not found"));

	    Wallet wallet = walletService.getWalletByUser(user);

	    return ResponseEntity.ok(new WalletResponse(wallet.getBalance()));
	}

}
