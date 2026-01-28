package com.kartik.Trading.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Operations", description = "Admin-only APIs for managing users, wallets and assets")
public class AdminController {
	
	private static final Logger log = LoggerFactory.getLogger(AdminController.class);
	
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
	
	@Operation(
	    summary = "Fund a user's wallet",
	    description = """
	        Allows an admin to credit a user's wallet.
	        This is a SYSTEM-level credit and is recorded as an admin audit action.
	    """
	)
	@PostMapping("/fund-wallet")
	public ResponseEntity<?> fundWallet(@Valid @RequestBody AdminFundWalletRequest request){
		
		log.warn("ADMIN funding wallet | targetUser={} amount={}", request.getUserEmail(), request.getAmount());
		
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
	
	@Operation(
		summary = "Create a new tradable asset",
		description = """
		    Creates a new asset that will be visible in the public market API
		    and available for user trading.
		"""
	)
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
