package com.kartik.Trading.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kartik.Trading.dto.response.AdminDashboardResponse;
import com.kartik.Trading.service.AdminDashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Dashboard", description = "System-wide admin metrics")
public class AdminDashboardController {
	
	private final AdminDashboardService adminDashboardService;

	public AdminDashboardController(AdminDashboardService adminDashboardService) {
		this.adminDashboardService = adminDashboardService;
	}
	
	@Operation(summary = "Get system dashboard metrics")
	@GetMapping
	public ResponseEntity<AdminDashboardResponse> getDashboard(){
		return ResponseEntity.ok(adminDashboardService.getSummary());
	}
}
