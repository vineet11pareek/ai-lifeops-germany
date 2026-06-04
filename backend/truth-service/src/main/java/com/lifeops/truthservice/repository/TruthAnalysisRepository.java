package com.lifeops.truthservice.repository;

import com.lifeops.truthservice.entity.TruthAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


public interface TruthAnalysisRepository extends JpaRepository<TruthAnalysis, UUID> {
    List<TruthAnalysis> findTop20ByUserExternalIdOrderByCreatedAtDesc(String userExternalId);
}
