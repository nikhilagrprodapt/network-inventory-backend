package com.company.network_inventory.dto;

import com.company.network_inventory.entity.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class TaskUpdateRequest {
    private String title;
    private String description;
    private TaskStatus status;
    private Long technicianId;
    private Long customerId;
    private LocalDateTime dueAt;
}
