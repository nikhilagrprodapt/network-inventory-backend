package com.company.network_inventory.dto.onboarding;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OnboardingConfirmRequest {

    // Customer basics
    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String neighborhood;

    @NotBlank
    private String plan;

    @NotBlank
    private String connectionType; // "WIRED"/"WIRELESS" (string to avoid enum mismatch)

    // Network selection
    @NotNull
    private Long fdhId;

    @NotNull
    private Long splitterId;

    @NotNull
    private Integer splitterPort;

    // Assets
    @NotNull
    private Long ontAssetId;

    @NotNull
    private Long routerAssetId;

    // Task
    @NotBlank
    private String taskType; // e.g. "Install at House B1.2"

    // Optional
    private Long technicianId; // optional: allow null
    private String notes;      // optional
}
