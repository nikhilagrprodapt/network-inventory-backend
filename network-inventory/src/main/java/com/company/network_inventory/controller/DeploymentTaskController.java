package com.company.network_inventory.controller;

import com.company.network_inventory.dto.task.TaskAssignRequest;
import com.company.network_inventory.dto.task.TaskCreateRequest;
import com.company.network_inventory.dto.task.TaskResponse;
import com.company.network_inventory.dto.task.TaskStatusUpdateRequest;
import com.company.network_inventory.service.DeploymentTaskService;
import com.company.network_inventory.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class DeploymentTaskController {

    private final DeploymentTaskService deploymentTaskService;

    @PostMapping
    public ApiResponse<TaskResponse> create(@Valid @RequestBody TaskCreateRequest request) {
        return ApiResponse.ok("Task created", deploymentTaskService.create(request));
    }

    @GetMapping
    public ApiResponse<List<TaskResponse>> all() {
        return ApiResponse.ok("All tasks", deploymentTaskService.getAll());
    }

    @PatchMapping("/{taskId}/assign")
    public ApiResponse<TaskResponse> assign(@PathVariable Long taskId, @Valid @RequestBody TaskAssignRequest request) {
        return ApiResponse.ok("Technician assigned", deploymentTaskService.assignTechnician(taskId, request));
    }

    @PatchMapping("/{taskId}/status")
    public ApiResponse<TaskResponse> updateStatus(@PathVariable Long taskId, @Valid @RequestBody TaskStatusUpdateRequest request) {
        return ApiResponse.ok("Task status updated", deploymentTaskService.updateStatus(taskId, request));
    }
}