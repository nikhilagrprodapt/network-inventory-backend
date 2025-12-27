package com.company.network_inventory.dto.task;

import com.company.network_inventory.entity.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskStatusUpdateRequest {

    @NotNull
    private TaskStatus status;

    private String notes;
}
