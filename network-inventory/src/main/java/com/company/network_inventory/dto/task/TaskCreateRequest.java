package com.company.network_inventory.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskCreateRequest {

    @NotNull
    private Long customerId;

    @NotBlank
    private String taskType; // "INSTALLATION"

    private String notes;

    // optional
    private Long technicianId;
}
