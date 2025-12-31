package com.company.network_inventory.service;

import com.company.network_inventory.entity.CoreSwitch;
import java.util.List;

public interface CoreSwitchService {
    CoreSwitch create(CoreSwitch request);
    List<CoreSwitch> getAll();
}
