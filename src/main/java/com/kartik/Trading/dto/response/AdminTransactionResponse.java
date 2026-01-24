package com.kartik.Trading.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.kartik.Trading.model.TransactionSource;
import com.kartik.Trading.model.TransactionType;

public class AdminTransactionResponse {

    private Long transactionId;
    private String userEmail;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionSource source;
    private LocalDateTime timestamp;

    public AdminTransactionResponse(Long transactionId,
						            String userEmail,
						            BigDecimal amount,
						            TransactionType type,
						            TransactionSource source,
						            LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.userEmail = userEmail;
        this.amount = amount;
        this.type = type;
        this.source = source;
        this.timestamp = timestamp;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionSource getSource() {
        return source;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
