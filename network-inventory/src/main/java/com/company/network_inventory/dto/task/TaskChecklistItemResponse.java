package com.company.network_inventory.dto.task;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskChecklistItemResponse {
    private Long itemId;
    private String label;
    private boolean done;
}
