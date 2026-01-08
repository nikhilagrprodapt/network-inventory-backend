package com.company.network_inventory.controller;

import com.company.network_inventory.dto.task.*;
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

    private final DeploymentTaskService taskService;

    @PostMapping
    public ApiResponse<TaskResponse> create(@Valid @RequestBody TaskCreateRequest request) {
        return ApiResponse.ok("Task created", taskService.create(request));
    }

    @GetMapping
    public ApiResponse<List<TaskResponse>> all() {
        return ApiResponse.ok("Tasks fetched", taskService.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<TaskResponse> getOne(@PathVariable Long id) {
        return ApiResponse.ok("Task fetched", taskService.getOne(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ApiResponse.ok("Task deleted", null);
    }

    @PutMapping("/{id}/assign-technician")
    public ApiResponse<TaskResponse> assignTechnician(
            @PathVariable Long id,
            @Valid @RequestBody TaskAssignRequest request
    ) {
        return ApiResponse.ok("Technician assigned", taskService.assignTechnician(id, request));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<TaskResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody TaskStatusUpdateRequest request
    ) {
        return ApiResponse.ok("Task status updated", taskService.updateStatus(id, request));
    }

    @GetMapping("/{id}/detail")
    public ApiResponse<TaskDetailResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok("Task detail fetched", taskService.getDetail(id));
    }

    @GetMapping("/by-customer/{customerId}")
    public ApiResponse<List<TaskResponse>> byCustomer(@PathVariable Long customerId) {
        return ApiResponse.ok("Tasks fetched", taskService.getByCustomer(customerId));
    }

    @GetMapping("/by-technician/{technicianId}")
    public ApiResponse<List<TaskResponse>> byTechnician(@PathVariable Long technicianId) {
        return ApiResponse.ok("Tasks fetched", taskService.getByTechnician(technicianId));
    }

    @PostMapping("/{id}/notes")
    public ApiResponse<TaskNoteResponse> addNote(
            @PathVariable Long id,
            @Valid @RequestBody TaskNoteCreateRequest request
    ) {
        return ApiResponse.ok("Note added", taskService.addNote(id, request));
    }

    @PostMapping("/{id}/checklist")
    public ApiResponse<TaskChecklistItemResponse> addChecklistItem(
            @PathVariable Long id,
            @Valid @RequestBody TaskChecklistCreateRequest request
    ) {
        return ApiResponse.ok("Checklist item added", taskService.addChecklistItem(id, request));
    }

    @PutMapping("/checklist/{itemId}/toggle")
    public ApiResponse<TaskChecklistItemResponse> toggleChecklist(
            @PathVariable Long itemId,
            @Valid @RequestBody TaskChecklistToggleRequest request
    ) {
        return ApiResponse.ok("Checklist toggled", taskService.toggleChecklist(itemId, request));
    }
}
