package com.kartik.Trading.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kartik.Trading.dto.response.PortfolioResponse;
import com.kartik.Trading.model.User;
import com.kartik.Trading.repository.UserRepository;
import com.kartik.Trading.service.PortfolioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/portfolio")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Portfolio", description = "User portfolio summary")
public class PortfolioController {
	
	private final PortfolioService portfolioService;
    private final UserRepository userRepository;

    public PortfolioController(PortfolioService portfolioService,
                               UserRepository userRepository) {
        this.portfolioService = portfolioService;
        this.userRepository = userRepository;
    }
    
    @GetMapping
    @Operation(summary = "Get user portfolio summary")
    public ResponseEntity<List<PortfolioResponse>> portfolio(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return ResponseEntity.ok(portfolioService.getPortfolio(user));
    }
	
}
