package com.kartik.Trading.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kartik.Trading.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
	Optional<Wallet> findByUserId(long userId);
}
