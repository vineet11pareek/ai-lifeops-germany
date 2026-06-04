package com.lifeops.taskservice.service;

import com.lifeops.taskservice.dto.AuthenticatedUserContext;
import com.lifeops.taskservice.dto.TaskResponse;
import com.lifeops.taskservice.entity.Task;
import com.lifeops.taskservice.entity.TaskSourceType;
import com.lifeops.taskservice.entity.TaskStatus;
import com.lifeops.taskservice.event.DocumentAnalyzedEvent;
import com.lifeops.taskservice.exception.InvalidTaskStateException;
import com.lifeops.taskservice.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private AuthenticatedUserContext userContext;

    @BeforeEach
    void setup(){
        userContext = new AuthenticatedUserContext(
                "google-sub-123",
                "test@examle.com",
                "Test User",
                "GOOGLE"
        );
    }

    private String externalId = "google-sub-123";

    @Test
    void shouldReturnRecentTask(){
        //Given
        Task task = createTask();
        TaskResponse response = toResponse(task);

        //when
        when(taskRepository.findTop20ByUserExternalIdOrderByCreatedAtDesc(externalId)).thenReturn(List.of(task));
        when(taskMapper.toResponse(task)).thenReturn(response);


        List<TaskResponse> taskList = taskService.getRecentTask(userContext);

        assertNotNull(taskList);
        assertThat(taskList).hasSize(1);
        assertThat(taskList.get(0).title()).isEqualTo("Review required action");

    }

    @Test
    void shouldReturnPendingTasks() {
        Task task = createTask();

        TaskResponse response = toResponse(task);

        when(taskRepository.findTop20ByUserExternalIdAndStatusOrderByCreatedAtDesc(externalId,TaskStatus.WAITING_FOR_APPROVAL))
                .thenReturn(List.of(task));
        when(taskMapper.toResponse(task)).thenReturn(response);

        List<TaskResponse> result = taskService.getPendingTask(userContext);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo("WAITING_FOR_APPROVAL");
    }

    @Test
    void shouldApproveTask() {
        UUID taskId = UUID.randomUUID();
        Task task = createTask();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenAnswer(invocation -> toResponse(task));

        TaskResponse response = taskService.approveTask(taskId, userContext);

        assertThat(response.status()).isEqualTo("APPROVED");
        verify(taskRepository).save(task);
    }

    @Test
    void shouldRejectTask() {
        UUID taskId = UUID.randomUUID();
        Task task = createTask();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenAnswer(invocation -> toResponse(task));

        TaskResponse response = taskService.rejectTask(taskId,userContext);

        assertThat(response.status()).isEqualTo("REJECTED");
        verify(taskRepository).save(task);
    }

    @Test
    void shouldBlockApprovingAlreadyApprovedTask() {
        UUID taskId = UUID.randomUUID();
        Task task = createTask();
        task.approve();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.approveTask(taskId,userContext))
                .isInstanceOf(InvalidTaskStateException.class)
                .hasMessageContaining("Only tasks waiting for approval can be approved");

        verify(taskRepository, never()).save(any(Task.class));
    }



    private Task createTask() {
        return new Task(
                null,
                "google-sub-123",
                TaskSourceType.DOCUMENT_ANALYSIS,
                UUID.randomUUID(),
                "Review required action",
                "Document requires user action.",
                "Submit missing documents.",
                "MEDIUM"
        );
    }

    @Test
    void shouldCreateTaskFromDocumentAnalyzedEvent() {
        UUID documentId = UUID.randomUUID();

        DocumentAnalyzedEvent event = new DocumentAnalyzedEvent(
                UUID.randomUUID(),
                documentId,
                null,
                "google-sub-123",
                "Letter from Finanzamt",
                "The document asks for missing documents.",
                "15.06.2026",
                "Submit missing documents.",
                "MEDIUM",
                "ANALYZED",
                Instant.now()
        );

        when(taskRepository.findBySourceIdAndUserExternalId(documentId,externalId)).thenReturn(Optional.empty());
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        taskService.createTaskFromDocumentAnalyzedEvent(event, event.userExternalId());

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void shouldSkipDuplicateTaskForSameSourceDocument() {
        UUID documentId = UUID.randomUUID();
        Task existingTask = createTask();

        DocumentAnalyzedEvent event = new DocumentAnalyzedEvent(
                UUID.randomUUID(),
                documentId,
                null,
                "google-sub-123",
                "Letter from Finanzamt",
                "The document asks for missing documents.",
                "15.06.2026",
                "Submit missing documents.",
                "MEDIUM",
                "ANALYZED",
                Instant.now()
        );

        when(taskRepository.findBySourceIdAndUserExternalId(documentId,externalId)).thenReturn(Optional.of(existingTask));

        taskService.createTaskFromDocumentAnalyzedEvent(event, event.userExternalId());

        verify(taskRepository, never()).save(any(Task.class));
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getUserId(),
                task.getSourceType().name(),
                task.getSourceId(),
                task.getTitle(),
                task.getDescription(),
                task.getRecommendedAction(),
                task.getRiskLevel(),
                task.getStatus().name(),
                task.getApprovedAt(),
                task.getRejectedAt(),
                task.getCreatedAt()
        );
    }

}