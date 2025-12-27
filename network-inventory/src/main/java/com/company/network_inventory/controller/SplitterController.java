package com.company.network_inventory.controller;

import com.company.network_inventory.dto.SplitterCreateRequest;
import com.company.network_inventory.dto.SplitterResponse;
import com.company.network_inventory.service.SplitterService;
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
    public SplitterResponse create(@Valid @RequestBody SplitterCreateRequest request) {
        return splitterService.create(request);
    }

    @GetMapping
    public List<SplitterResponse> getAll() {
        return splitterService.getAll();
    }
}
