package com.company.network_inventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssetAssignRequest {

    @NotNull
    private Long customerId;
}
