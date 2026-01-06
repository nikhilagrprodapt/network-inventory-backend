package com.company.network_inventory.audit.dto;

import com.company.network_inventory.audit.entity.AuditLog;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuditPageResponse {
    private List<AuditLog> items;
    private int page;
    private int size;
    private boolean hasNext;
}
