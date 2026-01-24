package com.kartik.Trading.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kartik.Trading.dto.response.AdminTransactionResponse;
import com.kartik.Trading.dto.response.TransactionResponse;
import com.kartik.Trading.model.Transaction;
import com.kartik.Trading.model.User;
import com.kartik.Trading.model.Wallet;
import com.kartik.Trading.repository.TransactionRepository;
import com.kartik.Trading.repository.WalletRepository;

@Service
public class AdminTransactionService {

	private final TransactionRepository transactionRepository;
	private final WalletRepository walletRepository;

    public AdminTransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }
    
    @Transactional(readOnly = true)
    public List<AdminTransactionResponse> getAllTransactions() {

        return transactionRepository.findAllByOrderByTimestampDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
	
    @Transactional(readOnly = true)
    public List<TransactionResponse> getUserWalletTransactions(User user) {

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalStateException("Wallet not found"));

        return transactionRepository
                .findByWalletOrderByTimestampDesc(wallet)
                .stream()
                .map(TransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    private AdminTransactionResponse mapToResponse(Transaction tx) {
        return new AdminTransactionResponse(
                tx.getId(),
                tx.getWallet().getUser().getEmail(),
                tx.getAmount(),
                tx.getType(),
                tx.getSource(),
                tx.getTimestamp()
        );
    }
    
}
