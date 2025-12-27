package com.company.network_inventory.service;

import com.company.network_inventory.dto.task.TechnicianCreateRequest;
import com.company.network_inventory.dto.task.TechnicianResponse;

import java.util.List;

public interface TechnicianService {
    TechnicianResponse create(TechnicianCreateRequest request);
    List<TechnicianResponse> getAll();
}
