package com.company.network_inventory.service;

import com.company.network_inventory.dto.FDHCreateRequest;
import com.company.network_inventory.dto.FDHResponse;

import java.util.List;

public interface FDHService {
    FDHResponse create(FDHCreateRequest request);
    List<FDHResponse> getAll();
}
