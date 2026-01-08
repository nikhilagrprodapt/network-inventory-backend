package com.company.network_inventory.controller;

import com.company.network_inventory.dto.AssetAssignRequest;
import com.company.network_inventory.dto.AssetCreateRequest;
import com.company.network_inventory.dto.AssetResponse;
import com.company.network_inventory.entity.AssetAssignment;
import com.company.network_inventory.service.AssetService;
import com.company.network_inventory.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @PostMapping
    public ApiResponse<AssetResponse> create(@Valid @RequestBody AssetCreateRequest request) {
        return ApiResponse.ok("Asset created", assetService.createAsset(request));
    }

    @PutMapping("/{id}/assign")
    public ApiResponse<AssetResponse> assign(
            @PathVariable Long id,
            @Valid @RequestBody AssetAssignRequest request
    ) {
        return ApiResponse.ok("Asset assigned", assetService.assignAsset(id, request));
    }

    @GetMapping
    public ApiResponse<List<AssetResponse>> all(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status
    ) {
        return ApiResponse.ok("Assets fetched", assetService.getAssetsByFilter(type, status));
    }

    @PutMapping("/{id}/unassign")
    public ApiResponse<AssetResponse> unassign(@PathVariable Long id) {
        return ApiResponse.ok("Asset unassigned", assetService.unassignAsset(id));
    }

    @GetMapping("/{id}/history")
    public ApiResponse<List<AssetAssignment>> history(@PathVariable Long id) {
        return ApiResponse.ok("Asset history fetched", assetService.getAssetHistory(id));
    }
}
