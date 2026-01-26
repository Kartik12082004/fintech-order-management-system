package com.kartik.Trading.dto.response;

import java.math.BigDecimal;

public class AdminDashboardResponse {

	 private long totalUsers;
	 private long totalAssets;
	 private long totalTrades;
	 private BigDecimal totalWalletBalance;
	 private long buyTrades;
	 private long sellTrades;
	 
	 public AdminDashboardResponse(
	         long totalUsers,
	         long totalAssets,
	         long totalTrades,
	         BigDecimal totalWalletBalance,
	         long buyTrades,
	         long sellTrades
	 ) {
	     this.totalUsers = totalUsers;
	     this.totalAssets = totalAssets;
	     this.totalTrades = totalTrades;
	     this.totalWalletBalance = totalWalletBalance;
	     this.buyTrades = buyTrades;
	     this.sellTrades = sellTrades;
	 }

	    public long getTotalUsers() { return totalUsers; }
	    public long getTotalAssets() { return totalAssets; }
	    public long getTotalTrades() { return totalTrades; }
	    public BigDecimal getTotalWalletBalance() { return totalWalletBalance; }
	    public long getBuyTrades() { return buyTrades; }
	    public long getSellTrades() { return sellTrades; }
}
