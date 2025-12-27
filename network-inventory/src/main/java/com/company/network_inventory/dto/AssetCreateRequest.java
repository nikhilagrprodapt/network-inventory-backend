package com.company.network_inventory.dto;


import com.company.network_inventory.entity.enums.AssetStatus;
import com.company.network_inventory.entity.enums.AssetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssetCreateRequest {

    @NotNull
    private AssetType type;

    private String model;

    @NotBlank
    private String serialNumber;

    @NotNull
    private AssetStatus status;
}
