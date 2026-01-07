package com.company.network_inventory.repository;

import com.company.network_inventory.entity.Task_DISABLED;
import com.company.network_inventory.entity.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository_DISABLED extends JpaRepository<Task_DISABLED, Long> {
    List<Task_DISABLED> findByStatus(TaskStatus status);
    List<Task_DISABLED> findByAssignedTechnician_TechnicianId(Long technicianId);
    List<Task_DISABLED> findByCustomer_CustomerId(Long customerId);
}
