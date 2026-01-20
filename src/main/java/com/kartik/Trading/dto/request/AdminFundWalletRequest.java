package com.kartik.Trading.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AdminFundWalletRequest {

	@NotNull(message = "User email is required")
    @Email(message = "Email must be valid")
	private String userEmail;
	
	@NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
	private BigDecimal amount;
	
	public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
	
}
