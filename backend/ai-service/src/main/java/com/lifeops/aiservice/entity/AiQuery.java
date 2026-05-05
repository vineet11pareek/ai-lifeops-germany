package com.lifeops.aiservice.entity;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ai_queries")
public class AiQuery {

    private static final Logger log = LoggerFactory.getLogger(AiQuery.class);

    @Id
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AiQueryStatus status;

    private String provider;

    private String model;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public AiQuery() {
    }

    public AiQuery(UUID userId, String question) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.question = question;
        this.status = AiQueryStatus.CREATED;
    }

    public void markProcessing(){
        this.status = AiQueryStatus.PROCESSING;
        this.createdAt=Instant.now();
    }

    public void markCompleted(String answer, String provider, String model){
        this.answer = answer;
        this.provider=provider;
        this.model = model;
        this.status=AiQueryStatus.COMPLETED;
    }

    public void markFailed(String errorMessage){
        this.status=AiQueryStatus.FAILED;
        this.errorMessage=errorMessage;
    }

    @PrePersist
    public void prePersist(){
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public AiQueryStatus getStatus() {
        return status;
    }

    public String getProvider() {
        return provider;
    }

    public String getModel() {
        return model;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
