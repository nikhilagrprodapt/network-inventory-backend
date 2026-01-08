package com.company.network_inventory.service;

import com.company.network_inventory.dto.task.*;

import java.util.List;

public interface DeploymentTaskService {

    TaskResponse create(TaskCreateRequest request);

    TaskResponse assignTechnician(Long taskId, TaskAssignRequest request);

    TaskResponse updateStatus(Long taskId, TaskStatusUpdateRequest request);

    TaskResponse getOne(Long taskId);

    void delete(Long taskId);

    List<TaskResponse> getAll();

    List<TaskResponse> getByCustomer(Long customerId);

    List<TaskResponse> getByTechnician(Long technicianId);

    TaskDetailResponse getDetail(Long taskId);

    TaskNoteResponse addNote(Long taskId, TaskNoteCreateRequest request);

    TaskChecklistItemResponse addChecklistItem(Long taskId, TaskChecklistCreateRequest request);

    TaskChecklistItemResponse toggleChecklist(Long itemId, TaskChecklistToggleRequest request);
}
