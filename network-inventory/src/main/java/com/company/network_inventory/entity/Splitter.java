package com.company.network_inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Splitter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long splitterId;

    @Column(nullable = false, unique = true)
    private String name;

    private String model;

    @Column(nullable = false)
    private Integer portCapacity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fdh_id")
    private FDH fdh;
}
