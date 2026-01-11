package com.company.network_inventory.dto.topology;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TopologyFiberSummary {
    private Long lineId;
    private BigDecimal lengthMeters;
    private String status;
    private Long fromSplitterId;
    private String fromSplitterName;
}
