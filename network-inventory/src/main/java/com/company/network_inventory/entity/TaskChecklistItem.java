package com.company.network_inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "task_checklist_item")
public class TaskChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private DeploymentTask task;

    @Column(nullable = false, length = 300)
    private String label;

    @Column(nullable = false)
    private boolean done;
}
