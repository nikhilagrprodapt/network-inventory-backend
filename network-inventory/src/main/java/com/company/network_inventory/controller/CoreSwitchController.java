package com.company.network_inventory.controller;

import com.company.network_inventory.entity.CoreSwitch;
import com.company.network_inventory.service.CoreSwitchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/core-switches")
@RequiredArgsConstructor
public class CoreSwitchController {

    private final CoreSwitchService coreSwitchService;

    @PostMapping
    public CoreSwitch create(@RequestBody CoreSwitch request) {
        return coreSwitchService.create(request);
    }

    @GetMapping
    public List<CoreSwitch> all() {
        return coreSwitchService.getAll();
    }
}
