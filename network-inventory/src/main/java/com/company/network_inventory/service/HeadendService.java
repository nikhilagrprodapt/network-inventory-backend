package com.company.network_inventory.service;

import com.company.network_inventory.dto.HeadendCreateRequest;
import com.company.network_inventory.dto.HeadendResponse;

import java.util.List;

public interface HeadendService {
    HeadendResponse create(HeadendCreateRequest request);
    List<HeadendResponse> getAll();

    HeadendResponse getOne(Long id);

    HeadendResponse update(Long id, HeadendCreateRequest request);
}
