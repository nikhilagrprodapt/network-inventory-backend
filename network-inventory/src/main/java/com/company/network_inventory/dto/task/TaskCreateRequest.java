package com.company.network_inventory.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCreateRequest {

    @NotNull(message = "customerId is required")
    private Long customerId;

    // optional
    private Long technicianId;

    @NotBlank(message = "taskType is required")
    private String taskType;

    // optional
    private String notes;
}
