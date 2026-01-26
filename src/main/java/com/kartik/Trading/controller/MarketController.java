package com.kartik.Trading.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kartik.Trading.model.Asset;
import com.kartik.Trading.repository.AssetRepository;

@RestController
@RequestMapping("/api/market")
public class MarketController {
	
	private final AssetRepository assetRepository;
	
	public MarketController(AssetRepository assetRepository) {
		this.assetRepository = assetRepository;
	}
	
	@GetMapping("/assets")
	public ResponseEntity<List<Asset>> getAllAssets(){
		return ResponseEntity.ok(assetRepository.findAll());
	}
	
	@GetMapping("/assets/{symbol}")
	public ResponseEntity<Asset> getAsset(@PathVariable String symbol){
		return ResponseEntity.ok(
				assetRepository.findBySymbol(symbol.toUpperCase())
				.orElseThrow(() -> new IllegalArgumentException("Asset not found"))
		);
	}
	
}
