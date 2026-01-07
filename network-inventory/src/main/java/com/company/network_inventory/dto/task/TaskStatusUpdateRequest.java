package com.company.network_inventory.dto.task;

import com.company.network_inventory.entity.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskStatusUpdateRequest {

    @NotNull(message = "status is required")
    private TaskStatus status;
}
