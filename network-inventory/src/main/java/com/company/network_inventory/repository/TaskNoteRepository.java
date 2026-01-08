package com.company.network_inventory.repository;

import com.company.network_inventory.entity.TaskNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskNoteRepository extends JpaRepository<TaskNote, Long> {
    List<TaskNote> findByTaskTaskIdOrderByCreatedAtDesc(Long taskId);
}
