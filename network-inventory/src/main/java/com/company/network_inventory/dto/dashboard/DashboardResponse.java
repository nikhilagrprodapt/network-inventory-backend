package com.company.network_inventory.dto.dashboard;

import com.company.network_inventory.audit.entity.AuditLog;
import com.company.network_inventory.dto.CustomerResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardResponse {

    // counts
    private long customers;
    private long activeCustomers;
    private long pendingCustomers;

    private long splitters;
    private long fdhs;
    private long headends;
    private long coreSwitches;

    private long fiberDropLines;
    private long activeFiberLines;
    private long disconnectedFiberLines;

    // recent tables
    private List<CustomerResponse> recentCustomers;
    private List<AuditLog> recentAuditLogs;
}
