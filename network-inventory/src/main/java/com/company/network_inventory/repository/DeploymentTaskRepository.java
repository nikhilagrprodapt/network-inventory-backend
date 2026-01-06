package com.company.network_inventory.repository;

import com.company.network_inventory.entity.DeploymentTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeploymentTaskRepository extends JpaRepository<DeploymentTask, Long> {
    List<DeploymentTask> findByCustomerCustomerId(Long customerId);
    List<DeploymentTask> findByTechnicianTechnicianId(Long technicianId);
}
