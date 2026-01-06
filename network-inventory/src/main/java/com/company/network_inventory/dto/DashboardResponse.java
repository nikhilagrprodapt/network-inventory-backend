package com.company.network_inventory.dto;

import com.company.network_inventory.audit.entity.AuditLog;
import com.company.network_inventory.entity.Customer;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardResponse {

    // counts
    private long totalCustomers;
    private long activeCustomers;
    private long pendingCustomers;

    private long totalSplitters;
    private long totalFdh;
    private long totalHeadends;
    private long totalCoreSwitches;

    private long totalFiberDropLines;
    private long activeFiberLines;
    private long disconnectedLines;

    // recent lists
    private List<Customer> recentCustomers;
    private List<AuditLog> recentAuditLogs;
}
