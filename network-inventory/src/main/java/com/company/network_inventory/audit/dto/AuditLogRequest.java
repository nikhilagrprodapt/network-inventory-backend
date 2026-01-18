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
    private String action;

    @NotBlank
    private String entityType;

    private Long entityId;

    private String details;
}
