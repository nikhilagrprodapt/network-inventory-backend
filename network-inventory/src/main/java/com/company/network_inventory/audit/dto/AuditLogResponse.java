package com.company.network_inventory.audit.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogResponse {
    private Long id;
    private LocalDateTime createdAt;
    private String actor;
    private String action;
    private String entityType;
    private Long entityId;
    private String details;
    private String requestId;
}
