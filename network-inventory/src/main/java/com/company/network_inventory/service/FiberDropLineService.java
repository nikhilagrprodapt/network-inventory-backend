package com.company.network_inventory.service;

import com.company.network_inventory.entity.FiberDropLine;
import java.util.List;

public interface FiberDropLineService {
    FiberDropLine create(FiberDropLine request);
    List<FiberDropLine> getAll();
}
