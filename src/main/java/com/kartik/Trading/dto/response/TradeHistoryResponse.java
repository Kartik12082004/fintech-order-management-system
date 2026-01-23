package com.kartik.Trading.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.kartik.Trading.model.TradeType;

public class TradeHistoryResponse {

	private String assetSymbol;
    private BigDecimal quantity;
    private BigDecimal priceAtExecution;
    private TradeType tradeType;
    private LocalDateTime executedAt;
    
    public TradeHistoryResponse(String assetSymbol,
					            BigDecimal quantity,
					            BigDecimal priceAtExecution,
					            TradeType tradeType,
					            LocalDateTime executedAt) {
		this.assetSymbol = assetSymbol;
		this.quantity = quantity;
		this.priceAtExecution = priceAtExecution;
		this.tradeType = tradeType;
		this.executedAt = executedAt;
    }
	
    public String getAssetSymbol() {
    	return assetSymbol;
    }
    
    public BigDecimal getQuantity() {
    	return quantity;
    }
    
    public BigDecimal getPriceAtExecution() {
    	return priceAtExecution;
    }
    
    public TradeType getTradeType() {
        return tradeType;
    }
    
    public LocalDateTime getExecutedAt() {
        return executedAt;
    }
    
}
