package com.lifeops.taskservice.exception;

import com.lifeops.taskservice.common.CorrelationConstants;
import com.lifeops.taskservice.dto.ApiResponse;
import com.lifeops.taskservice.dto.error.ApiErrorResponse;
import com.lifeops.taskservice.dto.error.FieldErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
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

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTaskNotFoundException(TaskNotFoundException exception, HttpServletRequest request){
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
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,HttpServletRequest request){
        List<FieldErrorResponse> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
                .toList();

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                getCorrelationId(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                request.getRequestURI(),
                errors
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
                "Something went wrong, Please try again later",
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(InvalidTaskStateException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidTaskStateException(InvalidTaskStateException exception, HttpServletRequest request){
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                getCorrelationId(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    private String getCorrelationId(){
        return MDC.get(CorrelationConstants.CORRELATION_ID_MDC_KEY);
    }
}
