package com.company.network_inventory.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskChecklistCreateRequest {
    @NotBlank(message = "label is required")
    private String label;
}
