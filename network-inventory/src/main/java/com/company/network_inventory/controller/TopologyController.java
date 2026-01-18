package com.company.network_inventory.controller;

import com.company.network_inventory.audit.service.AuditService;
import com.company.network_inventory.dto.topology.TopologyCustomerDetailsResponse;
import com.company.network_inventory.dto.topology.TopologyResponse;
import com.company.network_inventory.entity.Headend;
import com.company.network_inventory.service.TopologyService;
import com.company.network_inventory.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topology")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','PLANNER','MANAGER')")
public class TopologyController {

    private final TopologyService topologyService;
    private final AuditService auditService;

    @GetMapping("/headends")
    public ApiResponse<List<Headend>> headends() {
        return ApiResponse.ok("Headends fetched", topologyService.listHeadends());
    }

    @GetMapping
    public ApiResponse<TopologyResponse> topology(@RequestParam Long headendId) {
        auditService.log("TOPOLOGY_VIEW", "HEADEND", headendId, "Viewed topology");
        return ApiResponse.ok("Topology fetched", topologyService.getTopology(headendId));
    }

    @GetMapping("/{headendId}")
    public ApiResponse<TopologyResponse> topologyByPath(@PathVariable Long headendId) {
        auditService.log("TOPOLOGY_VIEW", "HEADEND", headendId, "Viewed topology");
        return ApiResponse.ok("Topology fetched", topologyService.getTopology(headendId));
    }

    @GetMapping("/customer/{customerId}")
    public ApiResponse<TopologyCustomerDetailsResponse> customerNode(@PathVariable Long customerId) {
        auditService.log("NODE_VIEW", "CUSTOMER", customerId, "Viewed customer node details");
        return ApiResponse.ok("Customer node fetched", topologyService.getCustomerDetails(customerId));
    }
}
