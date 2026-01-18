package com.company.network_inventory.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AssetBulkUploadResult {

    private int createdCount;
    private int failedCount;

    private List<Failure> failures;

    @Data
    @Builder
    public static class Failure {
        private int rowNumber;
        private String serialNumber;
        private String reason;
        private String rawLine;
    }
}
