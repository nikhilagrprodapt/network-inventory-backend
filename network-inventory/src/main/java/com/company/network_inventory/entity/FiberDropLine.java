package com.company.network_inventory.entity;

import com.company.network_inventory.entity.enums.FiberLineStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FiberDropLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_splitter_id")
    private Splitter fromSplitter;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_customer_id", unique = true)
    private Customer toCustomer;

    private BigDecimal lengthMeters;

    @Enumerated(EnumType.STRING)
    private FiberLineStatus status;
}
