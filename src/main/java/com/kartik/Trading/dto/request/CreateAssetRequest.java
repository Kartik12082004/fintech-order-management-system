package com.kartik.Trading.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateAssetRequest {
	
	@NotBlank(message = "Asset symbol is required")
	private String symbol;
	
	@NotNull(message = "Price is required")
	@Positive(message = "Price must be greater than zero")
	private BigDecimal price;
	
	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
