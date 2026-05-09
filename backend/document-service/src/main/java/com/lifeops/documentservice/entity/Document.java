package com.lifeops.documentservice.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "deadline_text",columnDefinition = "TEXT")
    private String deadlineText;

    @Column(name = "required_action", columnDefinition = "TEXT")
    private String requiredAction;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level")
    private RiskLevel riskLevel;

    @Column(name = "suggested_next_step", columnDefinition = "TEXT")
    private String suggestedNextStep;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Document() {

    }

    public Document(UUID userId, String title, String content) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.status = DocumentStatus.RECEIVED;
        this.riskLevel = RiskLevel.UNKNOWN;
    }

    public void markAnalysing(){
        this.status = DocumentStatus.ANALYZING;
    }

    public void markAnalyzed(
            String summary,
            String deadlineText,
            String requiredAction,
            RiskLevel riskLevel,
            String suggestedNextStep
    ) {
        this.summary = summary;
        this.deadlineText = deadlineText;
        this.requiredAction = requiredAction;
        this.riskLevel = riskLevel;
        this.suggestedNextStep = suggestedNextStep;
        this.status = DocumentStatus.ANALYZED;
    }

    public void markFailed() {
        this.status = DocumentStatus.FAILED;
    }

    public void updateAnalysis(
            String summary,
            String deadlineText,
            String requiredAction,
            RiskLevel riskLevel,
            String suggestedNextStep
    ) {
        markAnalyzed(
                summary,
                deadlineText,
                requiredAction,
                riskLevel,
                suggestedNextStep
        );
    }

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSummary() {
        return summary;
    }

    public String getDeadlineText() {
        return deadlineText;
    }

    public String getRequiredAction() {
        return requiredAction;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public String getSuggestedNextStep() {
        return suggestedNextStep;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
