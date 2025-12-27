package com.company.network_inventory.entity;

import com.company.network_inventory.entity.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String neighborhood;

    private String plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;

    @ManyToOne
    @JoinColumn(name = "splitter_id")
    private Splitter splitter;

    private Integer splitterPort;

    private LocalDateTime createdAt;
}
