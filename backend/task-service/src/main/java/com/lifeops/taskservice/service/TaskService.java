package com.lifeops.taskservice.service;

import com.lifeops.taskservice.dto.TaskResponse;
import com.lifeops.taskservice.entity.Task;
import com.lifeops.taskservice.entity.TaskStatus;
import com.lifeops.taskservice.exception.TaskNotFoundException;
import com.lifeops.taskservice.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private TaskRepository taskRepository;
    private TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getRecentTask(){
        return taskRepository.findTop20ByOrderByCreatedAtDesc()
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getPendingTask(){
        return taskRepository.findTop20ByStatusOrderByCreatedAtDesc(TaskStatus.WAITING_FOR_APPROVAL)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID id){
        Task task =  taskRepository.findBySourceId(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toResponse(task);
    }
}
