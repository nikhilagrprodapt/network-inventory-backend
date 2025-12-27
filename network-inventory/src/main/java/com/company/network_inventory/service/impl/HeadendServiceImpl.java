package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.HeadendCreateRequest;
import com.company.network_inventory.dto.HeadendResponse;
import com.company.network_inventory.entity.Headend;
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
        Headend headend = Headend.builder()
                .name(request.getName())
                .location(request.getLocation())
                .bandwidthCapacityMbps(request.getBandwidthCapacityMbps())
                .build();

        Headend saved = headendRepository.save(headend);

        return HeadendResponse.builder()
                .headendId(saved.getHeadendId())
                .name(saved.getName())
                .location(saved.getLocation())
                .bandwidthCapacityMbps(saved.getBandwidthCapacityMbps())
                .build();
    }

    @Override
    public List<HeadendResponse> getAll() {
        return headendRepository.findAll().stream()
                .map(h -> HeadendResponse.builder()
                        .headendId(h.getHeadendId())
                        .name(h.getName())
                        .location(h.getLocation())
                        .bandwidthCapacityMbps(h.getBandwidthCapacityMbps())
                        .build()
                )
                .toList();
    }
}
