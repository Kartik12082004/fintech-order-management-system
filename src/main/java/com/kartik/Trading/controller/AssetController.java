package com.kartik.Trading.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kartik.Trading.dto.response.AssetResponse;
import com.kartik.Trading.service.AssetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/assets")
@Tag(name = "Assets", description = "Asset listing APIs")
public class AssetController {
	
	private final AssetService assetService;
	
	public AssetController(AssetService assetService) {
		this.assetService = assetService;
	}
	
	@Operation(summary = "List all tradable assets")
	@GetMapping
	public ResponseEntity<List<AssetResponse>> getAllAssets(){
		return ResponseEntity.ok(assetService.getAllAssets());
	}
	
	@Operation(summary = "Get asset by symbol")
	@GetMapping("/{symbol}")
	public ResponseEntity<AssetResponse> getAsset(@PathVariable String symbol){
		return ResponseEntity.ok(assetService.getAssetSymbol(symbol));
	}
	
}
