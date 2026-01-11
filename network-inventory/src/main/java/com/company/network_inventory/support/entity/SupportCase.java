package com.company.network_inventory.support.entity;

import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.support.enums.SupportCaseReason;
import com.company.network_inventory.support.enums.SupportCaseStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "support_case")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SupportCaseReason reason;

    @Column(length = 2000)
    private String exitNotes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SupportCaseStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;
}
