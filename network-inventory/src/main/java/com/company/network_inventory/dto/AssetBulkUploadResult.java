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
        private int rowNumber;       // 1-based row index in CSV file (excluding header logic)
        private String serialNumber; // if available
        private String reason;       // why failed
        private String rawLine;      // original line for debugging
    }
}
