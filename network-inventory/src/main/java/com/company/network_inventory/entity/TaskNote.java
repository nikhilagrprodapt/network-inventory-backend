package com.company.network_inventory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "task_note")
public class TaskNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private DeploymentTask task;

    @Column(nullable = false, length = 2000)
    private String text;

    @Column(length = 200)
    private String author; // for now, passed from frontend (since no auth)

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
