package com.company.network_inventory.audit.service;

import com.company.network_inventory.audit.entity.AuditLog;
import com.company.network_inventory.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void log(String action, String entityType, Long entityId, String details) {

        String actor = resolveActor();                 // ✅ always set
        String requestId = resolveRequestId();         // ✅ always set

        AuditLog log = AuditLog.builder()
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .details(details)
                .actor(actor)
                .requestId(requestId)
                .build();

        auditLogRepository.save(log);
    }

    private String resolveActor() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && auth.getName() != null) {
                String name = auth.getName();
                if (!"anonymousUser".equalsIgnoreCase(name)) return name;
            }
        } catch (Exception ignored) {}
        return "SYSTEM"; // ✅ default since you don’t have auth yet
    }

    private String resolveRequestId() {
        String rid = MDC.get("requestId");
        if (rid != null && !rid.isBlank()) return rid;
        return UUID.randomUUID().toString(); // ✅ fallback if filter not applied somehow
    }
}
