package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.task.TechnicianCreateRequest;
import com.company.network_inventory.dto.task.TechnicianResponse;
import com.company.network_inventory.entity.Technician;
import com.company.network_inventory.repository.TechnicianRepository;
import com.company.network_inventory.service.TechnicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicianServiceImpl implements TechnicianService {

    private final TechnicianRepository technicianRepository;

    @Override
    public TechnicianResponse create(TechnicianCreateRequest request) {

        Technician tech = Technician.builder()
                .name(request.getName())
                .contact(request.getContact())
                .region(request.getRegion())
                .build();

        Technician saved = technicianRepository.save(tech);

        return TechnicianResponse.builder()
                .technicianId(saved.getTechnicianId())
                .name(saved.getName())
                .contact(saved.getContact())
                .region(saved.getRegion())
                .build();
    }

    @Override
    public List<TechnicianResponse> getAll() {
        return technicianRepository.findAll().stream()
                .map(t -> TechnicianResponse.builder()
                        .technicianId(t.getTechnicianId())
                        .name(t.getName())
                        .contact(t.getContact())
                        .region(t.getRegion())
                        .build())
                .toList();
    }
}
