package com.company.network_inventory.controller;

import com.company.network_inventory.dto.*;
import com.company.network_inventory.service.TechnicianService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technicians")
@RequiredArgsConstructor
public class TechnicianController {

    private final TechnicianService technicianService;

    @PostMapping
    public TechnicianResponse create(@Valid @RequestBody TechnicianCreateRequest request) {
        return technicianService.create(request);
    }

    @GetMapping
    public List<TechnicianResponse> getAll() {
        return technicianService.getAll();
    }

    @GetMapping("/{id}")
    public TechnicianResponse getOne(@PathVariable Long id) {
        return technicianService.getOne(id);
    }

    @PatchMapping("/{id}")
    public TechnicianResponse update(@PathVariable Long id,
                                     @Valid @RequestBody TechnicianUpdateRequest request) {
        return technicianService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public TechnicianResponse updateStatus(@PathVariable Long id,
                                           @Valid @RequestBody TechnicianStatusRequest request) {
        return technicianService.updateStatus(id, request);
    }
}
