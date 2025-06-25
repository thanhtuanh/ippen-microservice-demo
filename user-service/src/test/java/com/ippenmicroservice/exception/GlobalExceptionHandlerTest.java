package com.ippenmicroservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleResourceNotFound_shouldReturn404() {
        // Arrange
        String errorMessage = "Order not found";
        ResourceNotFoundException ex = new ResourceNotFoundException(errorMessage);

        // Act
        ResponseEntity<Object> response = exceptionHandler.handleResourceNotFound(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isInstanceOfAny(java.util.Map.class);
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> body = (java.util.Map<String, Object>) response.getBody();
        assertThat(body).containsEntry("status", 404);
        assertThat(body).containsEntry("message", errorMessage);
    }

    @Test
    void handleAllExceptions_shouldReturn500() {
        // Arrange
        Exception ex = new RuntimeException("Unexpected error");

        // Act
        ResponseEntity<Object> response = exceptionHandler.handleAllExceptions(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isInstanceOfAny(java.util.Map.class);
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> body = (java.util.Map<String, Object>) response.getBody();
        assertThat(body).containsEntry("status", 500);
        assertThat(body).containsEntry("message", "Internal server error");
    }
}