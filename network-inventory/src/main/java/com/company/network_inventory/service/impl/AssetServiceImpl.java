package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.AssetAssignRequest;
import com.company.network_inventory.dto.AssetCreateRequest;
import com.company.network_inventory.dto.AssetResponse;
import com.company.network_inventory.entity.Asset;
import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.enums.AssetStatus;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.AssetRepository;
import com.company.network_inventory.repository.CustomerRepository;
import com.company.network_inventory.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final CustomerRepository customerRepository;

    @Override
    public AssetResponse createAsset(AssetCreateRequest request) {

        Asset asset = Asset.builder()
                .type(request.getType())
                .model(request.getModel())
                .serialNumber(request.getSerialNumber())
                .status(request.getStatus())
                .build();

        return toResponse(assetRepository.save(asset));
    }

    @Override
    public AssetResponse assignAsset(Long assetId, AssetAssignRequest request) {

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        asset.setAssignedToCustomer(customer);
        asset.setAssignedAt(LocalDateTime.now());
        asset.setStatus(AssetStatus.ASSIGNED);

        return toResponse(assetRepository.save(asset));
    }

    @Override
    public List<AssetResponse> getAllAssets() {
        return assetRepository.findAll().stream().map(this::toResponse).toList();
    }

    private AssetResponse toResponse(Asset asset) {
        return AssetResponse.builder()
                .assetId(asset.getAssetId())
                .type(asset.getType())
                .model(asset.getModel())
                .serialNumber(asset.getSerialNumber())
                .status(asset.getStatus())
                .assignedCustomerId(
                        asset.getAssignedToCustomer() != null ?
                                asset.getAssignedToCustomer().getCustomerId() : null
                )
                .assignedAt(asset.getAssignedAt())
                .build();
    }
}
