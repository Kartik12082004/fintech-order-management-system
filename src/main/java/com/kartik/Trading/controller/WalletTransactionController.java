package com.kartik.Trading.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kartik.Trading.dto.response.TransactionResponse;
import com.kartik.Trading.model.User;
import com.kartik.Trading.repository.UserRepository;
import com.kartik.Trading.service.AdminTransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/wallet/transactions")
@Tag(name = "Wallet Transactions", description = "Wallet transaction history APIs")
public class WalletTransactionController {

	private final AdminTransactionService adminTransactionService;
    private final UserRepository userRepository;

    public WalletTransactionController(
            AdminTransactionService adminTransactionService,
            UserRepository userRepository) {
        this.adminTransactionService = adminTransactionService;
        this.userRepository = userRepository;
    }
	
    @Operation(summary = "Get wallet transaction history")
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getWalletTransactions(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return ResponseEntity.ok(adminTransactionService.getUserWalletTransactions(user));
    }
    
}
