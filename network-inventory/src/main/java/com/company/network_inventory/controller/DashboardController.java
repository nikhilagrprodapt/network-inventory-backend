package com.company.network_inventory.controller;

import com.company.network_inventory.audit.entity.AuditLog;
import com.company.network_inventory.audit.repository.AuditLogRepository;
import com.company.network_inventory.dto.CustomerResponse;
import com.company.network_inventory.dto.dashboard.DashboardResponse;
import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.FiberDropLine;
import com.company.network_inventory.entity.enums.CustomerStatus;
import com.company.network_inventory.entity.enums.FiberLineStatus;
import com.company.network_inventory.repository.*;
import com.company.network_inventory.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final CustomerRepository customerRepository;
    private final SplitterRepository splitterRepository;
    private final FDHRepository fdhRepository;
    private final HeadendRepository headendRepository;
    private final CoreSwitchRepository coreSwitchRepository;
    private final FiberDropLineRepository fiberDropLineRepository;
    private final AuditLogRepository auditLogRepository;

    @GetMapping
    public ApiResponse<DashboardResponse> getDashboard() {

        List<Customer> customers = customerRepository.findAll();
        List<FiberDropLine> lines = fiberDropLineRepository.findAll();
        List<AuditLog> recentLogs = auditLogRepository.findTop50ByOrderByCreatedAtDesc();

        long activeCustomers = customers.stream().filter(c -> c.getStatus() == CustomerStatus.ACTIVE).count();
        long pendingCustomers = customers.stream().filter(c -> c.getStatus() == CustomerStatus.PENDING).count();

        long activeLines = lines.stream().filter(l -> l.getStatus() == FiberLineStatus.ACTIVE).count();
        long disconnectedLines = lines.stream().filter(l -> l.getStatus() == FiberLineStatus.DISCONNECTED).count();

        List<CustomerResponse> recentCustomers =
                customers.stream()
                        .sorted(Comparator.comparing(Customer::getCustomerId, Comparator.nullsLast(Long::compareTo)).reversed())
                        .limit(8)
                        .map(this::toCustomerResponse)
                        .toList();

        DashboardResponse resp = DashboardResponse.builder()
                .customers(customers.size())
                .activeCustomers(activeCustomers)
                .pendingCustomers(pendingCustomers)
                .splitters(splitterRepository.count())
                .fdhs(fdhRepository.count())
                .headends(headendRepository.count())
                .coreSwitches(coreSwitchRepository.count())
                .fiberDropLines(lines.size())
                .activeFiberLines(activeLines)
                .disconnectedFiberLines(disconnectedLines)
                .recentCustomers(recentCustomers)
                .recentAuditLogs(recentLogs.stream().limit(8).toList())
                .build();

        return ApiResponse.ok("Dashboard loaded", resp);
    }

    private CustomerResponse toCustomerResponse(Customer c) {
        return CustomerResponse.builder()
                .customerId(c.getCustomerId())
                .name(c.getName())
                .address(c.getAddress())
                .neighborhood(c.getNeighborhood())
                .plan(c.getPlan())
                .connectionType(c.getConnectionType())
                .status(c.getStatus())
                .splitterId(c.getSplitter() != null ? c.getSplitter().getSplitterId() : null)
                .splitterPort(c.getSplitterPort())
                .createdAt(c.getCreatedAt())
                .build();
    }

}
