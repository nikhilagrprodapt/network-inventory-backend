package com.company.network_inventory.dto.topology;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TopologyCustomerDetailsResponse {

    private Long customerId;
    private String name;
    private String address;
    private String status;

    private Long splitterId;
    private String splitterName;
    private Integer splitterPort;

    // Fiber info
    private Long fiberLineId;
    private String fiberStatus;

    // ✅ FIX: lengthMeters type must match FiberDropLine (most likely BigDecimal)
    private BigDecimal fiberLengthMeters;

    // Explicit asset mapping (Journey-5)
    private String ontSerial;
    private String ontStatus;

    private String routerSerial;
    private String routerStatus;
}
