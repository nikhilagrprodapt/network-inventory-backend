package com.company.network_inventory.dto.task;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskNoteResponse {
    private Long noteId;
    private String text;
    private String author;
    private LocalDateTime createdAt;
}
