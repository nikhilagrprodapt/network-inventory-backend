package com.company.network_inventory.audit.service;

import com.company.network_inventory.audit.entity.AuditLog;
import com.company.network_inventory.audit.repository.AuditLogRepository;
import com.company.network_inventory.util.RequestContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void log(String action, String entityType, Long entityId, String details) {
        AuditLog log = AuditLog.builder()
                .actor(RequestContext.getActor())
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .details(details)
                .requestId(RequestContext.getRequestId())
                .createdAt(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }
}
