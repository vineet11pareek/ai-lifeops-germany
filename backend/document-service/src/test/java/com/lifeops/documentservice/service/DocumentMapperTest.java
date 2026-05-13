package com.lifeops.documentservice.service;

import com.lifeops.documentservice.dto.DocumentResponse;
import com.lifeops.documentservice.entity.Document;
import com.lifeops.documentservice.entity.RiskLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DocumentMapperTest {
    private final DocumentMapper documentMapper = new DocumentMapper();

    @Test
    @DisplayName("Document should map to response")
    void shouldMapDocumentToResponse(){
        //Given
        Document document = new Document(
                null,
                "Letter from Finanzamt",
                "Please submit missing documents by 15.06.2026."
        );

        document.markAnalyzed(
                "The letter asks for missing documents.",
                "15.06.2026",
                "Submit missing document",
                RiskLevel.MEDIUM,
                "Prepare and send the missing document before the deadline"
        );

        //When
        DocumentResponse response = documentMapper.toResponse(document);

        //Then
        assertNotNull(response);
        assertThat(response.deadlineText()).isEqualTo("15.06.2026");
        assertThat(response.title()).isEqualTo("Letter from Finanzamt");
        assertThat(response.requiredAction()).isEqualTo("Submit missing document");
        assertThat(response.riskLevel()).isEqualTo("MEDIUM");
        assertThat(response.status()).isEqualTo("ANALYZED");

    }

}