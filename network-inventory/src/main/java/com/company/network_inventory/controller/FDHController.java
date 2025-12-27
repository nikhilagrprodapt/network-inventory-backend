package com.company.network_inventory.controller;

import com.company.network_inventory.dto.FDHCreateRequest;
import com.company.network_inventory.dto.FDHResponse;
import com.company.network_inventory.service.FDHService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fdhs")
@RequiredArgsConstructor
public class FDHController {

    private final FDHService fdhService;

    @PostMapping
    public FDHResponse create(@Valid @RequestBody FDHCreateRequest request) {
        return fdhService.create(request);
    }

    @GetMapping
    public List<FDHResponse> getAll() {
        return fdhService.getAll();
    }
}
