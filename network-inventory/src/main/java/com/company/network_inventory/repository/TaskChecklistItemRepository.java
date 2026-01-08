package com.company.network_inventory.repository;

import com.company.network_inventory.entity.TaskChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskChecklistItemRepository extends JpaRepository<TaskChecklistItem, Long> {
    List<TaskChecklistItem> findByTaskTaskIdOrderByItemIdAsc(Long taskId);
}
