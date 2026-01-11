package com.company.network_inventory.dto;

import com.company.network_inventory.entity.enums.AssetStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssetStatusUpdateRequest {

    @NotNull
    private AssetStatus status;
}
