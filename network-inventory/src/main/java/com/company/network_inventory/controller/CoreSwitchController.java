package com.company.network_inventory.controller;

import com.company.network_inventory.dto.CoreSwitchCreateRequest;
import com.company.network_inventory.dto.CoreSwitchResponse;
import com.company.network_inventory.service.CoreSwitchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/core-switches")
@RequiredArgsConstructor
public class CoreSwitchController {

    private final CoreSwitchService coreSwitchService;

    @PostMapping
    public CoreSwitchResponse create(@Valid @RequestBody CoreSwitchCreateRequest request) {
        return coreSwitchService.create(request);
    }

    @GetMapping
    public List<CoreSwitchResponse> all() {
        return coreSwitchService.getAll();
    }
}
