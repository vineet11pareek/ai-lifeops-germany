package com.lifeops.documentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifeops.documentservice.dto.AnalyzeDocumentRequest;
import com.lifeops.documentservice.dto.CreateDocumentRequest;
import com.lifeops.documentservice.dto.DocumentAnalysisResult;
import com.lifeops.documentservice.dto.DocumentResponse;
import com.lifeops.documentservice.entity.Document;
import com.lifeops.documentservice.entity.RiskLevel;
import com.lifeops.documentservice.exception.DocumentNotFoundException;
import com.lifeops.documentservice.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final static Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final AiServiceClient aiServiceClient;

    public DocumentService(DocumentRepository documentRepository, DocumentMapper documentMapper,AiServiceClient aiServiceClient) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.aiServiceClient=aiServiceClient;
    }

    @Transactional
    public DocumentResponse createDocument(CreateDocumentRequest request){
        log.info("Creating document metadata title: {}",request.title());

        Document document = new Document(
                null,
                request.title(),
                request.content()
        );

        Document savedDocument = documentRepository.save(document);
        log.info("Document metadata created documentId: {}",savedDocument.getId());
        return  documentMapper.toResponse(savedDocument);
    }

    @Transactional(readOnly = true)
    public List<DocumentResponse> getRecentDocuments(){
        return documentRepository.findTop20ByOrderByCreatedAtDesc()
                .stream()
                .map(documentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DocumentResponse getDocumentById(UUID id){
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));
        return documentMapper.toResponse(document);
    }

    @Transactional
    public DocumentResponse analyzeDocument(AnalyzeDocumentRequest request){
        log.info("Creating document for AI analysis title: {}", request.title());

        Document document = new Document(
                null,
                request.title(),
                request.content()
        );

        document.markAnalysing();
        Document savedDocument = documentRepository.save(document);

        try{
            DocumentAnalysisResult analysisResult = aiServiceClient.analyzeDocument(
                    savedDocument.getTitle(),
                    savedDocument.getContent()
            );

            RiskLevel riskLevel = parseRiskLevel(analysisResult.riskLevel());
            savedDocument.markAnalyzed(
                    analysisResult.summary(),
                    analysisResult.deadlineText(),
                    analysisResult.requiredAction(),
                    riskLevel,
                    analysisResult.suggestedNextStep()
            );

            Document analyzedDocument = documentRepository.save(savedDocument);
            log.info("Document analyzed successfully documentId: {}",savedDocument.getId());
            return documentMapper.toResponse(savedDocument);
        } catch (Exception exception) {
            savedDocument.markFailed();
            documentRepository.save(savedDocument);
            throw exception;
        }

    }

    private RiskLevel parseRiskLevel(String value){
        try{
            return RiskLevel.valueOf(value);
        }catch (Exception exception){
            return RiskLevel.UNKNOWN;
        }

    }
}
