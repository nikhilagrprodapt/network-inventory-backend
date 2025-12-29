package com.company.network_inventory.audit.controller;

import com.company.network_inventory.audit.entity.AuditLog;
import com.company.network_inventory.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.company.network_inventory.util.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditLogRepository auditLogRepository;

    @GetMapping("/recent")
    public ApiResponse<List<AuditLog>> recent() {
        List<AuditLog> logs = auditLogRepository.findTop50ByOrderByCreatedAtDesc();
        return ApiResponse.ok("Recent audit logs", logs);
    }

}
