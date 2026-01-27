package com.kartik.Trading.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "admin_audit_logs")
public class AdminAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;

    @Column(name = "performed_by", nullable = false)
    private String performedBy; 

    @Column(name = "target_user")
    private String targetUser;  

    @Column
    private BigDecimal amount;    

    @Column(nullable = false)
    private LocalDateTime timestamp;

    protected AdminAuditLog() {}

    public AdminAuditLog(
            String action,
            String performedBy,
            String targetUser,
            BigDecimal amount
    ) {
        this.action = action;
        this.performedBy = performedBy;
        this.targetUser = targetUser;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
