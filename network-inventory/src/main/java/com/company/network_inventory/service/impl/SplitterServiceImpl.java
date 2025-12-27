package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.SplitterCreateRequest;
import com.company.network_inventory.dto.SplitterResponse;
import com.company.network_inventory.entity.FDH;
import com.company.network_inventory.entity.Splitter;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.FDHRepository;
import com.company.network_inventory.repository.SplitterRepository;
import com.company.network_inventory.service.SplitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SplitterServiceImpl implements SplitterService {

    private final SplitterRepository splitterRepository;
    private final FDHRepository fdhRepository;

    @Override
    public SplitterResponse create(SplitterCreateRequest request) {

        FDH fdh = fdhRepository.findById(request.getFdhId())
                .orElseThrow(() -> new ResourceNotFoundException("FDH not found: " + request.getFdhId()));

        Splitter splitter = Splitter.builder()
                .name(request.getName())
                .model(request.getModel())
                .portCapacity(request.getPortCapacity())
                .fdh(fdh)
                .build();

        Splitter saved = splitterRepository.save(splitter);

        return SplitterResponse.builder()
                .splitterId(saved.getSplitterId())
                .name(saved.getName())
                .model(saved.getModel())
                .portCapacity(saved.getPortCapacity())
                .fdhId(saved.getFdh().getFdhId())
                .build();
    }

    @Override
    public List<SplitterResponse> getAll() {
        return splitterRepository.findAll().stream()
                .map(s -> SplitterResponse.builder()
                        .splitterId(s.getSplitterId())
                        .name(s.getName())
                        .model(s.getModel())
                        .portCapacity(s.getPortCapacity())
                        .fdhId(s.getFdh().getFdhId())
                        .build()
                )
                .toList();
    }
}
