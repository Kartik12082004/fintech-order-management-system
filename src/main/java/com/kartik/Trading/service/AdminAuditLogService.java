package com.kartik.Trading.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kartik.Trading.model.AdminAuditLog;
import com.kartik.Trading.repository.AdminAuditLogRepository;

@Service
public class AdminAuditLogService {

	private static final Logger log = LoggerFactory.getLogger(AdminAuditLogService.class);

    private final AdminAuditLogRepository adminAuditLogRepository;

    public AdminAuditLogService(AdminAuditLogRepository adminAuditLogRepository) {
        this.adminAuditLogRepository = adminAuditLogRepository;
    }
    
    @Transactional
    public void logAction(
            String action,
            String performedBy,
            String targetUser,
            BigDecimal amount
    ) {
        AdminAuditLog auditLog = new AdminAuditLog(
                action,
                performedBy,
                targetUser,
                amount
        );

        adminAuditLogRepository.save(auditLog);

        log.info(
            "ADMIN AUDIT | action={} admin={} target={} amount={}",
            action, performedBy, targetUser, amount
        );
    }
    
    @Transactional(readOnly = true)
    public List<AdminAuditLog> getAllLogs() {
        return adminAuditLogRepository.findAllByOrderByTimestampDesc();
    }

    @Transactional(readOnly = true)
    public List<AdminAuditLog> getLogsByAdmin(String adminEmail) {
        return adminAuditLogRepository
                .findByPerformedByOrderByTimestampDesc(adminEmail);
    }

    @Transactional(readOnly = true)
    public List<AdminAuditLog> getLogsForUser(String userEmail) {
        return adminAuditLogRepository
                .findByTargetUserOrderByTimestampDesc(userEmail);
    }
    
    @Transactional(readOnly = true)
    public Page<AdminAuditLog> getLogs(
            String targetUser,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    ) {

        if (targetUser != null && !targetUser.isBlank()) {
            return adminAuditLogRepository.findByTargetUserAndTimestampBetween(
                    targetUser, from, to, pageable
            );
        }

        return adminAuditLogRepository.findByTimestampBetween(from, to, pageable);
    }
	
}
