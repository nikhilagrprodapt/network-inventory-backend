package com.company.network_inventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerAssignSplitterRequest {

    @NotNull
    private Long splitterId;

    @NotNull
    private Integer splitterPort;
}
