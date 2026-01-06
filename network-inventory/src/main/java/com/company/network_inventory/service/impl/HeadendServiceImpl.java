package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.HeadendCreateRequest;
import com.company.network_inventory.dto.HeadendResponse;
import com.company.network_inventory.entity.Headend;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.HeadendRepository;
import com.company.network_inventory.service.HeadendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeadendServiceImpl implements HeadendService {

    private final HeadendRepository headendRepository;

    @Override
    public HeadendResponse create(HeadendCreateRequest request) {
        Headend h = Headend.builder()
                .name(request.getName())
                .location(request.getLocation())
                .bandwidthCapacityMbps(request.getBandwidthCapacityMbps())
                .build();

        Headend saved = headendRepository.save(h);
        return toResponse(saved);
    }

    @Override
    public List<HeadendResponse> getAll() {
        return headendRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public HeadendResponse getOne(Long id) {
        Headend h = headendRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Headend not found: " + id));
        return toResponse(h);
    }

    @Override
    public HeadendResponse update(Long id, HeadendCreateRequest request) {
        Headend h = headendRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Headend not found: " + id));

        h.setName(request.getName());
        h.setLocation(request.getLocation());
        h.setBandwidthCapacityMbps(request.getBandwidthCapacityMbps());

        Headend saved = headendRepository.save(h);
        return toResponse(saved);
    }

    private HeadendResponse toResponse(Headend h) {
        return HeadendResponse.builder()
                .headendId(h.getHeadendId())
                .name(h.getName())
                .location(h.getLocation())
                .bandwidthCapacityMbps(h.getBandwidthCapacityMbps())
                .build();
    }
}
