package com.kartik.Trading.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "wallet_id", nullable = false)
	private Wallet wallet;
	
	@Column(nullable = false)
	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionType type;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionSource source;
	
	@Column(nullable = false)
	private LocalDateTime timestamp;
	
	public Transaction(Wallet wallet,
					   BigDecimal amount,
					   TransactionType type,
					   TransactionSource source) {
		this.wallet = wallet;
		this.amount = amount;
		this.type = type;
		this.source = source;
		this.timestamp = LocalDateTime.now();
	}
}
