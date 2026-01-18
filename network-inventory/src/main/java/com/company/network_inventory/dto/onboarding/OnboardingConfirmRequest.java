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
    private String connectionType;

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
    private String taskType;

    // Optional
    private Long technicianId;
    private String notes;
}
