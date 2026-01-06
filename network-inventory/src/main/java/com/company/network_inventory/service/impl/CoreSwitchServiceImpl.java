package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.CoreSwitchCreateRequest;
import com.company.network_inventory.dto.CoreSwitchResponse;
import com.company.network_inventory.entity.CoreSwitch;
import com.company.network_inventory.entity.Headend;
import com.company.network_inventory.repository.CoreSwitchRepository;
import com.company.network_inventory.repository.HeadendRepository;
import com.company.network_inventory.service.CoreSwitchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreSwitchServiceImpl implements CoreSwitchService {

    private final CoreSwitchRepository coreSwitchRepository;
    private final HeadendRepository headendRepository;

    @Override
    public CoreSwitchResponse create(CoreSwitchCreateRequest request) {
        Headend headend = headendRepository.findById(request.getHeadendId())
                .orElseThrow(() -> new RuntimeException("Headend not found: " + request.getHeadendId()));

        CoreSwitch cs = CoreSwitch.builder()
                .name(request.getName())
                .location(request.getLocation())
                .headend(headend)
                .build();

        CoreSwitch saved = coreSwitchRepository.save(cs);

        return CoreSwitchResponse.builder()
                .coreSwitchId(saved.getCoreSwitchId())
                .name(saved.getName())
                .location(saved.getLocation())
                .headendId(saved.getHeadend().getHeadendId())
                .build();
    }

    @Override
    public List<CoreSwitchResponse> getAll() {
        return coreSwitchRepository.findAll().stream()
                .map(cs -> CoreSwitchResponse.builder()
                        .coreSwitchId(cs.getCoreSwitchId())
                        .name(cs.getName())
                        .location(cs.getLocation())
                        .headendId(cs.getHeadend() != null ? cs.getHeadend().getHeadendId() : null)
                        .build())
                .toList();
    }
}
