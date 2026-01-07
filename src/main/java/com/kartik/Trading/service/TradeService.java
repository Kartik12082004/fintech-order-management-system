package com.kartik.Trading.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kartik.Trading.model.Asset;
import com.kartik.Trading.model.Trade;
import com.kartik.Trading.model.TradeType;
import com.kartik.Trading.model.Transaction;
import com.kartik.Trading.model.TransactionType;
import com.kartik.Trading.model.User;
import com.kartik.Trading.model.Wallet;
import com.kartik.Trading.repository.AssetRepository;
import com.kartik.Trading.repository.TradeRepository;
import com.kartik.Trading.repository.TransactionRepository;
import com.kartik.Trading.repository.WalletRepository;

@Service
public class TradeService {
	
	private final WalletRepository walletRepository;
	private final AssetRepository assetRepository;
	private final TradeRepository tradeRepository;
	private final TransactionRepository transactionRepository;
	
	public TradeService(WalletRepository walletRepository,
			AssetRepository assetRepository,
			TradeRepository tradeRepository,
			TransactionRepository transactionRepository) {
		
		this.walletRepository = walletRepository;
        this.assetRepository = assetRepository;
        this.tradeRepository = tradeRepository;
        this.transactionRepository = transactionRepository;
	}
	
	@Transactional
	public void buy(User user, String assetSymbol, BigDecimal quantity) {
		
		if(quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Quantity must be positive");
		}
		
		Asset asset = assetRepository.findBySymbol(assetSymbol)
				.orElseThrow(() -> new IllegalArgumentException("Asset not found"));
		
		Wallet wallet = walletRepository.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalStateException("Wallet not initialized"));
		
		BigDecimal totalCost = asset.getPrice().multiply(quantity);
		
		wallet.debit(totalCost);
		
		Trade trade = new Trade(user, asset, quantity, asset.getPrice(), TradeType.BUY);
		tradeRepository.save(trade);
		
		Transaction tx = new Transaction(wallet, totalCost, TransactionType.DEBIT);
		transactionRepository.save(tx);
		
		walletRepository.save(wallet);
	}
}
