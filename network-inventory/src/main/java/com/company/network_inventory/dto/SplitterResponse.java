package com.company.network_inventory.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SplitterResponse {
    private Long splitterId;
    private String name;
    private String model;
    private Integer portCapacity;
    private Long fdhId;
}
