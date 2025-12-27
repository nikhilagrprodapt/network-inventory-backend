package com.company.network_inventory.service;

import com.company.network_inventory.dto.task.TaskCreateRequest;
import com.company.network_inventory.dto.task.TaskResponse;
import com.company.network_inventory.dto.task.TaskStatusUpdateRequest;

import java.util.List;

public interface DeploymentTaskService {

    TaskResponse create(TaskCreateRequest request);

    TaskResponse updateStatus(Long taskId, TaskStatusUpdateRequest request);

    List<TaskResponse> getByCustomer(Long customerId);

    List<TaskResponse> getByTechnician(Long technicianId);
}
