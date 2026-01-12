package com.company.network_inventory.service;

import com.company.network_inventory.dto.*;
import java.util.List;

public interface TechnicianService {
    TechnicianResponse create(TechnicianCreateRequest request);

    List<TechnicianResponse> getAll();

    TechnicianResponse getOne(Long id);

    TechnicianResponse update(Long id, TechnicianUpdateRequest request);

    TechnicianResponse updateStatus(Long id, TechnicianStatusRequest request);
}
