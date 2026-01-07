package com.company.network_inventory.dto.topology;

import lombok.*;

import java.util.List;
import java.util.Map;

public class TopologyDtos {

    public enum NodeType {
        HEADEND, FDH, SPLITTER, FIBER_DROP_LINE, CUSTOMER
    }

    public enum EdgeType {
        CONTAINS, LINKED
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class TopologyNodeDto {
        private String id;          // e.g. "headend-1"
        private NodeType type;      // HEADEND/FDH/...
        private String label;       // display
        private Long refId;         // DB id
        private Map<String, Object> meta; // optional extra info
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class TopologyEdgeDto {
        private String id;          // unique edge id
        private String source;      // node id
        private String target;      // node id
        private EdgeType type;      // CONTAINS
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class TopologyGraphDto {
        private List<TopologyNodeDto> nodes;
        private List<TopologyEdgeDto> edges;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class IdNameDto {
        private Long id;
        private String name;
    }
}
