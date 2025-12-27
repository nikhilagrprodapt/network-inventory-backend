package com.company.network_inventory.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FDHResponse {
    private Long fdhId;
    private String name;
    private String location;
    private String region;
    private Integer maxPorts;
    private Long headendId;
}
