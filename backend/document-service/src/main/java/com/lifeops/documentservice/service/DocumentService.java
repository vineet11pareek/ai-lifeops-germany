package com.lifeops.documentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifeops.documentservice.dto.CreateDocumentRequest;
import com.lifeops.documentservice.dto.DocumentResponse;
import com.lifeops.documentservice.entity.Document;
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

    public DocumentService(DocumentRepository documentRepository, DocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
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
}
