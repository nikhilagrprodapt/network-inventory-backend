package com.company.network_inventory.service;

import com.company.network_inventory.dto.AssetAssignRequest;
import com.company.network_inventory.dto.AssetCreateRequest;
import com.company.network_inventory.dto.AssetResponse;
import com.company.network_inventory.entity.AssetAssignment;

import java.util.List;

public interface AssetService {

    AssetResponse createAsset(AssetCreateRequest request);

    AssetResponse assignAsset(Long assetId, AssetAssignRequest request);

    List<AssetResponse> getAllAssets();

    // ✅ add these (required by controller + impl)
    List<AssetResponse> getAssetsByFilter(String type, String status);

    AssetResponse unassignAsset(Long assetId);

    List<AssetAssignment> getAssetHistory(Long assetId);
}
