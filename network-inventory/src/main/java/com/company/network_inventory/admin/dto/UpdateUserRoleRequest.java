package com.company.network_inventory.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRoleRequest {
    @NotBlank
    private String role; // e.g. "TECHNICIAN"
}
