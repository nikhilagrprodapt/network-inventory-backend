package com.company.network_inventory.controller;

import com.company.network_inventory.dto.onboarding.OnboardingConfirmRequest;
import com.company.network_inventory.dto.onboarding.OnboardingConfirmResponse;
import com.company.network_inventory.entity.FDH;
import com.company.network_inventory.entity.Splitter;
import com.company.network_inventory.service.OnboardingService;
import com.company.network_inventory.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

    private final OnboardingService onboardingService;

    @GetMapping("/fdhs")
    public ApiResponse<List<FDH>> fdhs() {
        return ApiResponse.ok("FDHs fetched", onboardingService.getAllFdhs());
    }

    @GetMapping("/splitters")
    public ApiResponse<List<Splitter>> splittersByFdh(@RequestParam Long fdhId) {
        return ApiResponse.ok("Splitters fetched", onboardingService.getSplittersByFdh(fdhId));
    }

    @GetMapping("/splitters/{splitterId}/free-ports")
    public ApiResponse<List<Integer>> freePorts(@PathVariable Long splitterId) {
        return ApiResponse.ok("Free ports fetched", onboardingService.getFreePorts(splitterId));
    }

    @PostMapping("/confirm")
    public ApiResponse<OnboardingConfirmResponse> confirm(@Valid @RequestBody OnboardingConfirmRequest request) {
        return ApiResponse.ok("Onboarding confirmed", onboardingService.confirm(request));
    }
}
