package com.kartik.Trading.controller;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kartik.Trading.model.AdminAuditLog;
import com.kartik.Trading.service.AdminAuditLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/audit-logs")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Audit Logs", description = "Admin-only audit trail APIs")
public class AdminAuditLogController {
	
	private final AdminAuditLogService adminAuditLogService;

    public AdminAuditLogController(AdminAuditLogService adminAuditLogService) {
        this.adminAuditLogService = adminAuditLogService;
    }
	
    @Operation(summary = "Get admin audit logs with filters and pagination")
    @GetMapping
    public ResponseEntity<Page<AdminAuditLog>> getLogs(
            @RequestParam(required = false) String targetUser,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,

            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(
        		adminAuditLogService.getLogs(targetUser, from, to, pageable)
        );
    }
    
}
