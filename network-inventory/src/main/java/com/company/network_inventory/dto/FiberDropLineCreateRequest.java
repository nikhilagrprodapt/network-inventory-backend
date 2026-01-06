package com.company.network_inventory.dto;

import com.company.network_inventory.entity.enums.FiberLineStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FiberDropLineCreateRequest {

    @NotNull
    private Long fromSplitterId;

    @NotNull
    private Long toCustomerId;

    private BigDecimal lengthMeters;

    @NotNull
    private FiberLineStatus status;
}
