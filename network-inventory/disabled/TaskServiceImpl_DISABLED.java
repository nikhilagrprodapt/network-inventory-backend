package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.task.TaskCreateRequest;
import com.company.network_inventory.dto.task.TaskResponse;
import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.DeploymentTask;
import com.company.network_inventory.entity.Technician;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.CustomerRepository;
import com.company.network_inventory.repository.DeploymentTaskRepository;
import com.company.network_inventory.repository.TechnicianRepository;
import com.company.network_inventory.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl_DISABLED implements TaskService {

    private final DeploymentTaskRepository taskRepository;
    private final CustomerRepository customerRepository;
    private final TechnicianRepository technicianRepository;

    @Override
    @Transactional
    public TaskResponse create(TaskCreateRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + request.getCustomerId()));

        Technician tech = null;
        if (request.getTechnicianId() != null) {
            tech = technicianRepository.findById(request.getTechnicianId())
                    .orElseThrow(() -> new ResourceNotFoundException("Technician not found: " + request.getTechnicianId()));
        }

        DeploymentTask task = DeploymentTask.builder()
                .customer(customer)
                .technician(tech)
                .taskType(request.getTitle())                 // map title -> taskType
                .notes(request.getDescription())              // map description -> notes
                .status(request.getStatus())                  // uses your enum OPEN/IN_PROGRESS/DONE/CANCELLED
                .scheduledAt(request.getDueAt() != null ? request.getDueAt() : LocalDateTime.now())
                .build();

        DeploymentTask saved = taskRepository.save(task);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAll() {
        return taskRepository.findAll().stream().map(this::toResponse).toList();
    }

    private TaskResponse toResponse(DeploymentTask t) {

        Long customerId = (t.getCustomer() != null) ? t.getCustomer().getCustomerId() : null;
        String customerName = (t.getCustomer() != null) ? t.getCustomer().getName() : null;

        Long techId = (t.getTechnician() != null) ? t.getTechnician().getTechnicianId() : null;
        String techName = (t.getTechnician() != null) ? t.getTechnician().getName() : null;

        return TaskResponse.builder()
                .taskId(t.getTaskId())
                .title(t.getTaskType())
                .description(t.getNotes())
                .status(t.getStatus())
                .customerId(customerId)
                .customerName(customerName)
                .technicianId(techId)
                .technicianName(techName)
                .dueAt(t.getScheduledAt())
                .build();
    }
}
