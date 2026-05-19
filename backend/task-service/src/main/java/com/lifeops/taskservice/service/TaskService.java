package com.lifeops.taskservice.service;

import com.lifeops.taskservice.dto.TaskResponse;
import com.lifeops.taskservice.entity.Task;
import com.lifeops.taskservice.entity.TaskSourceType;
import com.lifeops.taskservice.entity.TaskStatus;
import com.lifeops.taskservice.event.DocumentAnalyzedEvent;
import com.lifeops.taskservice.exception.TaskNotFoundException;
import com.lifeops.taskservice.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private TaskRepository taskRepository;
    private TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getRecentTask() {
        return taskRepository.findTop20ByOrderByCreatedAtDesc()
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getPendingTask() {
        return taskRepository.findTop20ByStatusOrderByCreatedAtDesc(TaskStatus.WAITING_FOR_APPROVAL)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID id) {
        Task task = taskRepository.findBySourceId(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toResponse(task);
    }

    @Transactional
    public void createTaskFromDocumentAnalyzedEvent(DocumentAnalyzedEvent event) {
        log.info("Creating task proposal from document analyzed event documentId: {}", event.documentId());

        if (taskRepository.findBySourceId(event.documentId()).isPresent()) {
            log.info("Task proposal already exists for documentId={}, skipping", event.documentId());
            return;
        }

        String title = "Review required action for: " + event.title();

        String description = """
                A document was analyzed and may require user action.
                
                Summary:
                %s
                
                Deadline:
                %s
                """.formatted(
                safeValue(event.summary(), "No summary available"),
                safeValue(event.deadlineText(), "No clear deadline found")
        );

        String recommendedAction = safeValue(
                event.requiredAction(),
                "Review the document and decide if any action is needed"
        );

        Task task = new Task(
                event.userId(),
                TaskSourceType.DOCUMENT_ANALYSIS,
                event.documentId(),
                title,
                description,
                recommendedAction,
                safeValue(event.riskLevel(), "UNKNOWN")
        );

        Task savedTask = taskRepository.save(task);

        log.info("Task proposal created taskId={}, documentId={}",
                savedTask.getId(),
                event.documentId());

    }

    private String safeValue(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }

        return value;
    }
}
