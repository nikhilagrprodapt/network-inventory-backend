package com.company.network_inventory.service.impl;

import com.company.network_inventory.dto.task.TaskAssignRequest;
import com.company.network_inventory.dto.task.TaskCreateRequest;
import com.company.network_inventory.dto.task.TaskResponse;
import com.company.network_inventory.dto.task.TaskStatusUpdateRequest;
import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.DeploymentTask;
import com.company.network_inventory.entity.Technician;
import com.company.network_inventory.entity.enums.TaskStatus;
import com.company.network_inventory.repository.CustomerRepository;
import com.company.network_inventory.repository.DeploymentTaskRepository;
import com.company.network_inventory.repository.TechnicianRepository;
import com.company.network_inventory.service.DeploymentTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeploymentTaskServiceImpl implements DeploymentTaskService {

    private final DeploymentTaskRepository taskRepository;
    private final CustomerRepository customerRepository;
    private final TechnicianRepository technicianRepository;

    @Override
    @Transactional
    public TaskResponse create(TaskCreateRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found: " + request.getCustomerId()));

        Technician tech = null;
        if (request.getTechnicianId() != null) {
            tech = technicianRepository.findById(request.getTechnicianId())
                    .orElseThrow(() -> new RuntimeException("Technician not found: " + request.getTechnicianId()));
        }

        DeploymentTask task = DeploymentTask.builder()
                .customer(customer)
                .technician(tech)
                .taskType(request.getTaskType())
                .notes(request.getNotes())
                .status(TaskStatus.OPEN)
                .scheduledAt(LocalDateTime.now())
                .build();

        return toResponse(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskResponse assignTechnician(Long taskId, TaskAssignRequest request) {

        DeploymentTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));

        Technician tech = technicianRepository.findById(request.getTechnicianId())
                .orElseThrow(() -> new RuntimeException("Technician not found: " + request.getTechnicianId()));

        task.setTechnician(tech);

        // keep status OPEN (assignment is not start)
        return toResponse(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskResponse updateStatus(Long taskId, TaskStatusUpdateRequest request) {

        DeploymentTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));

        TaskStatus newStatus = request.getStatus();
        task.setStatus(newStatus);

        if (newStatus == TaskStatus.IN_PROGRESS && task.getStartedAt() == null) {
            task.setStartedAt(LocalDateTime.now());
        }
        if (newStatus == TaskStatus.DONE && task.getCompletedAt() == null) {
            task.setCompletedAt(LocalDateTime.now());
        }
        return toResponse(taskRepository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getOne(Long taskId) {
        DeploymentTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));
        return toResponse(task);
    }

    @Override
    @Transactional
    public void delete(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }


    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAll() {
        return taskRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getByCustomer(Long customerId) {
        return taskRepository.findByCustomerCustomerId(customerId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getByTechnician(Long technicianId) {
        return taskRepository.findByTechnicianTechnicianId(technicianId).stream().map(this::toResponse).toList();
    }

    private TaskResponse toResponse(DeploymentTask t) {

        Long customerId = null;
        String customerName = null;
        if (t.getCustomer() != null) {
            customerId = t.getCustomer().getCustomerId();
            customerName = t.getCustomer().getName();
        }

        Long techId = null;
        String techName = null;
        if (t.getTechnician() != null) {
            techId = t.getTechnician().getTechnicianId();
            techName = t.getTechnician().getName();
        }

        return TaskResponse.builder()
                .taskId(t.getTaskId())
                .customerId(customerId)
                .customerName(customerName)
                .technicianId(techId)
                .technicianName(techName)
                .taskType(t.getTaskType())
                .status(t.getStatus() == null ? TaskStatus.OPEN : t.getStatus())
                .scheduledAt(t.getScheduledAt())
                .startedAt(t.getStartedAt())
                .completedAt(t.getCompletedAt())
                .notes(t.getNotes())
                .build();
    }
}
