package com.company.network_inventory.service;

import com.company.network_inventory.dto.task.TaskCreateRequest;
import com.company.network_inventory.dto.task.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse create(TaskCreateRequest request);
    List<TaskResponse> getAll();
}
