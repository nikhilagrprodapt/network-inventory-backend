package com.company.network_inventory.support.controller;

import com.company.network_inventory.support.repository.SupportCaseRepository;
import com.company.network_inventory.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/support/cases")
@RequiredArgsConstructor
public class SupportCaseController {

    private final SupportCaseRepository supportCaseRepository;

    @GetMapping
    public ApiResponse list(@RequestParam(required = false) Long customerId) {
        if (customerId != null) {
            return ApiResponse.ok(
                    "Support cases fetched",
                    supportCaseRepository.findByCustomer_CustomerIdOrderByCreatedAtDesc(customerId)
            );
        }
        return ApiResponse.ok("Support cases fetched", supportCaseRepository.findAll());
    }
}
