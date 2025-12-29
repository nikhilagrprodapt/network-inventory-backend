package com.company.network_inventory.controller;

import com.company.network_inventory.entity.CoreSwitch;
import com.company.network_inventory.entity.Headend;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.CoreSwitchRepository;
import com.company.network_inventory.repository.HeadendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/core-switches")
@RequiredArgsConstructor
public class CoreSwitchController {

    private final CoreSwitchRepository coreSwitchRepository;
    private final HeadendRepository headendRepository;

    @PostMapping
    public CoreSwitch create(@RequestBody CoreSwitch request) {
        if (request.getHeadend() != null && request.getHeadend().getHeadendId() != null) {
            Headend headend = headendRepository.findById(request.getHeadend().getHeadendId())
                    .orElseThrow(() -> new ResourceNotFoundException("Headend not found"));
            request.setHeadend(headend);
        }
        return coreSwitchRepository.save(request);
    }

    @GetMapping
    public List<CoreSwitch> all() {
        return coreSwitchRepository.findAll();
    }
}
