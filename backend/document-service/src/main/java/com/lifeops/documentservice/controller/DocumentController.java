package com.lifeops.documentservice.controller;

import com.lifeops.documentservice.common.UserContextHeaders;
import com.lifeops.documentservice.dto.*;
import com.lifeops.documentservice.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
@Tag(
        name = "Documents",
        description = "Document metadata and analysis APIs"
)
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create document metadata",
            description = "Stores document title and content with RECIEVED status."
    )
    public ApiResponse<DocumentResponse> createDocument(@Valid @RequestBody CreateDocumentRequest request,
                                                        @RequestHeader(UserContextHeaders.USER_EXTERNAL_ID) String externalId,
                                                        @RequestHeader(UserContextHeaders.USER_EMAIL) String email,
                                                        @RequestHeader(UserContextHeaders.USER_NAME) String name,
                                                        @RequestHeader(UserContextHeaders.AUTH_PROVIDER) String provider) {

        AuthenticatedUserContext userContext = getUserContext(externalId, name, email, provider);
        return ApiResponse.success("Document created successfully", documentService.createDocument(request));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get document by ID",
            description = "Returns document details for the given ID"
    )
    public ApiResponse<DocumentResponse> getDocumentById(@PathVariable UUID id,
                                                         @RequestHeader(UserContextHeaders.USER_EXTERNAL_ID) String externalId,
                                                         @RequestHeader(UserContextHeaders.USER_EMAIL) String email,
                                                         @RequestHeader(UserContextHeaders.USER_NAME) String name,
                                                         @RequestHeader(UserContextHeaders.AUTH_PROVIDER) String provider) {

        AuthenticatedUserContext userContext = getUserContext(externalId, name, email, provider);
        return ApiResponse.success(
                "Document fetched successfully",
                documentService.getDocumentById(id)
        );
    }

    @GetMapping
    @Operation(
            summary = "Get recent documents",
            description = "Returns recent documents records"
    )
    public ApiResponse<List<DocumentResponse>> getRecentDocuments(@RequestHeader(UserContextHeaders.USER_EXTERNAL_ID) String externalId,
                                                                  @RequestHeader(UserContextHeaders.USER_EMAIL) String email,
                                                                  @RequestHeader(UserContextHeaders.USER_NAME) String name,
                                                                  @RequestHeader(UserContextHeaders.AUTH_PROVIDER) String provider) {
        AuthenticatedUserContext userContext = getUserContext(externalId, email, name, provider);

        return ApiResponse.success(
                "Documents fetched successfully",
                documentService.getRecentDocuments()
        );
    }

    @PostMapping("/analyze")
    @Operation(
            summary = "Analyze document text",
            description = "Stores document text and analyzes it using AI to extract summary, deadline, action, risk, and next step."
    )
    public ApiResponse<DocumentResponse> analyzeDocument(@Valid @RequestBody AnalyzeDocumentRequest request,
                                                         @RequestHeader(UserContextHeaders.USER_EXTERNAL_ID) String externalId,
                                                         @RequestHeader(UserContextHeaders.USER_EMAIL) String email,
                                                         @RequestHeader(UserContextHeaders.USER_NAME) String name,
                                                         @RequestHeader(UserContextHeaders.AUTH_PROVIDER) String provider) {
        AuthenticatedUserContext userContext = getUserContext(externalId, name, email, provider);

        return ApiResponse.success(
                "Document analyzed successfully",
                documentService.analyzeDocument(request)
        );
    }

    private AuthenticatedUserContext getUserContext(String externalId, String name, String email, String provider) {
        return new AuthenticatedUserContext(
                externalId,
                email,
                name,
                provider
        );
    }
}
