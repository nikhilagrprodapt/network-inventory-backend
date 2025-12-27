package com.company.network_inventory.controller;

import com.company.network_inventory.dto.task.TechnicianCreateRequest;
import com.company.network_inventory.dto.task.TechnicianResponse;
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
}
