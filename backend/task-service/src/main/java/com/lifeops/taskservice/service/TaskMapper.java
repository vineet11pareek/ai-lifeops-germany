package com.lifeops.taskservice.service;

import com.lifeops.taskservice.dto.TaskResponse;
import com.lifeops.taskservice.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponse toResponse(Task task) {
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
