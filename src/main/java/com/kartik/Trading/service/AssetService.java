package com.kartik.Trading.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kartik.Trading.dto.response.AssetResponse;
import com.kartik.Trading.exception.ConflictException;
import com.kartik.Trading.exception.ResourceNotFoundException;
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

        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Asset symbol is required");
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        symbol = symbol.toUpperCase();

        if (assetRepository.findBySymbol(symbol).isPresent()) {
            throw new ConflictException("Asset already exists: " + symbol);
        }

        Asset asset = new Asset(null, symbol, price);
        assetRepository.save(asset);
    }

    @Transactional(readOnly = true)
    public List<AssetResponse> getAllAssets() {

        return assetRepository.findAll()
                .stream()
                .map(a -> new AssetResponse(a.getSymbol(), a.getPrice()))
                .toList();
    }

    @Transactional(readOnly = true)
    public AssetResponse getAssetSymbol(String symbol) {

        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Asset symbol is required");
        }

        Asset asset = assetRepository.findBySymbol(symbol.toUpperCase())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Asset not found: " + symbol)
                );

        return new AssetResponse(asset.getSymbol(), asset.getPrice());
    }
}
