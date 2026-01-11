package com.company.network_inventory.dto.topology;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
    @Data
    @AllArgsConstructor
    public class TopologyFDHResponse {
        private Long fdhId;
        private String fdhName;
        private List<TopologySplitterNode> splitters;
    }