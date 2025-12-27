package com.company.network_inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FDH {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fdhId;

    @Column(nullable = false, unique = true)
    private String name;

    private String location;

    private String region;

    private Integer maxPorts;

    @ManyToOne(optional = false)
    @JoinColumn(name = "headend_id")
    private Headend headend;
}
