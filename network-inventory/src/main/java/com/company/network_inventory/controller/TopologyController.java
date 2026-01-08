package com.company.network_inventory.controller;

import com.company.network_inventory.dto.topology.TopologyResponse;
import com.company.network_inventory.entity.Headend;
import com.company.network_inventory.service.TopologyService;
import com.company.network_inventory.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topology")
@RequiredArgsConstructor
public class TopologyController {

    private final TopologyService topologyService;

    @GetMapping("/headends")
    public ApiResponse<List<Headend>> headends() {
        return ApiResponse.ok("Headends fetched", topologyService.listHeadends());
    }

    // ✅ requires headendId because service requires it
    @GetMapping
    public ApiResponse<TopologyResponse> topology(@RequestParam Long headendId) {
        return ApiResponse.ok("Topology fetched", topologyService.getTopology(headendId));
    }
}
