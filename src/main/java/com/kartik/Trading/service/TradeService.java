package com.kartik.Trading.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kartik.Trading.dto.response.PortfolioResponse;
import com.kartik.Trading.dto.response.TradeHistoryResponse;
import com.kartik.Trading.exception.InsufficientBalanceException;
import com.kartik.Trading.model.Asset;
import com.kartik.Trading.model.Trade;
import com.kartik.Trading.model.TradeType;
import com.kartik.Trading.model.Transaction;
import com.kartik.Trading.model.TransactionSource;
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
		
		BigDecimal totalCost = asset.getPrice()
		        .multiply(quantity)
		        .setScale(2, RoundingMode.HALF_UP);

		if (wallet.getBalance().compareTo(totalCost) < 0) {
		    throw new InsufficientBalanceException();
		}

		wallet.debit(totalCost);

		
		Trade trade = new Trade(user, asset, quantity, asset.getPrice(), TradeType.BUY);
		tradeRepository.save(trade);
		
		Transaction tx = new Transaction(wallet, totalCost, TransactionType.DEBIT, TransactionSource.USER);
		transactionRepository.save(tx);
		
		walletRepository.save(wallet);
	}
	
	@Transactional
	public void sell(User user, String assetSymbol, BigDecimal quantity) {
		
		if(quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Quantity must be positive");
		}
		
		Asset asset = assetRepository.findBySymbol(assetSymbol)
				.orElseThrow(() -> new IllegalArgumentException("Asset not found"));
		
		Wallet wallet = walletRepository.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalStateException("Wallet not initialized"));
		
		BigDecimal ownedQuantity = tradeRepository.getNetQuantity(user, asset);
		
		if (ownedQuantity.compareTo(quantity) < 0) {
			throw new IllegalStateException("Insufficient asset quantity");
		}
		
		BigDecimal totalCredit = asset.getPrice().multiply(quantity);
		
		wallet.credit(totalCredit);
		
		Trade trade = new Trade(user, asset, quantity, asset.getPrice(), TradeType.SELl);
		tradeRepository.save(trade);
		
		Transaction tx = new Transaction(wallet, totalCredit, TransactionType.CREDIT, TransactionSource.USER);
		transactionRepository.save(tx);
		
		walletRepository.save(wallet);
	}
	
	public BigDecimal getHolding(User user, String assetSymbol) {
		
		Asset asset = assetRepository.findBySymbol(assetSymbol)
				.orElseThrow(() -> new IllegalArgumentException("Asset not found"));
		
		return tradeRepository.getNetQuantity(user, asset);
	}
	
	public List<PortfolioResponse> getPortfolio(User user) {
	    List<Asset> assets = assetRepository.findAll();

	    return assets.stream()
	            .map(asset -> {
	                BigDecimal qty = tradeRepository.getNetQuantity(user, asset);

	                if (qty.compareTo(BigDecimal.ZERO) <= 0) {
	                    return null;
	                }

	                return new PortfolioResponse(
	                        asset.getSymbol(),
	                        qty,
	                        asset.getPrice(),
	                        qty.multiply(asset.getPrice())
	                );
	            })
	            .filter(Objects::nonNull)
	            .toList();
	}

	public List<TradeHistoryResponse> getTradeHistory(User user) {
	    return tradeRepository
	            .findByUserOrderByExecutedAtDesc(user)
	            .stream()
	            .map(trade -> new TradeHistoryResponse(
	                    trade.getAsset().getSymbol(),
	                    trade.getQuantity(),
	                    trade.getPriceAtExecution(),
	                    trade.getTradeType(),
	                    trade.getExecutedAt()
	            ))
	            .toList();
	}
	
}
