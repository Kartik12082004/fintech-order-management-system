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
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional = false)
	private User user;
	
	@ManyToOne(optional = false)
	private Asset asset;
	
	@Column(nullable = false)
	private BigDecimal quantity;
	
	@Column(nullable = false)
	private BigDecimal priceAtExecution;
	
	@Enumerated(EnumType.STRING)
	private TradeType tradeType;
	
	private LocalDateTime executedAt;
	
	public Trade(User user, Asset asset, BigDecimal quantity,
			BigDecimal priceAtExecution, TradeType tradeType) {
		
		this.user = user;
        this.asset = asset;
        this.quantity = quantity;
        this.priceAtExecution = priceAtExecution;
        this.tradeType = tradeType;
        this.executedAt = LocalDateTime.now();
	}
	
}
