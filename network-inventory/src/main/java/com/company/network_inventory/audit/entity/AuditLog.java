package com.company.network_inventory.audit.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String actor;         // who triggered it (from X-Actor or SYSTEM)
    private String action;        // CREATE, UPDATE, DELETE, ASSIGN, STATUS_CHANGE
    private String entityType;    // CUSTOMER, SPLITTER, TASK, ASSET, etc.
    private Long entityId;        // id of that entity

    @Column(length = 2000)
    private String details;       // extra info

    private String requestId;     // tracking id for that request

    private LocalDateTime createdAt;
}
