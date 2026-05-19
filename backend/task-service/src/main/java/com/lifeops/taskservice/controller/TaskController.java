package com.lifeops.taskservice.controller;

import com.lifeops.taskservice.dto.ApiResponse;
import com.lifeops.taskservice.dto.TaskResponse;
import com.lifeops.taskservice.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Task proposal and approval APIs")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Get recent tasks", description = "Returns recent task records.")
    public ApiResponse<List<TaskResponse>> getRecentTasks() {
        return ApiResponse.success(
                "Tasks fetched successfully",
                taskService.getRecentTask()
        );
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending tasks", description = " Return tasks waiting for user approval.")
    public ApiResponse<List<TaskResponse>> getPendingTasks(){
        return ApiResponse.success(
                "Pending tasks fetch successfully",
                taskService.getPendingTask()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by Id", description = "Return task details for the given ID.")
    public ApiResponse<TaskResponse> getTaskById(@PathVariable UUID id){
        return ApiResponse.success(
                "Task fetched successfully",
                taskService.getTaskById(id)
        );

    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve task", description = "Approves a task waiting for user approval.")
    public ApiResponse<TaskResponse> approveTask(@PathVariable UUID id){
        return ApiResponse.success(
                "Task approved successfully",
                taskService.approveTask(id)
        );
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject task", description = "Reject a task waiting for user approval.")
    public ApiResponse<TaskResponse> rejectTask(@PathVariable UUID id){
        return ApiResponse.success(
                "Task reject successfully",
                taskService.rejectTask(id)
        );
    }

}
