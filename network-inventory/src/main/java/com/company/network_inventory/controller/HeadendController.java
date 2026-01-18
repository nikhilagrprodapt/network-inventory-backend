package com.company.network_inventory.controller;

import com.company.network_inventory.dto.HeadendCreateRequest;
import com.company.network_inventory.dto.HeadendResponse;
import com.company.network_inventory.service.HeadendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/headends")
@RequiredArgsConstructor
public class HeadendController {

    private final HeadendService headendService;

    @PostMapping
    public HeadendResponse create(@Valid @RequestBody HeadendCreateRequest request) {
        return headendService.create(request);
    }

    @GetMapping
    public List<HeadendResponse> getAll() {
        return headendService.getAll();
    }

    @GetMapping("/{id}")
    public HeadendResponse getOne(@PathVariable Long id) {
        return headendService.getOne(id);
    }

    @PatchMapping("/{id}")
    public HeadendResponse update(@PathVariable Long id,
                                  @Valid @RequestBody HeadendCreateRequest request) {
        return headendService.update(id, request);
    }
}
