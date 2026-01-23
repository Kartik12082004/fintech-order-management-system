package com.kartik.Trading.dto.response;

import java.math.BigDecimal;

public class PortfolioResponse {
	
	private String assetSymbol;
	private BigDecimal quantity;
	private BigDecimal currentPrice;
	private BigDecimal value;
	
	public PortfolioResponse(String assetSymbol,
				             BigDecimal quantity,
				             BigDecimal currentPrice,
				             BigDecimal value) {
		this.assetSymbol = assetSymbol;
		this.quantity = quantity;
		this.currentPrice = currentPrice;
		this.value = value;
	}
	
	public String getAssetSymbol() {
		return assetSymbol;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
}
