package com.lifeops.documentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnalyzeDocumentRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255,message = "Title must not exceed 255 characters")
        String title,

        @NotBlank(message = "Content is required")
        @Size(max = 20000, message = "Content must not exceed 20000 characters")
        String content
) {
}
