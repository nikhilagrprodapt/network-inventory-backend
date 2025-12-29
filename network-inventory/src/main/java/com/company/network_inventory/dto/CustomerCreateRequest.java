package com.company.network_inventory.dto;

import com.company.network_inventory.entity.enums.CustomerStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.company.network_inventory.entity.enums.ConnectionType;


@Data
public class CustomerCreateRequest {

    @NotBlank(message = "Customer name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Neighborhood is required")
    private String neighborhood;

    @NotBlank(message = "Plan is required")
    private String plan;

    @NotNull(message = "Status is required")
    private CustomerStatus status;
    // optional at onboarding time
    private Long splitterId;
    private Integer splitterPort;

    @NotNull(message = "Connection type is required")
    private ConnectionType connectionType;
}

