package com.company.network_inventory.service;

import com.company.network_inventory.dto.SplitterCreateRequest;
import com.company.network_inventory.dto.SplitterResponse;

import java.util.List;

public interface SplitterService {
    SplitterResponse create(SplitterCreateRequest request);
    List<SplitterResponse> getAll();

    List<Integer> getAvailablePorts(Long splitterId);
}
