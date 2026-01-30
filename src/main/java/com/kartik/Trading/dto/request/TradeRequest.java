package com.kartik.Trading.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TradeRequest {
	
	@NotBlank
	private String assetSymbol;
	
	@NotNull
	@Positive
	private BigDecimal quantity;
	
	public void setAssetSymbol(String assetSymbol) {
		this.assetSymbol = assetSymbol; 
	}
	
	public String getAssetSymbol() {
		return assetSymbol;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
}
