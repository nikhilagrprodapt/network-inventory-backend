package com.company.network_inventory.entity;

import com.company.network_inventory.entity.enums.AssetStatus;
import com.company.network_inventory.entity.enums.AssetType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetType type;

    private String model;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetStatus status;

    @ManyToOne
    @JoinColumn(name = "assigned_to_customer_id")
    private Customer assignedToCustomer;

    private LocalDateTime assignedAt;
}
