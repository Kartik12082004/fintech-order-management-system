package com.kartik.Trading.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kartik.Trading.model.AdminAuditLog;

public interface AdminAuditLogRepository extends JpaRepository<AdminAuditLog, Long> {
	
	List<AdminAuditLog> findAllByOrderByTimestampDesc();
	List<AdminAuditLog> findByPerformedByOrderByTimestampDesc(String performedBy);
	List<AdminAuditLog> findByTargetUserOrderByTimestampDesc(String targetUser);

	Page<AdminAuditLog> findByTimestampBetween(
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    );

    Page<AdminAuditLog> findByTargetUserAndTimestampBetween(
            String targetUser,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    );
	
}
