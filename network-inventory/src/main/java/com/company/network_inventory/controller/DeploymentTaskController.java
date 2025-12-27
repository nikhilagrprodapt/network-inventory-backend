package com.company.network_inventory.controller;

import com.company.network_inventory.dto.task.TaskCreateRequest;
import com.company.network_inventory.dto.task.TaskResponse;
import com.company.network_inventory.dto.task.TaskStatusUpdateRequest;
import com.company.network_inventory.service.DeploymentTaskService;
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
    public TaskResponse create(@Valid @RequestBody TaskCreateRequest request) {
        return deploymentTaskService.create(request);
    }

    @PatchMapping("/{taskId}/status")
    public TaskResponse updateStatus(@PathVariable Long taskId, @Valid @RequestBody TaskStatusUpdateRequest request) {
        return deploymentTaskService.updateStatus(taskId, request);
    }

    @GetMapping("/customer/{customerId}")
    public List<TaskResponse> byCustomer(@PathVariable Long customerId) {
        return deploymentTaskService.getByCustomer(customerId);
    }

    @GetMapping("/technician/{technicianId}")
    public List<TaskResponse> byTechnician(@PathVariable Long technicianId) {
        return deploymentTaskService.getByTechnician(technicianId);
    }
}
