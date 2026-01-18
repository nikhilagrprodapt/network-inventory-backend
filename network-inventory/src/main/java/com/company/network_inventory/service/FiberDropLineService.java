package com.company.network_inventory.service;

import com.company.network_inventory.dto.FiberDropLineCreateRequest;
import com.company.network_inventory.dto.FiberDropLineResponse;
import com.company.network_inventory.entity.FiberDropLine;

import java.util.List;

public interface FiberDropLineService {
    FiberDropLineResponse create(FiberDropLineCreateRequest request);

    FiberDropLine create(FiberDropLine request);

    List<FiberDropLineResponse> getAll();
}
