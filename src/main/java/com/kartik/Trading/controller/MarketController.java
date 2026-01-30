package com.kartik.Trading.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kartik.Trading.dto.response.AssetResponse;
import com.kartik.Trading.exception.ResourceNotFoundException;
import com.kartik.Trading.model.Asset;
import com.kartik.Trading.repository.AssetRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/market")
@Tag(
	    name = "Market",
	    description = "Public market APIs for viewing available assets and prices"
)
public class MarketController {
	
	private final AssetRepository assetRepository;
	
	public MarketController(AssetRepository assetRepository) {
		this.assetRepository = assetRepository;
	}
	
	@Operation(
	        summary = "Get all tradable assets",
	        description = "Returns a list of all assets available in the market with current prices"
	)
	@GetMapping("/assets")
	public ResponseEntity<List<AssetResponse>> getAllAssets() {
	    return ResponseEntity.ok(
	            assetRepository.findAll()
	                    .stream()
	                    .map(a -> new AssetResponse(a.getSymbol(), a.getPrice()))
	                    .toList()
	    );
	}

	@Operation(
		        summary = "Get asset by symbol",
		        description = "Fetch details of a specific asset using its symbol (case-insensitive)"
	)
	@GetMapping("/assets/{symbol}")
	public ResponseEntity<AssetResponse> getAsset(@PathVariable String symbol) {
	    Asset asset = assetRepository.findBySymbol(symbol.toUpperCase())
	            .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

	    return ResponseEntity.ok(
	            new AssetResponse(asset.getSymbol(), asset.getPrice())
	    );
	}

	
}
