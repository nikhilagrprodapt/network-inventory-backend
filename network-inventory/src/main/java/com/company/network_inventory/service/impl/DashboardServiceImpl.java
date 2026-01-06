package com.company.network_inventory.service.impl;

import com.company.network_inventory.audit.entity.AuditLog;
import com.company.network_inventory.audit.repository.AuditLogRepository;
import com.company.network_inventory.dto.DashboardResponse;
import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.enums.CustomerStatus;
import com.company.network_inventory.entity.enums.FiberLineStatus;
import com.company.network_inventory.repository.*;
import com.company.network_inventory.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CustomerRepository customerRepository;
    private final SplitterRepository splitterRepository;
    private final FDHRepository fdhRepository;
    private final HeadendRepository headendRepository;
    private final CoreSwitchRepository coreSwitchRepository;
    private final FiberDropLineRepository fiberDropLineRepository;
    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {

        long totalCustomers = customerRepository.count();
        long activeCustomers = customerRepository.countByStatus(CustomerStatus.ACTIVE);
        long pendingCustomers = customerRepository.countByStatus(CustomerStatus.PENDING);

        long totalSplitters = splitterRepository.count();
        long totalFdh = fdhRepository.count();
        long totalHeadends = headendRepository.count();
        long totalCoreSwitches = coreSwitchRepository.count();

        long totalFiberLines = fiberDropLineRepository.count();
        long activeFiberLines = fiberDropLineRepository.countByStatus(FiberLineStatus.ACTIVE);
        long disconnectedLines = fiberDropLineRepository.countByStatus(FiberLineStatus.DISCONNECTED);

        List<Customer> recentCustomers = customerRepository.findTop6ByOrderByCustomerIdDesc();
        List<AuditLog> recentAuditLogs = auditLogRepository.findTop50ByOrderByCreatedAtDesc();

        return DashboardResponse.builder()
                .totalCustomers(totalCustomers)
                .activeCustomers(activeCustomers)
                .pendingCustomers(pendingCustomers)
                .totalSplitters(totalSplitters)
                .totalFdh(totalFdh)
                .totalHeadends(totalHeadends)
                .totalCoreSwitches(totalCoreSwitches)
                .totalFiberDropLines(totalFiberLines)
                .activeFiberLines(activeFiberLines)
                .disconnectedLines(disconnectedLines)
                .recentCustomers(recentCustomers)
                .recentAuditLogs(recentAuditLogs)
                .build();
    }
}
