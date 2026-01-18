package com.company.network_inventory.service;

import com.company.network_inventory.dto.AssetAssignRequest;
import com.company.network_inventory.dto.AssetBulkUploadResult;
import com.company.network_inventory.dto.AssetCreateRequest;
import com.company.network_inventory.dto.AssetResponse;
import com.company.network_inventory.dto.AssetStatusUpdateRequest;
import com.company.network_inventory.entity.AssetAssignment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssetService {

    AssetResponse createAsset(AssetCreateRequest request);

    AssetResponse assignAsset(Long assetId, AssetAssignRequest request);

    List<AssetResponse> getAllAssets();

    List<AssetResponse> getAssetsByFilter(String type, String status);

    AssetResponse unassignAsset(Long assetId);

    List<AssetAssignment> getAssetHistory(Long assetId);

    AssetResponse updateStatus(Long assetId, AssetStatusUpdateRequest request);

    AssetBulkUploadResult bulkUploadCsv(MultipartFile file);
}
