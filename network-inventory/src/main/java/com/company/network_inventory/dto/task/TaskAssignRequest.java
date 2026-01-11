package com.company.network_inventory.dto.task;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskAssignRequest {

    // ✅ allow null (so unassign works)
    private Long technicianId;
}
