package com.kartik.Trading.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kartik.Trading.dto.response.TransactionResponse;
import com.kartik.Trading.exception.InsufficientBalanceException;
import com.kartik.Trading.model.Transaction;
import com.kartik.Trading.model.TransactionSource;
import com.kartik.Trading.model.TransactionType;
import com.kartik.Trading.model.User;
import com.kartik.Trading.model.Wallet;
import com.kartik.Trading.repository.TransactionRepository;
import com.kartik.Trading.repository.WalletRepository;

@Service
public class WalletService {
	
	private static final Logger log = LoggerFactory.getLogger(WalletService.class);
	
	private final WalletRepository walletRepository;
	private final TransactionRepository transactionRepository;
	
	public WalletService(WalletRepository walletRepository,
			TransactionRepository transactionRepository) {
		this.walletRepository = walletRepository;
		this.transactionRepository = transactionRepository;
	}
	
	@Transactional
	public void credit(User user, BigDecimal amount) {
		
		log.info("Wallet credit requested | user={} amount={}", user.getEmail(), amount);
		
		Wallet wallet = walletRepository.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalStateException("Wallet not initialized for user"));
		
		wallet.credit(amount);
		
		Transaction tx = new Transaction(wallet, amount, TransactionType.CREDIT, TransactionSource.USER);
		transactionRepository.save(tx);
		
		walletRepository.save(wallet);
		
		log.info("Wallet credited successfully | user={} newBalance={}", user.getEmail(), wallet.getBalance());
		
	}
	
	@Transactional
	public void debit(User user, BigDecimal amount) {
		
		log.info("Wallet debit requested | user={} amount={}", user.getEmail(), amount);
		
		Wallet wallet = walletRepository.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalStateException("Wallet not initialized for user"));
		
		if(wallet.getBalance().compareTo(amount) < 0) {
			 log.warn("INSUFFICIENT BALANCE | user={} balance={} requested={}", user.getEmail(), wallet.getBalance(), amount);
			throw new InsufficientBalanceException();
		}
		
		wallet.debit(amount);
		
		Transaction tx = new Transaction(wallet, amount, TransactionType.DEBIT, TransactionSource.USER);
		transactionRepository.save(tx);
		
		walletRepository.save(wallet);
		
		log.info("Wallet debited successfully | user={} newBalance={}", user.getEmail(), wallet.getBalance());
	}
	
	@Transactional
	public void systemCredit(User user, BigDecimal amount) {
		
		log.warn("SYSTEM CREDIT | user={} amount={}", user.getEmail(), amount);
		
		Wallet wallet = walletRepository.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalStateException("Wallet not initialized"));
		
		wallet.credit(amount);
		
		Transaction tx = new Transaction(wallet, amount, TransactionType.CREDIT, TransactionSource.SYSTEM);
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
	
	public List<TransactionResponse> getTransactionHistory(User user) {

	    Wallet wallet = walletRepository.findByUserId(user.getId())
	            .orElseThrow(() -> new IllegalStateException("Wallet not found"));

	    return transactionRepository
	            .findByWalletOrderByTimestampDesc(wallet)
	            .stream()
	            .map(tx -> new TransactionResponse(
	                    tx.getAmount(),
	                    tx.getType(),
	                    tx.getSource(),
	                    tx.getTimestamp()
	            ))
	            .toList();
	}

	
}
