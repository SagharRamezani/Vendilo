package com.saghar.marketplace.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    public record ApiErrorResponse(
            String timestamp,
            int status,
            String code,
            String message,
            String path,
            List<Detail> details
    ) {
        public record Detail(String field, String issue) {}
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ApiErrorResponse.Detail> ds = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> ds.add(new ApiErrorResponse.Detail(err.getField(), err.getDefaultMessage())));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(Instant.now().toString(), 400, "VALIDATION_ERROR", "Validation failed", req.getRequestURI(), ds));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> badRequest(IllegalArgumentException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(Instant.now().toString(), 400, "BAD_REQUEST", ex.getMessage(), req.getRequestURI(), List.of()));
    }

    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> notFound(RuntimeException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(Instant.now().toString(), 404, "NOT_FOUND", ex.getMessage(), req.getRequestURI(), List.of()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> generic(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(Instant.now().toString(), 500, "INTERNAL_ERROR", ex.getMessage(), req.getRequestURI(), List.of()));
    }
}
