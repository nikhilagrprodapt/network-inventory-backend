package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.task.TaskCreateRequest;
import com.company.network_inventory.dto.task.TaskResponse;
import com.company.network_inventory.dto.task.TaskStatusUpdateRequest;
import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.DeploymentTask;
import com.company.network_inventory.entity.Technician;
import com.company.network_inventory.entity.enums.CustomerStatus;
import com.company.network_inventory.entity.enums.TaskStatus;
import com.company.network_inventory.exception.ResourceNotFoundException;
import com.company.network_inventory.repository.CustomerRepository;
import com.company.network_inventory.repository.DeploymentTaskRepository;
import com.company.network_inventory.repository.TechnicianRepository;
import com.company.network_inventory.service.DeploymentTaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.company.network_inventory.audit.service.AuditService;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeploymentTaskServiceImpl implements DeploymentTaskService {

    private final DeploymentTaskRepository deploymentTaskRepository;
    private final CustomerRepository customerRepository;
    private final TechnicianRepository technicianRepository;
    private final AuditService auditService;


    @Override
    public TaskResponse create(TaskCreateRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + request.getCustomerId()));

        Technician technician = null;
        if (request.getTechnicianId() != null) {
            technician = technicianRepository.findById(request.getTechnicianId())
                    .orElseThrow(() -> new ResourceNotFoundException("Technician not found: " + request.getTechnicianId()));
        }

        DeploymentTask task = DeploymentTask.builder()
                .customer(customer)
                .technician(technician)
                .taskType(request.getTaskType())
                .status(TaskStatus.SCHEDULED)
                .scheduledAt(LocalDateTime.now())
                .notes(request.getNotes())
                .build();

        DeploymentTask saved = deploymentTaskRepository.save(task);

// AUDIT: Task created
        auditService.log(
                "CREATE",
                "TASK",
                saved.getTaskId(),
                "Created task type=" + saved.getTaskType() +
                        ", customerId=" + saved.getCustomer().getCustomerId()
        );

// AUDIT: Technician assigned (ONLY if technician exists)
        if (saved.getTechnician() != null) {
            auditService.log(
                    "ASSIGN",
                    "TASK",
                    saved.getTaskId(),
                    "Assigned technicianId=" + saved.getTechnician().getTechnicianId()
            );
        }
        return toResponse(saved);
    }

    @Transactional
    @Override
    public TaskResponse updateStatus(Long taskId, TaskStatusUpdateRequest request) {

        DeploymentTask task = deploymentTaskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));

        TaskStatus newStatus = request.getStatus();
        task.setStatus(newStatus);

        if (request.getNotes() != null && !request.getNotes().isBlank()) {
            task.setNotes(request.getNotes());
        }

        if (newStatus == TaskStatus.IN_PROGRESS && task.getStartedAt() == null) {
            task.setStartedAt(LocalDateTime.now());
        }

        if (newStatus == TaskStatus.COMPLETED && task.getCompletedAt() == null) {
            task.setCompletedAt(LocalDateTime.now());

            // Optional realistic behavior: activate customer once installation is completed
            Customer customer = task.getCustomer();
            if (customer.getStatus() != CustomerStatus.ACTIVE) {
                customer.setStatus(CustomerStatus.ACTIVE);
                customerRepository.save(customer);
            }
        }
        DeploymentTask saved = deploymentTaskRepository.save(task);
        auditService.log(
                "STATUS_CHANGE",
                "TASK",
                saved.getTaskId(),
                "Task status changed to " + saved.getStatus()
        );
        return toResponse(saved);
    }

    @Override
    public List<TaskResponse> getByCustomer(Long customerId) {
        return deploymentTaskRepository.findByCustomer_CustomerId(customerId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> getByTechnician(Long technicianId) {
        return deploymentTaskRepository.findByTechnician_TechnicianId(technicianId).stream()
                .map(this::toResponse)
                .toList();
    }

    private TaskResponse toResponse(DeploymentTask task) {
        return TaskResponse.builder()
                .taskId(task.getTaskId())
                .customerId(task.getCustomer().getCustomerId())
                .customerName(task.getCustomer().getName())
                .technicianId(task.getTechnician() != null ? task.getTechnician().getTechnicianId() : null)
                .technicianName(task.getTechnician() != null ? task.getTechnician().getName() : null)
                .taskType(task.getTaskType())
                .status(task.getStatus())
                .scheduledAt(task.getScheduledAt())
                .startedAt(task.getStartedAt())
                .completedAt(task.getCompletedAt())
                .notes(task.getNotes())
                .build();
    }
}
