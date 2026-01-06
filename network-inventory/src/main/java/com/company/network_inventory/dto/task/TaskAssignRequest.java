package com.company.network_inventory.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskAssignRequest {
    @NotNull
    private Long technicianId;
}
