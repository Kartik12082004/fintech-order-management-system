package com.kartik.Trading.dto.response;

import java.math.BigDecimal;

public class AssetResponse {
	
	private String symbol;
	private BigDecimal price;
	
	public AssetResponse(String symbol, BigDecimal price) {
		this.symbol = symbol;
		this.price = price;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
}
