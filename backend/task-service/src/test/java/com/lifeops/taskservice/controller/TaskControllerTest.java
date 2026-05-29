package com.lifeops.taskservice.controller;

import com.lifeops.taskservice.common.UserContextHeaders;
import com.lifeops.taskservice.dto.TaskResponse;
import com.lifeops.taskservice.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void shouldReturnRecentTasks() throws Exception {
        TaskResponse task = sampleTask("WAITING_FOR_APPROVAL");

        when(taskService.getRecentTask()).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks")
                        .header(UserContextHeaders.USER_EXTERNAL_ID,UUID.randomUUID())
                        .header(UserContextHeaders.USER_NAME,"Test User")
                        .header(UserContextHeaders.USER_EMAIL,"User@test.com")
                        .header(UserContextHeaders.AUTH_PROVIDER,"GOOGLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Tasks fetched successfully"))
                .andExpect(jsonPath("$.data[0].status").value("WAITING_FOR_APPROVAL"));
    }

    @Test
    void shouldReturnPendingTasks() throws Exception {
        TaskResponse task = sampleTask("WAITING_FOR_APPROVAL");

        when(taskService.getPendingTask()).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks/pending")
                        .header(UserContextHeaders.USER_EXTERNAL_ID,UUID.randomUUID())
                        .header(UserContextHeaders.USER_NAME,"Test User")
                        .header(UserContextHeaders.USER_EMAIL,"User@test.com")
                        .header(UserContextHeaders.AUTH_PROVIDER,"GOOGLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Pending tasks fetched successfully"))
                .andExpect(jsonPath("$.data[0].status").value("WAITING_FOR_APPROVAL"));
    }

    @Test
    void shouldReturnTaskById() throws Exception {
        UUID taskId = UUID.randomUUID();
        TaskResponse task = sampleTask(taskId, "WAITING_FOR_APPROVAL");

        when(taskService.getTaskById(taskId)).thenReturn(task);

        mockMvc.perform(get("/api/tasks/{id}", taskId)
                        .header(UserContextHeaders.USER_EXTERNAL_ID,UUID.randomUUID())
                        .header(UserContextHeaders.USER_NAME,"Test User")
                        .header(UserContextHeaders.USER_EMAIL,"User@test.com")
                        .header(UserContextHeaders.AUTH_PROVIDER,"GOOGLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(taskId.toString()));
    }

    @Test
    void shouldApproveTask() throws Exception {
        UUID taskId = UUID.randomUUID();
        TaskResponse task = sampleTask(taskId, "APPROVED");

        when(taskService.approveTask(taskId)).thenReturn(task);

        mockMvc.perform(post("/api/tasks/{id}/approve", taskId)
                        .header(UserContextHeaders.USER_EXTERNAL_ID,UUID.randomUUID())
                        .header(UserContextHeaders.USER_NAME,"Test User")
                        .header(UserContextHeaders.USER_EMAIL,"User@test.com")
                        .header(UserContextHeaders.AUTH_PROVIDER,"GOOGLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task approved successfully"))
                .andExpect(jsonPath("$.data.status").value("APPROVED"));
    }

    @Test
    void shouldRejectTask() throws Exception {
        UUID taskId = UUID.randomUUID();
        TaskResponse task = sampleTask(taskId, "REJECTED");

        when(taskService.rejectTask(taskId)).thenReturn(task);

        mockMvc.perform(post("/api/tasks/{id}/reject", taskId)
                        .header(UserContextHeaders.USER_EXTERNAL_ID,UUID.randomUUID())
                        .header(UserContextHeaders.USER_NAME,"Test User")
                        .header(UserContextHeaders.USER_EMAIL,"User@test.com")
                        .header(UserContextHeaders.AUTH_PROVIDER,"GOOGLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task rejected successfully"))
                .andExpect(jsonPath("$.data.status").value("REJECTED"));
    }

    private TaskResponse sampleTask(String status) {
        return sampleTask(UUID.randomUUID(), status);
    }

    private TaskResponse sampleTask(UUID taskId, String status) {
        return new TaskResponse(
                taskId,
                null,
                "DOCUMENT_ANALYSIS",
                UUID.randomUUID(),
                "Review required action",
                "Document requires user action.",
                "Submit missing documents.",
                "MEDIUM",
                status,
                "APPROVED".equals(status) ? Instant.now() : null,
                "REJECTED".equals(status) ? Instant.now() : null,
                Instant.now()
        );
    }
}