package com.lifeops.aiservice.repository;

import com.lifeops.aiservice.entity.AiQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AiQueryRepository extends JpaRepository<AiQuery, UUID> {
    List<AiQuery> findTop20ByOrderByCreatedAtDesc();
}
