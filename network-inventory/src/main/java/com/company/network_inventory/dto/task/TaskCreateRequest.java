package com.company.network_inventory.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskCreateRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private Long technicianId;

    @NotBlank(message = "Task type is required")
    private String taskType;

    private String notes;
}
