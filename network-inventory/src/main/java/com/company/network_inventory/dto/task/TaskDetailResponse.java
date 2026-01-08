package com.company.network_inventory.dto.task;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDetailResponse {
    private TaskResponse task;
    private List<TaskNoteResponse> notes;
    private List<TaskChecklistItemResponse> checklist;
}
