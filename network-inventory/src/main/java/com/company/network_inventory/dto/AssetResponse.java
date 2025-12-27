package com.company.network_inventory.dto;

import com.company.network_inventory.entity.enums.AssetStatus;
import com.company.network_inventory.entity.enums.AssetType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AssetResponse {

    private Long assetId;
    private AssetType type;
    private String model;
    private String serialNumber;
    private AssetStatus status;

    private Long assignedCustomerId;
    private LocalDateTime assignedAt;
}
