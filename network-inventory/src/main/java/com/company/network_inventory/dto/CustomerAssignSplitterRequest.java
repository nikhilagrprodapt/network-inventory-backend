package com.company.network_inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerAssignSplitterRequest {

    @NotNull(message = "Splitter ID is required")
    private Long splitterId;

    @NotNull(message = "Splitter port is required")
    @Min(value = 1, message = "Port number must be >= 1")
    private Integer splitterPort;
}
