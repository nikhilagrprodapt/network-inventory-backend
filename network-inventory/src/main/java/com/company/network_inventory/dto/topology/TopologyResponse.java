package com.company.network_inventory.dto.topology;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TopologyResponse {
    private Long headendId;
    private String headendName;
    private String headendLocation;

    private List<TopologyFDHNode> fdhs;
}
