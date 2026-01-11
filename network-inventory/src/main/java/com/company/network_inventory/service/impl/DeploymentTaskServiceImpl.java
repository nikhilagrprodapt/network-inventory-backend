package com.company.network_inventory.service.impl;

import com.company.network_inventory.audit.service.AuditService;
import com.company.network_inventory.dto.task.*;
import com.company.network_inventory.entity.Customer;
import com.company.network_inventory.entity.DeploymentTask;
import com.company.network_inventory.entity.TaskChecklistItem;
import com.company.network_inventory.entity.TaskNote;
import com.company.network_inventory.entity.Technician;
import com.company.network_inventory.entity.enums.TaskStatus;
import com.company.network_inventory.repository.CustomerRepository;
import com.company.network_inventory.repository.DeploymentTaskRepository;
import com.company.network_inventory.repository.TaskChecklistItemRepository;
import com.company.network_inventory.repository.TaskNoteRepository;
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
    private final TaskNoteRepository taskNoteRepository;
    private final TaskChecklistItemRepository taskChecklistItemRepository;

    // ✅ Step C: audit
    private final AuditService auditService;

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
                .status(tech == null ? TaskStatus.OPEN : TaskStatus.ASSIGNED)
                .scheduledAt(LocalDateTime.now())
                .build();

        DeploymentTask saved = taskRepository.save(task);

        // ✅ existing log
        auditService.log(
                "TASK_CREATED",
                "TASK",
                saved.getTaskId(),
                "Created taskType=" + safe(saved.getTaskType()) + ", status=" + saved.getStatus()
        );

        // ✅ NEW: if technician is provided during create, log it as an assign event too
        if (tech != null) {
            auditService.log(
                    "TASK_ASSIGNED",
                    "TASK",
                    saved.getTaskId(),
                    "technicianId null -> " + tech.getTechnicianId() + ", status=" + saved.getStatus()
            );
        }

        return toResponse(saved);
    }


    @Override
    @Transactional
    public TaskResponse assignTechnician(Long taskId, TaskAssignRequest request) {

        DeploymentTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));

        TaskStatus beforeStatus = task.getStatus() == null ? TaskStatus.OPEN : task.getStatus();
        Long beforeTechId = task.getTechnician() != null ? task.getTechnician().getTechnicianId() : null;

        // ✅ allow unassign
        if (request.getTechnicianId() == null) {
            task.setTechnician(null);

            // if task wasn't started, go back to OPEN
            if (beforeStatus == TaskStatus.ASSIGNED) {
                task.setStatus(TaskStatus.OPEN);
            }

            DeploymentTask saved = taskRepository.save(task);

            auditService.log(
                    "TASK_UNASSIGNED",
                    "TASK",
                    saved.getTaskId(),
                    "technicianId " + beforeTechId + " -> null, status=" + beforeStatus + " -> " + saved.getStatus()
            );

            return toResponse(saved);
        }

        Technician tech = technicianRepository.findById(request.getTechnicianId())
                .orElseThrow(() -> new RuntimeException("Technician not found: " + request.getTechnicianId()));

        task.setTechnician(tech);

        // ✅ if task is OPEN and technician assigned, mark ASSIGNED (still "PENDING" in UI)
        if (beforeStatus == TaskStatus.OPEN) {
            task.setStatus(TaskStatus.ASSIGNED);
        }

        DeploymentTask saved = taskRepository.save(task);

        auditService.log(
                "TASK_ASSIGNED",
                "TASK",
                saved.getTaskId(),
                "technicianId " + beforeTechId + " -> " + tech.getTechnicianId() + ", status=" + beforeStatus + " -> " + saved.getStatus()
        );

        return toResponse(saved);
    }

    @Override
    @Transactional
    public TaskResponse updateStatus(Long taskId, TaskStatusUpdateRequest request) {

        DeploymentTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));

        if (request == null || request.getStatus() == null) {
            throw new IllegalArgumentException("status is required.");
        }

        TaskStatus current = task.getStatus() == null ? TaskStatus.OPEN : task.getStatus();
        TaskStatus next = request.getStatus();

        // ✅ Step C: strict transitions (Journey)
        validateTransitionStrict(current, next);

        // ✅ Step C: checklist enforcement before DONE
        if (next == TaskStatus.DONE) {
            List<TaskChecklistItem> items = taskChecklistItemRepository.findByTaskTaskIdOrderByItemIdAsc(taskId);

            if (items == null || items.isEmpty()) {
                throw new IllegalArgumentException("Cannot complete task: checklist is empty. Add checklist items first.");
            }

            boolean hasIncomplete = items.stream().anyMatch(it -> !it.isDone());
            if (hasIncomplete) {
                throw new IllegalArgumentException("Cannot complete task: please complete all checklist items first.");
            }
        }

        // timeline updates
        if (next == TaskStatus.IN_PROGRESS && task.getStartedAt() == null) {
            task.setStartedAt(LocalDateTime.now());
        }

        if (next == TaskStatus.DONE) {
            if (task.getStartedAt() == null) task.setStartedAt(LocalDateTime.now());
            if (task.getCompletedAt() == null) task.setCompletedAt(LocalDateTime.now());
        }

        task.setStatus(next);

        DeploymentTask saved = taskRepository.save(task);

        auditService.log(
                "TASK_STATUS_CHANGED",
                "TASK",
                saved.getTaskId(),
                "status " + current + " -> " + next
        );

        return toResponse(saved);
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

        auditService.log("TASK_DELETED", "TASK", taskId, "Deleted task");
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

    @Override
    @Transactional(readOnly = true)
    public TaskDetailResponse getDetail(Long taskId) {
        TaskResponse task = getOne(taskId);

        var notes = taskNoteRepository.findByTaskTaskIdOrderByCreatedAtDesc(taskId)
                .stream()
                .map(n -> TaskNoteResponse.builder()
                        .noteId(n.getNoteId())
                        .text(n.getText())
                        .author(n.getAuthor())
                        .createdAt(n.getCreatedAt())
                        .build())
                .toList();

        var checklist = taskChecklistItemRepository.findByTaskTaskIdOrderByItemIdAsc(taskId)
                .stream()
                .map(c -> TaskChecklistItemResponse.builder()
                        .itemId(c.getItemId())
                        .label(c.getLabel())
                        .done(c.isDone())
                        .build())
                .toList();

        return TaskDetailResponse.builder()
                .task(task)
                .notes(notes)
                .checklist(checklist)
                .build();
    }

    @Override
    @Transactional
    public TaskNoteResponse addNote(Long taskId, TaskNoteCreateRequest request) {
        DeploymentTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));

        if (request == null || request.getText() == null || request.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("text is required.");
        }

        TaskNote note = TaskNote.builder()
                .task(task)
                .text(request.getText())
                .author(request.getAuthor())
                .build();

        TaskNote saved = taskNoteRepository.save(note);

        auditService.log(
                "TASK_NOTE_ADDED",
                "TASK",
                taskId,
                "noteId=" + saved.getNoteId()
        );

        return TaskNoteResponse.builder()
                .noteId(saved.getNoteId())
                .text(saved.getText())
                .author(saved.getAuthor())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public TaskChecklistItemResponse addChecklistItem(Long taskId, TaskChecklistCreateRequest request) {
        DeploymentTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));

        if (request == null || request.getLabel() == null || request.getLabel().trim().isEmpty()) {
            throw new IllegalArgumentException("label is required.");
        }

        TaskChecklistItem item = TaskChecklistItem.builder()
                .task(task)
                .label(request.getLabel())
                .done(false)
                .build();

        TaskChecklistItem saved = taskChecklistItemRepository.save(item);

        auditService.log(
                "TASK_CHECKLIST_ITEM_ADDED",
                "TASK",
                taskId,
                "itemId=" + saved.getItemId() + ", label=" + safe(saved.getLabel())
        );

        return TaskChecklistItemResponse.builder()
                .itemId(saved.getItemId())
                .label(saved.getLabel())
                .done(saved.isDone())
                .build();
    }

    @Override
    @Transactional
    public TaskChecklistItemResponse toggleChecklist(Long itemId, TaskChecklistToggleRequest request) {
        TaskChecklistItem item = taskChecklistItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Checklist item not found: " + itemId));

        boolean next = Boolean.TRUE.equals(request.getDone());
        item.setDone(next);

        TaskChecklistItem saved = taskChecklistItemRepository.save(item);

        Long taskId = (saved.getTask() != null) ? saved.getTask().getTaskId() : null;

        auditService.log(
                "TASK_CHECKLIST_TOGGLED",
                "TASK",
                taskId,
                "itemId=" + saved.getItemId() + ", done=" + next
        );

        return TaskChecklistItemResponse.builder()
                .itemId(saved.getItemId())
                .label(saved.getLabel())
                .done(saved.isDone())
                .build();
    }

    // --------------------
    // Mapping
    // --------------------
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

    // --------------------
    // Step C Strict Transition rules
    // OPEN/ASSIGNED -> IN_PROGRESS -> DONE
    // --------------------
    private void validateTransitionStrict(TaskStatus from, TaskStatus to) {
        if (from == null) from = TaskStatus.OPEN;
        if (to == null) throw new IllegalArgumentException("Status is required.");
        if (from == to) return;

        boolean ok = switch (from) {
            case OPEN, ASSIGNED -> (to == TaskStatus.IN_PROGRESS);
            case IN_PROGRESS -> (to == TaskStatus.DONE);
            case DONE, CANCELLED, BLOCKED -> false;
        };

        if (!ok) throw new IllegalArgumentException("Invalid status transition: " + from + " -> " + to);
    }

    private String safe(String s) {
        return s == null ? "-" : s;
    }
}
