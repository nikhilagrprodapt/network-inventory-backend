package com.company.network_inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CoreSwitch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coreSwitchId;

    private String name;
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "headend_id")
    private Headend headend;
}
