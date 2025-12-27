package com.company.network_inventory.dto.task;

import com.company.network_inventory.entity.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskResponse {
    private Long taskId;

    private Long customerId;
    private String customerName;

    private Long technicianId;
    private String technicianName;

    private String taskType;
    private TaskStatus status;

    private LocalDateTime scheduledAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    private String notes;
}
