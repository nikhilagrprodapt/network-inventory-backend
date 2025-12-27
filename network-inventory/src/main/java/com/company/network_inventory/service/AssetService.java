package com.company.network_inventory.service;

import com.company.network_inventory.dto.AssetAssignRequest;
import com.company.network_inventory.dto.AssetCreateRequest;
import com.company.network_inventory.dto.AssetResponse;

import java.util.List;

public interface AssetService {

    AssetResponse createAsset(AssetCreateRequest request);
    AssetResponse assignAsset(Long assetId, AssetAssignRequest request);
    List<AssetResponse> getAllAssets();
}

