package com.company.network_inventory.dto;

import com.company.network_inventory.entity.enums.FiberLineStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FiberDropLineResponse {
    private Long lineId;

    private Long fromSplitterId;
    private String fromSplitterName;
    private String fromSplitterModel;

    private Long toCustomerId;
    private String toCustomerName;
    private String toCustomerStatus;

    private BigDecimal lengthMeters;
    private FiberLineStatus status;
}
