package com.lifeops.taskservice.repository;

import com.lifeops.taskservice.entity.Task;
import com.lifeops.taskservice.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findTop20ByUserExternalIdOrderByCreatedAtDesc(String externalId);
    List<Task> findTop20ByUserExternalIdAndStatusOrderByCreatedAtDesc(String externalId,TaskStatus status);
    Optional<Task> findBySourceIdAndUserExternalId(UUID sourceId, String externalId);
}
