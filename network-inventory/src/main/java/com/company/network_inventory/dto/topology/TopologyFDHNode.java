package com.company.network_inventory.dto.topology;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TopologyFDHNode {
    private Long fdhId;
    private String name;
    private String region;
    private String location;
    private List<TopologySplitterNode> splitters;
}
