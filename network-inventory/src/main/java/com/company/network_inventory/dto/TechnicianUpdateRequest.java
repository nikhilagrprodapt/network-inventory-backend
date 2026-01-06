package com.company.network_inventory.dto;

import lombok.Data;

// PATCH style: all fields optional
@Data
public class TechnicianUpdateRequest {
    private String name;
    private String phone;
    private String email;
    private String status; // optional
}
