package com.company.network_inventory.controller;

import com.company.network_inventory.repository.AssetAssignmentRepository;
import com.company.network_inventory.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assets/assignments")
@RequiredArgsConstructor
public class AssetAssignmentController {

    private final AssetAssignmentRepository assetAssignmentRepository;

    @GetMapping
    public ApiResponse list(@RequestParam(required = false) Long customerId) {
        if (customerId != null) {
            return ApiResponse.ok(
                    "Asset assignments fetched",
                    assetAssignmentRepository.findByCustomer_CustomerIdOrderByAssignedAtDesc(customerId)
            );
        }
        return ApiResponse.ok("Asset assignments fetched", assetAssignmentRepository.findAll());
    }
}
