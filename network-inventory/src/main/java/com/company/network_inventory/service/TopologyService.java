package com.company.network_inventory.service;

import com.company.network_inventory.dto.topology.TopologyResponse;
import com.company.network_inventory.entity.Headend;

import java.util.List;

public interface TopologyService {
    TopologyResponse getTopology(Long headendId);

    // for dropdown
    List<Headend> listHeadends();
}
