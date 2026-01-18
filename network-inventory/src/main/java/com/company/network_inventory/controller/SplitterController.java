package com.company.network_inventory.controller;

import com.company.network_inventory.dto.SplitterCreateRequest;
import com.company.network_inventory.dto.SplitterResponse;
import com.company.network_inventory.service.SplitterService;
import com.company.network_inventory.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/splitters")
@RequiredArgsConstructor
public class SplitterController {

    private final SplitterService splitterService;

    @PostMapping
    public ApiResponse<SplitterResponse> create(@Valid @RequestBody SplitterCreateRequest request) {
        return ApiResponse.ok("Splitter created", splitterService.create(request));
    }

    @GetMapping
    public ApiResponse<List<SplitterResponse>> all() {
        return ApiResponse.ok("Splitters fetched", splitterService.getAll());
    }

    @GetMapping("/{id}/available-ports")
    public ApiResponse<List<Integer>> availablePorts(@PathVariable Long id) {
        return ApiResponse.ok("Available ports fetched", splitterService.getAvailablePorts(id));
    }
}
