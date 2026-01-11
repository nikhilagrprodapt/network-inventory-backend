package com.company.network_inventory.dto.onboarding;

import com.company.network_inventory.dto.CustomerResponse;
import com.company.network_inventory.dto.AssetResponse;
import com.company.network_inventory.dto.task.TaskResponse;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OnboardingConfirmResponse {
    private CustomerResponse customer;
    private AssetResponse ont;
    private AssetResponse router;
    private TaskResponse task;
}
