package com.company.network_inventory.dto;

import com.company.network_inventory.entity.enums.CustomerStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    private String neighborhood;
    private String plan;

    @NotNull
    private CustomerStatus status;

    // optional at onboarding time
    private Long splitterId;
    private Integer splitterPort;
}

