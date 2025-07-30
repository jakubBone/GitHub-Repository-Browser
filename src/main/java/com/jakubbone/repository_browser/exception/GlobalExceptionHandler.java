package com.jakubbone.repository_browser.exception;

import com.jakubbone.repository_browser.dto.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ErrorResponse> handleOwnerNotFound(final HttpClientErrorException.NotFound e) {
        log.warn("Handling owner not found exception: {}", e.getMessage());
        final var errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Owner not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidOwnerException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOwner(final InvalidOwnerException e) {
        log.warn("Handling invalid owner exception: {}", e.getMessage());
        final var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(final Exception e) {
        log.error("Unexpected error occurred", e);
        final var errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
