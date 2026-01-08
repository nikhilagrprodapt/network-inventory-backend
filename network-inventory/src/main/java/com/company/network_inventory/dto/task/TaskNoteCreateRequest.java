package com.company.network_inventory.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskNoteCreateRequest {
    @NotBlank(message = "text is required")
    private String text;

    private String author;
}
