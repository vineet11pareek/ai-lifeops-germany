package com.lifeops.documentservice.service;

import com.lifeops.documentservice.dto.AnalyzeDocumentRequest;
import com.lifeops.documentservice.dto.CreateDocumentRequest;
import com.lifeops.documentservice.dto.DocumentAnalysisResult;
import com.lifeops.documentservice.dto.DocumentResponse;
import com.lifeops.documentservice.entity.Document;
import com.lifeops.documentservice.event.DocumentAnalyzedEvent;
import com.lifeops.documentservice.repository.DocumentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentMapper documentMapper;

    @Mock
    private AiServiceClient aiServiceClient;

    @Mock
    private DocumentEventPublisher documentEventPublisher;

    @InjectMocks
    private DocumentService documentService;

    @Test
    @DisplayName("Should create document successfully")
    void shouldCreateDocumentSuccessfully(){
        //Given
        CreateDocumentRequest request = new CreateDocumentRequest(
                "Letter from Finanzamt",
                "Please submit missing document"
        );

        Document savedDocument = new Document(
                null,
                request.title(),
                request.content()
        );

        DocumentResponse expectedResponse = new DocumentResponse(
                savedDocument.getId(),
                savedDocument.getTitle(),
                savedDocument.getContent(),
                null,
                null,
                null,
                "UNKNOWN",
                null,
                "RECEIVED",
                savedDocument.getCreatedAt()
        );

        //When
        when(documentRepository.save(any(Document.class))).thenReturn(savedDocument);
        when(documentMapper.toResponse(any(Document.class))).thenReturn(expectedResponse);

        DocumentResponse response = documentService.createDocument(request);

        //Then
        assertThat(response.title()).isEqualTo("Letter from Finanzamt");
        assertThat(response.content()).isEqualTo("Please submit missing document");
        assertThat(response.status()).isEqualTo("RECEIVED");

        verify(documentRepository).save(any(Document.class));
        verify(documentMapper).toResponse(savedDocument);

        verifyNoInteractions(aiServiceClient);
        verifyNoInteractions(documentEventPublisher);
    }

    @Test
    @DisplayName("Should analyzed document successfully and publish kafka event")
    void shouldAnalyzedDocumentSuccessfullyAndPublishEvent(){

        //Given
        AnalyzeDocumentRequest request = new AnalyzeDocumentRequest(
                "Letter from Finanzamt",
                "Please submit missing document"
        );

        Document document = new Document(
                null,
                request.title(),
                request.content()
        );

        DocumentAnalysisResult analysisResult = new DocumentAnalysisResult(
                "The letter asks for missing documents.",
                "25.05.2026",
                "Submit missing document",
                "MEDIUM",
                "Prepare documents and send them before the deadline"
        );

        //When
        when(documentRepository.save(any(Document.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(aiServiceClient.analyzeDocument(
                request.title(),
                request.content()
        )).thenReturn(analysisResult);

        when(documentMapper.toResponse(any(Document.class)))
                .thenAnswer(invocation -> {
                    Document analyzedDocument = invocation.getArgument(0);
                    return new DocumentResponse(
                            analyzedDocument.getId(),
                            analyzedDocument.getTitle(),
                            analyzedDocument.getContent(),
                            analyzedDocument.getSummary(),
                            analyzedDocument.getDeadlineText(),
                            analyzedDocument.getRequiredAction(),
                            analyzedDocument.getRiskLevel().name(),
                            analyzedDocument.getSuggestedNextStep(),
                            analyzedDocument.getStatus().name(),
                            analyzedDocument.getCreatedAt()
                    );
                });



        //Then
        DocumentResponse response = documentService.analyzeDocument(request);


        assertThat(response.status()).isEqualTo("ANALYZED");
        assertThat(response.summary()).isEqualTo("The letter asks for missing documents.");
        assertThat(response.deadlineText()).isEqualTo("25.05.2026");
        assertThat(response.suggestedNextStep()).isEqualTo("Prepare documents and send them before the deadline");
        assertThat(response.riskLevel()).isEqualTo("MEDIUM");
        assertThat(response.requiredAction()).isEqualTo("Submit missing document");
        //Kafka event publisher scenario
        ArgumentCaptor<DocumentAnalyzedEvent> eventCapture = ArgumentCaptor.forClass(DocumentAnalyzedEvent.class);
        verify(documentEventPublisher).publish(eventCapture.capture());

        DocumentAnalyzedEvent event = eventCapture.getValue();


        assertThat(event.documentId()).isNotNull();
        assertThat(event.title()).isEqualTo("Letter from Finanzamt");
        assertThat(event.riskLevel()).isEqualTo("MEDIUM");
        assertThat(event.status()).isEqualTo("ANALYZED");

    }

    @Test
    @DisplayName("Should return the recent documents")
    void shouldReturnRecentDocuments(){
        //Given
        Document document = new Document(
                null,
                "Letter from Finanzamt",
                "Please submit missing document"
        );

        DocumentResponse expectedResponse = new DocumentResponse(
                document.getId(),
                document.getTitle(),
                document.getContent(),
                null,
                null,
                null,
                "UNKNOWN",
                null,
                "RECEIVED",
                document.getCreatedAt()
        );

        //When
        when(documentRepository.findTop20ByOrderByCreatedAtDesc()).thenReturn(List.of(document));
        when(documentMapper.toResponse(any(Document.class))).thenReturn(expectedResponse);

        //Then
        List<DocumentResponse> response = documentService.getRecentDocuments();

        assertThat(response).hasSize(1);
        assertThat(response.get(0).title()).isEqualTo("Letter from Finanzamt");
    }


    @Test
    @DisplayName("Should return the document from ID")
    void shouldReturnDocumentById() {
        UUID documentId = UUID.randomUUID();

        Document document = new Document(
                null,
                "Letter from Finanzamt",
                "Please submit missing documents."
        );

        DocumentResponse expectedResponse = new DocumentResponse(
                document.getId(),
                document.getTitle(),
                document.getContent(),
                null,
                null,
                null,
                "UNKNOWN",
                null,
                "RECEIVED",
                document.getCreatedAt()
        );

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(documentMapper.toResponse(document)).thenReturn(expectedResponse);

        DocumentResponse response = documentService.getDocumentById(documentId);

        assertThat(response.title()).isEqualTo("Letter from Finanzamt");
    }

}