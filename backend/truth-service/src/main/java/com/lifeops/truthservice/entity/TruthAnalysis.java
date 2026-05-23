package com.lifeops.truthservice.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "truth_analyses")
public class TruthAnalysis {

    @Id
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "claim_summary", columnDefinition = "TEXT")
    private String claimSummary;

    @Column(name = "trust_score")
    private Integer trustScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level")
    private RiskLevel riskLevel;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "suggested_verification_steps", columnDefinition = "TEXT")
    private String suggestedVerificationSteps;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TruthAnalysisStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected TruthAnalysis() {
    }

    public TruthAnalysis(UUID userId, String title, String content) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.status = TruthAnalysisStatus.RECEIVED;
        this.riskLevel = RiskLevel.UNKNOWN;
    }

    public void markAnalyzing() {
        this.status = TruthAnalysisStatus.ANALYZING;
    }

    public void markAnalyzed(
            String claimSummary,
            Integer trustScore,
            RiskLevel riskLevel,
            String explanation,
            String suggestedVerificationSteps
    ) {
        this.claimSummary = claimSummary;
        this.trustScore = normalizeTrustScore(trustScore);
        this.riskLevel = riskLevel;
        this.explanation = explanation;
        this.suggestedVerificationSteps = suggestedVerificationSteps;
        this.status = TruthAnalysisStatus.ANALYZED;
    }

    public void markFailed(){
        this.status = TruthAnalysisStatus.FAILED;
    }


    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = Instant.now();
    }

    private Integer normalizeTrustScore(Integer score) {
        if (score == null) {
            return null;
        }

        if (score < 0) {
            return 0;
        }

        if (score > 100) {
            return 100;
        }

        return score;
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

    public String getClaimSummary() {
        return claimSummary;
    }

    public Integer getTrustScore() {
        return trustScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getSuggestedVerificationSteps() {
        return suggestedVerificationSteps;
    }

    public TruthAnalysisStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
