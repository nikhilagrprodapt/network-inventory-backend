package com.company.network_inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Headend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long headendId;

    @Column(nullable = false, unique = true)
    private String name;

    private String location;

    private Integer bandwidthCapacityMbps;
}
