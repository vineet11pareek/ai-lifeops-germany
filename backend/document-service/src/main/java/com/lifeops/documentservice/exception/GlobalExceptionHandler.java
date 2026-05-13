package com.lifeops.documentservice.exception;

import com.lifeops.documentservice.common.CorrelationConstants;
import com.lifeops.documentservice.dto.error.ApiErrorResponse;
import com.lifeops.documentservice.dto.error.FieldErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleDocumentNotFoundException(DocumentNotFoundException exception, HttpServletRequest request){
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                getCorrelationId(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI(),
                List.of()

        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotFoundException(MethodArgumentNotValidException exception,HttpServletRequest request){
        List<FieldErrorResponse> fieldErrorResponses = exception.getFieldErrors()
                .stream()
                .map(error-> new FieldErrorResponse(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                getCorrelationId(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                request.getRequestURI(),
                fieldErrorResponses

        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenricException(Exception exception,HttpServletRequest request){
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                getCorrelationId(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Something went wrong. Please try again later.",
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(DocumentAnalysisException.class)
    public ResponseEntity<ApiErrorResponse> handleDocumentAnalysisException(DocumentAnalysisException exception,HttpServletRequest request){
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                getCorrelationId(),
                HttpStatus.BAD_GATEWAY.value(),
                HttpStatus.BAD_GATEWAY.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    private String getCorrelationId(){
        return MDC.get(CorrelationConstants.CORRELATION_ID_MDC_KEY);
    }
}
