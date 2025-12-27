package com.company.network_inventory.service;

import com.company.network_inventory.dto.topology.TopologyResponse;

public interface TopologyService {
    TopologyResponse getTopology(Long headendId);
}
