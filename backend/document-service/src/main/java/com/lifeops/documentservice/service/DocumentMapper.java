package com.lifeops.documentservice.service;

import com.lifeops.documentservice.dto.DocumentResponse;
import com.lifeops.documentservice.entity.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {
    public DocumentResponse toResponse(Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getTitle(),
                document.getContent(),
                document.getSummary(),
                document.getDeadlineText(),
                document.getRequiredAction(),
                document.getRiskLevel() != null ? document.getRiskLevel().name() : null,
                document.getSuggestedNextStep(),
                document.getStatus().name(),
                document.getCreatedAt()
        );
    }
}
