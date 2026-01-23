package com.kartik.Trading.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kartik.Trading.dto.request.TradeRequest;
import com.kartik.Trading.model.User;
import com.kartik.Trading.repository.UserRepository;
import com.kartik.Trading.service.TradeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/trades")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Trades", description = "Trading operations like buy, sell, holdings, history, portfolio")
public class TradeController {

	private final TradeService tradeService;
	private final UserRepository userRepository;
	
	public TradeController(TradeService tradeService,
						   UserRepository userRepository) {
		this.tradeService = tradeService;
		this.userRepository = userRepository;
	}
	
	@Operation(summary = "Buy an asset")
	@PostMapping("/buy")
	public ResponseEntity<?> buy(@AuthenticationPrincipal UserDetails userDetails,
								 @Valid @RequestBody TradeRequest request){
		
		User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
		
		tradeService.buy(user,
						 request.getAssetSymbol(),
						 request.getQuantity()
		);
		
		return ResponseEntity.ok("BUY successful");
	}
	
	@Operation(summary = "Sell an asset")
	@PostMapping("/sell")
    public ResponseEntity<?> sell(@AuthenticationPrincipal UserDetails userDetails,
    							  @Valid @RequestBody TradeRequest request) {
        
		User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
		
		tradeService.sell(user,
			              request.getAssetSymbol(),
			              request.getQuantity()
        );
        
        return ResponseEntity.ok("SELL successful");
    }
	
	@Operation(summary = "Get holding quantity of an asset")
	@GetMapping("/holding/{assetSymbol}")
	public ResponseEntity<?> holding(@AuthenticationPrincipal UserDetails userDetails,
	                                    @PathVariable String assetSymbol) {
	        
		User user = userRepository.findByEmail(userDetails.getUsername())
	               .orElseThrow(() -> new IllegalArgumentException("User not found"));
		 
		return ResponseEntity.ok(tradeService.getHolding(user, assetSymbol));
	}
	
	
	@Operation(summary = "Get trade history")
	@GetMapping("/history")
	public ResponseEntity<?> history(@AuthenticationPrincipal UserDetails userDetails) {
	    
		User user = userRepository.findByEmail(userDetails.getUsername())
	            .orElseThrow(() -> new IllegalArgumentException("User not found"));

	    return ResponseEntity.ok(tradeService.getTradeHistory(user));
	}
	
	@Operation(summary = "Get portfolio summary")
	@GetMapping("/portfolio")
	public ResponseEntity<?> portfolio(@AuthenticationPrincipal UserDetails userDetails) {
	    
		User user = userRepository.findByEmail(userDetails.getUsername())
	            .orElseThrow(() -> new IllegalArgumentException("User not found"));

	    return ResponseEntity.ok(tradeService.getPortfolio(user));
	}

}
