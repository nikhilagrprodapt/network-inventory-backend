package com.company.network_inventory.audit.service;

public interface AuditService {
    void log(String action, String entityType, Long entityId, String details);
}
