package com.company.network_inventory.dto;

import lombok.Data;

@Data
public class TechnicianUpdateRequest {
    private String name;
    private String phone;
    private String email;
    private String status;
}
