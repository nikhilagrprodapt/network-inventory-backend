package com.company.network_inventory.controller;

import com.company.network_inventory.dto.FiberDropLineCreateRequest;
import com.company.network_inventory.dto.FiberDropLineResponse;
import com.company.network_inventory.service.FiberDropLineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fiber-lines")
@RequiredArgsConstructor
public class FiberDropLineController {

    private final FiberDropLineService fiberDropLineService;

    @PostMapping
    public FiberDropLineResponse create(@Valid @RequestBody FiberDropLineCreateRequest request) {
        return fiberDropLineService.create(request);
    }

    @GetMapping
    public List<FiberDropLineResponse> all() {
        return fiberDropLineService.getAll();
    }
}
