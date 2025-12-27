package com.company.network_inventory.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TechnicianCreateRequest {
    @NotBlank
    private String name;

    private String contact;
    private String region;
}
