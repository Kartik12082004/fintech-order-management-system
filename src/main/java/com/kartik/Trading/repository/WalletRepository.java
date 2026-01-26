package com.kartik.Trading.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kartik.Trading.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
	Optional<Wallet> findByUserId(long userId);
	
	@Query("SELECT COALESCE(SUM(w.balance), 0) FROM Wallet w")
	BigDecimal getTotalBalance();

}
