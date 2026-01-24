package com.kartik.Trading.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.kartik.Trading.model.Transaction;
import com.kartik.Trading.model.TransactionSource;
import com.kartik.Trading.model.TransactionType;

public class TransactionResponse {

    private BigDecimal amount;
    private TransactionType type;
    private TransactionSource source;
    private LocalDateTime timestamp;

    public TransactionResponse(
            BigDecimal amount,
            TransactionType type,
            TransactionSource source,
            LocalDateTime timestamp) {
        this.amount = amount;
        this.type = type;
        this.source = source;
        this.timestamp = timestamp;
    }
    
    public static TransactionResponse fromEntity(Transaction tx) {
        return new TransactionResponse(
                tx.getAmount(),
                tx.getType(),
                tx.getSource(),
                tx.getTimestamp()
        );
    }

    public BigDecimal getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public TransactionSource getSource() { return source; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
