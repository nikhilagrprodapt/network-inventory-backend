package com.company.network_inventory.dto;

import com.company.network_inventory.support.enums.SupportCaseReason;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDeactivateRequest {

    @NotNull
    private SupportCaseReason reason;  // CUSTOMER_REQUEST

    private String exitNotes;
}
