package com.company.network_inventory.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TechnicianResponse {
    private Long technicianId;
    private String name;
    private String phone;
    private String email;
    private String status;
}
