package com.kartik.Trading.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kartik.Trading.model.Transaction;
import com.kartik.Trading.model.Wallet;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	List<Transaction> findByWallet(Wallet wallet);
	List<Transaction> findAllByOrderByTimestampDesc();
	List<Transaction> findByWalletOrderByTimestampDesc(Wallet wallet);
}
