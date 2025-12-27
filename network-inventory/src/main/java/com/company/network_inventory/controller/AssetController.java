package com.company.network_inventory.controller;

import com.company.network_inventory.dto.AssetAssignRequest;
import com.company.network_inventory.dto.AssetCreateRequest;
import com.company.network_inventory.dto.AssetResponse;
import com.company.network_inventory.service.AssetService;
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
    public AssetResponse create(@Valid @RequestBody AssetCreateRequest request) {
        return assetService.createAsset(request);
    }

    @PutMapping("/{id}/assign")
    public AssetResponse assign(
            @PathVariable Long id,
            @Valid @RequestBody AssetAssignRequest request
    ) {
        return assetService.assignAsset(id, request);
    }

    @GetMapping
    public List<AssetResponse> getAll() {
        return assetService.getAllAssets();
    }
}

