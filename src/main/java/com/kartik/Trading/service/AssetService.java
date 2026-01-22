package com.kartik.Trading.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kartik.Trading.dto.response.AssetResponse;
import com.kartik.Trading.model.Asset;
import com.kartik.Trading.repository.AssetRepository;

@Service
public class AssetService {
	
	private final AssetRepository assetRepository;
	
	public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }
	
	@Transactional
	public void createAsset(String symbol, BigDecimal price) {
		
		symbol = symbol.toUpperCase();
		
		if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
		    throw new IllegalArgumentException("Price must be greater than zero");
		}

		
		if(assetRepository.findBySymbol(symbol).isPresent()) {
			throw new IllegalArgumentException("Asset already exists");
		}
		
		Asset asset = new Asset(null, symbol, price);
		assetRepository.save(asset);
		
	}
	
	@Transactional(readOnly = true)
	public List<AssetResponse> getAllAssets(){
		return assetRepository.findAll()
				.stream()
				.map(a -> new AssetResponse(a.getSymbol(), a.getPrice()))
				.toList();
	}
	
	@Transactional(readOnly = true)
	public AssetResponse getAssetSymbol(String symbol) {
		Asset asset = assetRepository.findBySymbol(symbol.toUpperCase())
				.orElseThrow(() -> new IllegalArgumentException("Asset not found"));
		
		return new AssetResponse(asset.getSymbol(), asset.getPrice());
	}
	
}
