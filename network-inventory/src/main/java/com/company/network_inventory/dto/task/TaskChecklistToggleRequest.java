package com.company.network_inventory.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskChecklistToggleRequest {
    @NotNull(message = "done is required")
    private Boolean done;
}
