package com.company.network_inventory.dto.topology;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopologyAssetSummary {
    private Long assetId;
    private String assetType;     // "ONT" / "ROUTER" if present
    private String serial;        // best-effort from Asset fields
    private String status;        // best-effort from Asset fields
}
