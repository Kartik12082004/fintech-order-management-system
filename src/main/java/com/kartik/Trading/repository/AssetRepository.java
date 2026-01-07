package com.kartik.Trading.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kartik.Trading.model.Asset;


public interface AssetRepository extends JpaRepository<Asset, Long> {
	Optional<Asset> findBySymbol(String symbol);
}
