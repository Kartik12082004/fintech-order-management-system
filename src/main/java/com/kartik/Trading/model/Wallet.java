package com.kartik.Trading.model;

import java.math.BigDecimal;

import com.kartik.Trading.exception.InsufficientBalanceException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;

@Entity
public class Wallet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal balance;
	
	@Version
	private Long version;
	
	protected Wallet() {}
	
	public Wallet(User user) {
		this.user = user;
		this.balance = BigDecimal.ZERO;
	}
	
	public long getId() {
	    return id;
	}

	public User getUser() {
	    return user;
	}

	public BigDecimal getBalance() {
	    return balance;
	}
	
	public Long getVersion() {
		return version;
	}
	
	public void credit(BigDecimal amount) {
		validateAmount(amount);
		this.balance = this.balance.add(amount);
	}
	
	public void debit(BigDecimal amount) {
		validateAmount(amount);
		
		if(this.balance.compareTo(amount) < 0) {
			throw new InsufficientBalanceException();
		}
		
		this.balance = this.balance.subtract(amount);
	}
	
	 private void validateAmount(BigDecimal amount) {
	    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
	        throw new IllegalArgumentException("Amount must be positive");
	    }
	 }
	
}
