package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.FDHCreateRequest;
import com.company.network_inventory.dto.FDHResponse;
import com.company.network_inventory.entity.FDH;
import com.company.network_inventory.entity.Headend;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.FDHRepository;
import com.company.network_inventory.repository.HeadendRepository;
import com.company.network_inventory.service.FDHService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FDHServiceImpl implements FDHService {

    private final FDHRepository fdhRepository;
    private final HeadendRepository headendRepository;

    @Override
    public FDHResponse create(FDHCreateRequest request) {

        Headend headend = headendRepository.findById(request.getHeadendId())
                .orElseThrow(() -> new ResourceNotFoundException("Headend not found: " + request.getHeadendId()));

        FDH fdh = FDH.builder()
                .name(request.getName())
                .location(request.getLocation())
                .region(request.getRegion())
                .maxPorts(request.getMaxPorts())
                .headend(headend)
                .build();

        FDH saved = fdhRepository.save(fdh);

        return FDHResponse.builder()
                .fdhId(saved.getFdhId())
                .name(saved.getName())
                .location(saved.getLocation())
                .region(saved.getRegion())
                .maxPorts(saved.getMaxPorts())
                .headendId(saved.getHeadend().getHeadendId())
                .build();
    }

    @Override
    public List<FDHResponse> getAll() {
        return fdhRepository.findAll().stream()
                .map(f -> FDHResponse.builder()
                        .fdhId(f.getFdhId())
                        .name(f.getName())
                        .location(f.getLocation())
                        .region(f.getRegion())
                        .maxPorts(f.getMaxPorts())
                        .headendId(f.getHeadend().getHeadendId())
                        .build()
                )
                .toList();
    }
}
