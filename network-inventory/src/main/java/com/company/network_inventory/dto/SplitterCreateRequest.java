package com.company.network_inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SplitterCreateRequest {

    @NotBlank
    private String name;

    private String model;

    @NotNull
    private Integer portCapacity;

    @NotNull
    private Long fdhId;
}
