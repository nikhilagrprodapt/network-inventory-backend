package com.company.network_inventory.service;

import com.company.network_inventory.dto.CoreSwitchCreateRequest;
import com.company.network_inventory.dto.CoreSwitchResponse;

import java.util.List;

public interface CoreSwitchService {
    CoreSwitchResponse create(CoreSwitchCreateRequest request);
    List<CoreSwitchResponse> getAll();
}
