package com.kartik.Trading.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kartik.Trading.exception.InsufficientBalanceException;
import com.kartik.Trading.model.Transaction;
import com.kartik.Trading.model.TransactionType;
import com.kartik.Trading.model.User;
import com.kartik.Trading.model.Wallet;
import com.kartik.Trading.repository.TransactionRepository;
import com.kartik.Trading.repository.WalletRepository;

@Service
public class WalletService {
	
	private final WalletRepository walletRepository;
	private final TransactionRepository transactionRepository;
	
	public WalletService(WalletRepository walletRepository,
			TransactionRepository transactionRepository) {
		this.walletRepository = walletRepository;
		this.transactionRepository = transactionRepository;
	}
	
	@Transactional
	public void credit(User user, BigDecimal amount) {
		
		Wallet wallet = walletRepository.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalStateException("Wallet not initialized for user"));
		
		wallet.credit(amount);
		
		Transaction tx = new Transaction(wallet, amount, TransactionType.CREDIT);
		transactionRepository.save(tx);
		
		walletRepository.save(wallet);
		
	}
	
	@Transactional
	public void debit(User user, BigDecimal amount) {
		
		Wallet wallet = walletRepository.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalStateException("Wallet not initialized for user"));
		
		if(wallet.getBalance().compareTo(amount) < 0) {
			throw new InsufficientBalanceException();
		}
		
		wallet.debit(amount);
		
		Transaction tx = new Transaction(wallet, amount, TransactionType.DEBIT);
		transactionRepository.save(tx);
		
		walletRepository.save(wallet);
	}
	
	public Wallet getWalletByUser(User user) {
	    return walletRepository.findByUserId(user.getId())
	            .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
	}

	
	public List<Transaction> getTransactions(User user){
		
		Wallet wallet = walletRepository.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalStateException("Wallet not initialized for user"));
		
		return transactionRepository.findByWallet(wallet);
	}
	
}
