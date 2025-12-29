package com.company.network_inventory.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskCreateRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private Long technicianId;

    @NotNull(message = "Task type is required")
    private String taskType;

    @NotBlank(message = "Notes cannot be empty")
    private String notes;
}
