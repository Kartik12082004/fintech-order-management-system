package com.kartik.Trading.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kartik.Trading.dto.request.AdminFundWalletRequest;
import com.kartik.Trading.dto.request.CreateAssetRequest;
import com.kartik.Trading.model.User;
import com.kartik.Trading.repository.UserRepository;
import com.kartik.Trading.service.AssetService;
import com.kartik.Trading.service.WalletService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
	private final UserRepository userRepository;
	private final WalletService walletService;
	private final AssetService assetService;
	
	public AdminController(UserRepository userRepository,
						   WalletService walletService,
						   AssetService assetService) {
		this.userRepository = userRepository;
		this.walletService = walletService;
		this.assetService = assetService;
	}
	
	@PostMapping("/fund-wallet")
	public ResponseEntity<?> fundWallet(@Valid @RequestBody AdminFundWalletRequest request){
		
		User user = userRepository.findByEmail(request.getUserEmail())
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		
		walletService.systemCredit(user, request.getAmount());
		
		return ResponseEntity.ok(
				Map.of(
						"message", "Wallet funded successfully",
						"user", user.getEmail(),
						"amount", request.getAmount()
				)
		);
		
	}
	
	@PostMapping("/asset")
	public ResponseEntity<?> createAsset(@Valid @RequestBody CreateAssetRequest request) {
	    
		assetService.createAsset(
	            request.getSymbol(),
	            request.getPrice()
	    );

	    return ResponseEntity.ok(Map.of(
	            "message", "Asset created successfully",
	            "symbol", request.getSymbol(),
	            "price", request.getPrice()
	    ));
	}

	
}
