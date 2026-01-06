package com.company.network_inventory.audit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String actor; // who triggered it (from X-Actor or SYSTEM)

    @Column(nullable = false)
    private String action; // CREATE, UPDATE, DELETE, ASSIGN, STATUS_CHANGE

    @Column(nullable = false)
    private String entityType; // CUSTOMER, SPLITTER, TASK, ASSET, etc.

    private Long entityId;

    @Column(length = 2000)
    private String details;

    private String requestId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
