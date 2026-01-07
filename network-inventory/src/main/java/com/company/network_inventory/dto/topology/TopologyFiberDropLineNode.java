package com.company.network_inventory.dto.topology;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TopologyFiberDropLineNode {
    private Long lineId;
    private BigDecimal lengthMeters;
    private String status; // FiberLineStatus as string

    // Customer attached via FiberDropLine.toCustomer (OneToOne)
    private TopologyCustomerNode customer;
}
