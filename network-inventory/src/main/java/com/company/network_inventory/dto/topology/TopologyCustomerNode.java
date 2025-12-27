package com.company.network_inventory.dto.topology;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopologyCustomerNode {
    private Long customerId;
    private String name;
    private Integer splitterPort;
    private String status;
}
