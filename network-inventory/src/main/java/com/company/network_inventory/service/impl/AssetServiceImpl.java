package com.company.network_inventory.service.impl;

import com.company.network_inventory.audit.service.AuditService;
import com.company.network_inventory.dto.AssetAssignRequest;
import com.company.network_inventory.dto.AssetBulkUploadResult;
import com.company.network_inventory.dto.AssetCreateRequest;
import com.company.network_inventory.dto.AssetResponse;
import com.company.network_inventory.dto.AssetStatusUpdateRequest;
import com.company.network_inventory.entity.Asset;
import com.company.network_inventory.entity.AssetAssignment;
import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.enums.AssetStatus;
import com.company.network_inventory.entity.enums.AssetType;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.AssetAssignmentRepository;
import com.company.network_inventory.repository.AssetRepository;
import com.company.network_inventory.repository.CustomerRepository;
import com.company.network_inventory.service.AssetService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

        AssetType t = null;
        AssetStatus s = null;

        try {
            if (type != null && !type.isBlank()) {
                t = AssetType.valueOf(type.trim().toUpperCase(Locale.ROOT));
            }
        } catch (Exception ignored) {
            t = null;
        }

        try {
            if (status != null && !status.isBlank()) {
                s = AssetStatus.valueOf(status.trim().toUpperCase(Locale.ROOT));
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
        assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));
        return assetAssignmentRepository.findByAsset_AssetIdOrderByAssignedAtDesc(assetId);
    }

    @Transactional
    @Override
    public AssetResponse updateStatus(Long assetId, AssetStatusUpdateRequest request) {

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));

        boolean hasActiveAssignment = assetAssignmentRepository
                .findFirstByAsset_AssetIdAndUnassignedAtIsNullOrderByAssignedAtDesc(assetId)
                .isPresent();

        if (hasActiveAssignment || asset.getAssignedToCustomer() != null || asset.getStatus() == AssetStatus.ASSIGNED) {
            throw new IllegalArgumentException("Unassign first");
        }

        AssetStatus old = asset.getStatus();
        asset.setStatus(request.getStatus());

        Asset saved = assetRepository.save(asset);

        auditService.log(
                "STATUS_UPDATE",
                "ASSET",
                saved.getAssetId(),
                "Status changed: " + old + " -> " + saved.getStatus()
        );

        return toResponse(saved);
    }

    @Transactional
    @Override
    public AssetBulkUploadResult bulkUploadCsv(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("CSV file is required");
        }

        List<AssetBulkUploadResult.Failure> failures = new ArrayList<>();
        int created = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
        )) {
            String line;
            int row = 0;

            while ((line = br.readLine()) != null) {
                row++;

                String raw = line;
                line = line.trim();

                if (line.isBlank()) continue;

                // Allow header row
                if (row == 1 && line.toLowerCase(Locale.ROOT).contains("serial")) {
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 4) {
                    failures.add(AssetBulkUploadResult.Failure.builder()
                            .rowNumber(row)
                            .serialNumber(null)
                            .reason("Invalid CSV format. Expected 4 columns: type,serialNumber,model,status")
                            .rawLine(raw)
                            .build());
                    continue;
                }

                String typeStr = parts[0].trim();
                String serial = parts[1].trim();
                String model = parts[2].trim();
                String statusStr = parts[3].trim();

                if (typeStr.isBlank() || serial.isBlank() || statusStr.isBlank()) {
                    failures.add(AssetBulkUploadResult.Failure.builder()
                            .rowNumber(row)
                            .serialNumber(serial.isBlank() ? null : serial)
                            .reason("type, serialNumber, and status are required")
                            .rawLine(raw)
                            .build());
                    continue;
                }

                AssetType type;
                AssetStatus status;

                try {
                    type = AssetType.valueOf(typeStr.toUpperCase(Locale.ROOT));
                } catch (Exception ex) {
                    failures.add(AssetBulkUploadResult.Failure.builder()
                            .rowNumber(row)
                            .serialNumber(serial)
                            .reason("Invalid type: " + typeStr)
                            .rawLine(raw)
                            .build());
                    continue;
                }

                try {
                    status = AssetStatus.valueOf(statusStr.toUpperCase(Locale.ROOT));
                } catch (Exception ex) {
                    failures.add(AssetBulkUploadResult.Failure.builder()
                            .rowNumber(row)
                            .serialNumber(serial)
                            .reason("Invalid status: " + statusStr)
                            .rawLine(raw)
                            .build());
                    continue;
                }

                // Prevent duplicates (serial unique)
                if (assetRepository.existsBySerialNumber(serial)) {
                    failures.add(AssetBulkUploadResult.Failure.builder()
                            .rowNumber(row)
                            .serialNumber(serial)
                            .reason("Duplicate serialNumber (already exists)")
                            .rawLine(raw)
                            .build());
                    continue;
                }

                Asset asset = Asset.builder()
                        .type(type)
                        .serialNumber(serial)
                        .model(model.isBlank() ? null : model)
                        .status(status)
                        .build();

                Asset saved = assetRepository.save(asset);
                created++;

                auditService.log(
                        "CREATE",
                        "ASSET",
                        saved.getAssetId(),
                        "Bulk upload created asset type=" + saved.getType() + ", serial=" + saved.getSerialNumber()
                );
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to process CSV: " + e.getMessage());
        }

        return AssetBulkUploadResult.builder()
                .createdCount(created)
                .failedCount(failures.size())
                .failures(failures)
                .build();
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
