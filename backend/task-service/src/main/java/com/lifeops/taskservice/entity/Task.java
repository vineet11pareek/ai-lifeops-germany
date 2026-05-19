package com.lifeops.taskservice.entity;

import com.lifeops.taskservice.exception.InvalidTaskStateException;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private TaskSourceType sourceType;

    @Column(name = "source_id")
    private UUID sourceId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "recommended_action", columnDefinition = "TEXT")
    private String recommendedAction;

    @Column(name = "risk_level")
    private String riskLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @Column(name = "rejected_at")
    private Instant rejectedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Task() {
    }

    public Task(
            UUID userId,
            TaskSourceType sourceType,
            UUID sourceId,
            String title,
            String description,
            String recommendedAction,
            String riskLevel
    ) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.title = title;
        this.description = description;
        this.recommendedAction = recommendedAction;
        this.riskLevel = riskLevel;
        this.status = TaskStatus.WAITING_FOR_APPROVAL;
    }

    public void approve(){
        if(this.status != TaskStatus.WAITING_FOR_APPROVAL){
            throw new InvalidTaskStateException("Only tasks waiting for approval can be approved");
        }
        this.status = TaskStatus.APPROVED;
        this.approvedAt = Instant.now();
    }

    public void reject(){
        if(this.status != TaskStatus.WAITING_FOR_APPROVAL){
            throw new InvalidTaskStateException("Only tasks waiting for approval can be rejected");
        }
        this.status = TaskStatus.REJECTED;
        this.rejectedAt = Instant.now();
    }

    public void cancel(){
        this.status = TaskStatus.CANCELLED;
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

    public TaskSourceType getSourceType() {
        return sourceType;
    }

    public UUID getSourceId() {
        return sourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public Instant getRejectedAt() {
        return rejectedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
