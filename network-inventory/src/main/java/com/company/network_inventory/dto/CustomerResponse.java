package com.company.network_inventory.dto;

import com.company.network_inventory.entity.enums.ConnectionType;
import com.company.network_inventory.entity.enums.CustomerStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CustomerResponse {
    private Long customerId;
    private String name;
    private String address;
    private String neighborhood;
    private String plan;
    private ConnectionType connectionType;
    private CustomerStatus status;

    private Long splitterId;
    private Integer splitterPort;

    private LocalDateTime createdAt;
}
