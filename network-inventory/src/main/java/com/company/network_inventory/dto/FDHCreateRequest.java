package com.company.network_inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FDHCreateRequest {
    @NotBlank
    private String name;
    private String location;
    private String region;
    private Integer maxPorts;

    @NotNull
    private Long headendId;
}
