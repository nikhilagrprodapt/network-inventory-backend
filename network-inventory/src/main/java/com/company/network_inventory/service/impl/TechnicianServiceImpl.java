package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.*;
import com.company.network_inventory.entity.Technician;
import com.company.network_inventory.repository.TechnicianRepository;
import com.company.network_inventory.service.TechnicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicianServiceImpl implements TechnicianService {

    private final TechnicianRepository technicianRepository;

    private TechnicianResponse toResponse(Technician t) {
        return TechnicianResponse.builder()
                .technicianId(t.getTechnicianId())
                .name(t.getName())
                .phone(t.getPhone())
                .email(t.getEmail())
                .status(t.getStatus())
                .build();
    }

    private String normalizeStatus(String raw) {
        if (raw == null) return null;
        String s = raw.trim().toUpperCase();
        if (!s.equals("ACTIVE") && !s.equals("INACTIVE")) {
            throw new IllegalArgumentException("status must be ACTIVE or INACTIVE");
        }
        return s;
    }

    private String clean(String s) {
        if (s == null) return null;
        String v = s.trim();
        return v.isEmpty() ? null : v;
    }

    @Override
    @Transactional
    public TechnicianResponse create(TechnicianCreateRequest request) {
        Technician t = Technician.builder()
                .name(request.getName().trim())
                .phone(clean(request.getPhone()))
                .email(clean(request.getEmail()))
                .status(normalizeStatus(request.getStatus()))
                .build();

        return toResponse(technicianRepository.save(t));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicianResponse> getAll() {
        return technicianRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public TechnicianResponse getOne(Long id) {
        Technician t = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found: " + id));
        return toResponse(t);
    }

    @Override
    @Transactional
    public TechnicianResponse update(Long id, TechnicianUpdateRequest request) {
        Technician t = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found: " + id));

        if (request.getName() != null) {
            String name = request.getName().trim();
            if (name.isEmpty()) throw new IllegalArgumentException("name cannot be empty");
            t.setName(name);
        }
        if (request.getPhone() != null) t.setPhone(clean(request.getPhone()));
        if (request.getEmail() != null) t.setEmail(clean(request.getEmail()));
        if (request.getStatus() != null) t.setStatus(normalizeStatus(request.getStatus()));

        return toResponse(technicianRepository.save(t));
    }

    @Override
    @Transactional
    public TechnicianResponse updateStatus(Long id, TechnicianStatusRequest request) {
        Technician t = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found: " + id));

        t.setStatus(normalizeStatus(request.getStatus()));
        return toResponse(technicianRepository.save(t));
    }
}
