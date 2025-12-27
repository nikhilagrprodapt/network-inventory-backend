package com.company.network_inventory.dto.task;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TechnicianResponse {
    private Long technicianId;
    private String name;
    private String contact;
    private String region;
}
