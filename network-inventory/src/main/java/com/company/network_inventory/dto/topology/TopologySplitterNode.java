package com.company.network_inventory.dto.topology;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TopologySplitterNode {
    private Long splitterId;
    private String name;
    private String model;
    private Integer portCapacity;

    // Existing (kept for backward compatibility)
    private List<TopologyCustomerNode> customers;

    // NEW: Splitter -> FiberDropLine -> Customer
    private List<TopologyFiberDropLineNode> fiberDropLines;
}
