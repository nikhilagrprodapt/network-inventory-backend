package com.company.network_inventory.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskAssignRequest {

    @NotNull(message = "technicianId is required")
    private Long technicianId;
}
