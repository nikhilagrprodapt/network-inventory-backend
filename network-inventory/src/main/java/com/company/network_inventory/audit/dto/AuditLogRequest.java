package com.company.network_inventory.audit.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogRequest {

    @NotBlank
    private String action;     // e.g. TOPOLOGY_VIEW, TOPOLOGY_EXPORT, NODE_VIEW

    @NotBlank
    private String entityType; // e.g. HEADEND, FDH, SPLITTER, CUSTOMER

    private Long entityId;     // optional

    private String details;    // optional
}
