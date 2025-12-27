package com.company.network_inventory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HeadendCreateRequest {
    @NotBlank
    private String name;
    private String location;
    private Integer bandwidthCapacityMbps;
}

