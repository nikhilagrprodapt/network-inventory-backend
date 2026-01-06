package com.company.network_inventory.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoreSwitchResponse {
    private Long coreSwitchId;
    private String name;
    private String location;
    private Long headendId;
}
