package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.AssetAssignRequest;
import com.company.network_inventory.dto.AssetCreateRequest;
import com.company.network_inventory.dto.AssetResponse;
import com.company.network_inventory.entity.Asset;
import com.company.network_inventory.entity.AssetAssignment;
import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.enums.AssetStatus;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.AssetAssignmentRepository;
import com.company.network_inventory.repository.AssetRepository;
import com.company.network_inventory.repository.CustomerRepository;
import com.company.network_inventory.service.AssetService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.company.network_inventory.audit.service.AuditService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final CustomerRepository customerRepository;
    private final AuditService auditService;
    private final AssetAssignmentRepository assetAssignmentRepository;


    @Override
    public AssetResponse createAsset(AssetCreateRequest request) {

        Asset asset = Asset.builder()
                .type(request.getType())
                .model(request.getModel())
                .serialNumber(request.getSerialNumber())
                .status(request.getStatus())
                .build();

        Asset saved = assetRepository.save(asset);

        auditService.log(
                "CREATE",
                "ASSET",
                saved.getAssetId(),
                "Created asset type=" + saved.getType() +
                        ", serial=" + saved.getSerialNumber()
        );
        return toResponse(saved);
    }

    @Transactional
    @Override
    public AssetResponse assignAsset(Long assetId, AssetAssignRequest request) {

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        assetAssignmentRepository.findFirstByAsset_AssetIdAndUnassignedAtIsNullOrderByAssignedAtDesc(assetId)
                .ifPresent(active -> {
                    active.setUnassignedAt(LocalDateTime.now());
                    assetAssignmentRepository.save(active);
                });

        AssetAssignment assignment = AssetAssignment.builder()
                .asset(asset)
                .customer(customer)
                .build();

        assetAssignmentRepository.save(assignment);

        asset.setAssignedToCustomer(customer);
        asset.setAssignedAt(LocalDateTime.now());
        asset.setStatus(AssetStatus.ASSIGNED);

        Asset saved = assetRepository.save(asset);

        auditService.log(
                "ASSIGN",
                "ASSET",
                saved.getAssetId(),
                "Assigned asset to customerId=" + customer.getCustomerId()
        );
        return toResponse(saved);
    }

    @Override
    public List<AssetResponse> getAllAssets() {
        return assetRepository.findAll().stream().map(this::toResponse).toList();
    }


    @Override
    public List<AssetResponse> getAssetsByFilter(String type, String status) {

        com.company.network_inventory.entity.enums.AssetType t = null;
        com.company.network_inventory.entity.enums.AssetStatus s = null;

        try {
            if (type != null && !type.isBlank()) {
                t = com.company.network_inventory.entity.enums.AssetType
                        .valueOf(type.trim().toUpperCase(Locale.ROOT));
            }
        } catch (Exception ignored) {
            t = null;
        }

        try {
            if (status != null && !status.isBlank()) {
                s = com.company.network_inventory.entity.enums.AssetStatus
                        .valueOf(status.trim().toUpperCase(Locale.ROOT));
            }
        } catch (Exception ignored) {
            s = null;
        }

        if (t != null && s != null) {
            return assetRepository.findByTypeAndStatus(t, s).stream().map(this::toResponse).toList();
        }
        if (t != null) {
            return assetRepository.findByType(t).stream().map(this::toResponse).toList();
        }
        if (s != null) {
            return assetRepository.findByStatus(s).stream().map(this::toResponse).toList();
        }

        return getAllAssets();
    }


    @Transactional
    @Override
    public AssetResponse unassignAsset(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));

        AssetAssignment active = assetAssignmentRepository
                .findFirstByAsset_AssetIdAndUnassignedAtIsNullOrderByAssignedAtDesc(assetId)
                .orElseThrow(() -> new IllegalArgumentException("Asset is not currently assigned"));

        active.setUnassignedAt(LocalDateTime.now());
        assetAssignmentRepository.save(active);

        asset.setAssignedToCustomer(null);
        asset.setAssignedAt(null);
        asset.setStatus(AssetStatus.AVAILABLE);

        Asset saved = assetRepository.save(asset);

        auditService.log(
                "UNASSIGN",
                "ASSET",
                saved.getAssetId(),
                "Unassigned asset (reclaimed)"
        );

        return toResponse(saved);
    }
    @Override
    public List<AssetAssignment> getAssetHistory(Long assetId) {
        // validate asset exists
        assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));
        return assetAssignmentRepository.findByAsset_AssetIdOrderByAssignedAtDesc(assetId);
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