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
    private List<TopologyCustomerNode> customers;
}
