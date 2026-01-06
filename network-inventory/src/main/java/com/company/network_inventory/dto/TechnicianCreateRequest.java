package com.company.network_inventory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TechnicianCreateRequest {

    @NotBlank
    private String name;

    private String phone;

    private String email;

    @NotBlank
    private String status; // ACTIVE / INACTIVE
}
