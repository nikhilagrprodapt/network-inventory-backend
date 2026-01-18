package com.company.network_inventory.audit.controller;

import com.company.network_inventory.audit.dto.AuditLogRequest;
import com.company.network_inventory.audit.entity.AuditLog;
import com.company.network_inventory.audit.repository.AuditLogRepository;
import com.company.network_inventory.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    // Example: /api/audit/search?actor=admin&action=ASSIGN&entityType=ONT&days=7
    @GetMapping("/search")
    public ApiResponse<List<AuditLog>> search(
            @RequestParam(value = "actor", required = false) String actor,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "entityType", required = false) String entityType,
            @RequestParam(value = "days", defaultValue = "7") int days,
            @RequestParam(value = "limit", defaultValue = "200") int limit
    ) {
        int safeDays = Math.max(0, Math.min(days, 365));
        int safeLimit = Math.max(1, Math.min(limit, 5000));

        LocalDateTime since = (safeDays == 0) ? null : LocalDateTime.now().minusDays(safeDays);

        List<AuditLog> logs = auditLogRepository.search(
                actor, action, entityType,
                since,
                PageRequest.of(0, safeLimit)
        );
        return ApiResponse.ok("Audit search results", logs);
    }

    @GetMapping("/{id}")
    public ApiResponse<AuditLog> getById(@PathVariable("id") Long id) {
        AuditLog log = auditLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Audit log not found: " + id));
        return ApiResponse.ok("Audit log", log);
    }

    @GetMapping(value = "/export.csv", produces = "text/csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(value = "actor", required = false) String actor,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "entityType", required = false) String entityType,
            @RequestParam(value = "days", defaultValue = "7") int days
    ) {
        int safeDays = Math.max(0, Math.min(days, 365));
        LocalDateTime since = (safeDays == 0) ? null : LocalDateTime.now().minusDays(safeDays);

        // export more than UI limit
        List<AuditLog> logs = auditLogRepository.search(
                actor, action, entityType,
                since,
                PageRequest.of(0, 20000)
        );

        StringBuilder sb = new StringBuilder();
        sb.append("id,createdAt,actor,action,entityType,entityId,requestId,details\n");

        for (AuditLog a : logs) {
            sb.append(csv(a.getId())).append(',')
                    .append(csv(a.getCreatedAt())).append(',')
                    .append(csv(a.getActor())).append(',')
                    .append(csv(a.getAction())).append(',')
                    .append(csv(a.getEntityType())).append(',')
                    .append(csv(a.getEntityId())).append(',')
                    .append(csv(a.getRequestId())).append(',')
                    .append(csv(a.getDetails()))
                    .append("\n");
        }

        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);

        String filename = "audit_export_" + LocalDateTime.now().toString().replace(":", "-") + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(new MediaType("text", "csv"))
                .body(bytes);
    }

    @PostMapping("/log")
    public ApiResponse<AuditLog> log(@Valid @RequestBody AuditLogRequest req,
                                     @RequestHeader(value = "X-Request-Id", required = false) String requestId) {

        String actor = getActor();
        String rid = (requestId == null || requestId.isBlank()) ? UUID.randomUUID().toString() : requestId;

        AuditLog log = AuditLog.builder()
                .actor(actor)
                .action(req.getAction())
                .entityType(req.getEntityType())
                .entityId(req.getEntityId())
                .details(req.getDetails())
                .requestId(rid)
                .build();

        AuditLog saved = auditLogRepository.save(log);
        return ApiResponse.ok("Audit logged", saved);
    }

    private String getActor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return "SYSTEM";
        String name = auth.getName();
        return (name == null || name.isBlank()) ? "SYSTEM" : name;
    }

    private String csv(Object value) {
        if (value == null) return "\"\"";
        String s = String.valueOf(value);
        // escape quotes for CSV
        s = s.replace("\"", "\"\"");
        return "\"" + s + "\"";
    }
}
