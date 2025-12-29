package com.company.network_inventory.dto;

import com.company.network_inventory.entity.enums.ConnectionType;
import com.company.network_inventory.entity.enums.CustomerStatus;
import lombok.Data;

@Data
public class CustomerUpdateRequest {
    private String plan;
    private ConnectionType connectionType;
    private CustomerStatus status;
}
