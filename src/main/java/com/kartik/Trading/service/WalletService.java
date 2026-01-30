package com.kartik.Trading.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kartik.Trading.dto.response.TransactionResponse;
import com.kartik.Trading.exception.InsufficientBalanceException;
import com.kartik.Trading.exception.ResourceNotFoundException;
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

    public WalletService(
            WalletRepository walletRepository,
            TransactionRepository transactionRepository
    ) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void credit(User user, BigDecimal amount) {

        log.info("Wallet credit requested | user={} amount={}", user.getEmail(), amount);

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wallet not initialized for user")
                );

        wallet.credit(amount);

        transactionRepository.save(
                new Transaction(wallet, amount, TransactionType.CREDIT, TransactionSource.USER)
        );

        walletRepository.save(wallet);

        log.info("Wallet credited | user={} newBalance={}",
                user.getEmail(), wallet.getBalance());
    }

    @Transactional
    public void debit(User user, BigDecimal amount) {

        log.info("Wallet debit requested | user={} amount={}", user.getEmail(), amount);

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wallet not initialized for user")
                );

        if (wallet.getBalance().compareTo(amount) < 0) {
            log.warn(
                    "INSUFFICIENT BALANCE | user={} balance={} requested={}",
                    user.getEmail(), wallet.getBalance(), amount
            );
            throw new InsufficientBalanceException();
        }

        wallet.debit(amount);

        transactionRepository.save(
                new Transaction(wallet, amount, TransactionType.DEBIT, TransactionSource.USER)
        );

        walletRepository.save(wallet);

        log.info("Wallet debited | user={} newBalance={}",
                user.getEmail(), wallet.getBalance());
    }

    @Transactional
    public void systemCredit(User user, BigDecimal amount) {

        log.warn("SYSTEM CREDIT | user={} amount={}", user.getEmail(), amount);

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wallet not initialized for user")
                );

        wallet.credit(amount);

        transactionRepository.save(
                new Transaction(wallet, amount, TransactionType.CREDIT, TransactionSource.SYSTEM)
        );

        walletRepository.save(wallet);
    }

    public Wallet getWalletByUser(User user) {

        return walletRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wallet not found for user")
                );
    }

    public List<Transaction> getTransactions(User user) {

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wallet not initialized for user")
                );

        return transactionRepository.findByWallet(wallet);
    }

    public List<TransactionResponse> getTransactionHistory(User user) {

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wallet not found for user")
                );

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
