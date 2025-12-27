package com.company.network_inventory.controller;

import com.company.network_inventory.dto.topology.TopologyResponse;
import com.company.network_inventory.service.TopologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topology")
@RequiredArgsConstructor
public class TopologyController {

    private final TopologyService topologyService;

    // Example: /api/topology?headendId=1
    @GetMapping
    public TopologyResponse getTopology(@RequestParam Long headendId) {
        return topologyService.getTopology(headendId);
    }
}
