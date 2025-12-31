package com.company.network_inventory.controller;

import com.company.network_inventory.entity.FiberDropLine;
import com.company.network_inventory.service.FiberDropLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fiber-lines")
@RequiredArgsConstructor
public class FiberDropLineController {

    private final FiberDropLineService fiberDropLineService;

    @PostMapping
    public FiberDropLine create(@RequestBody FiberDropLine request) {
        return fiberDropLineService.create(request);
    }

    @GetMapping
    public List<FiberDropLine> all() {
        return fiberDropLineService.getAll();
    }
}
