package com.lifeops.taskservice.exception;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(UUID id) {

        super("Task not found with Id: "+id);
    }
}
