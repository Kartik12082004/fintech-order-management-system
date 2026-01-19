package com.kartik.Trading.dto.response;

import java.math.BigDecimal;

public class WalletResponse {

	private BigDecimal balance;

    public WalletResponse(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }
	
}
