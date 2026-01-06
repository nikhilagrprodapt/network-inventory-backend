package com.company.network_inventory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TechnicianStatusRequest {
    @NotBlank
    private String status; // ACTIVE / INACTIVE
}
