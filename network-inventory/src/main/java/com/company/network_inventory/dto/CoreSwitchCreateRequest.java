package com.company.network_inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CoreSwitchCreateRequest {

    @NotBlank
    private String name;

    private String location;

    @NotNull
    private Long headendId;
}
