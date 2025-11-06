package org.example.testwallet.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(IllegalArgumentException ex) {
    ErrorResponse errorResponse = ErrorResponse.create(
        ex,
        HttpStatus.NOT_FOUND,
        ex.getMessage()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
    // Обработка: невалидный JSON, невалидный enum и т.д.
    String message = "Invalid request format";

    if (ex.getMessage() != null && ex.getMessage().contains("OperationType")) {
      message = "Invalid operation type. Allowed values: DEPOSIT, WITHDRAW";
    } else if (ex.getMessage() != null && ex.getMessage().contains("JSON")) {
      message = "Invalid JSON format";
    }

    ErrorResponse errorResponse = ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, message);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    // Обработка: невалидный UUID в path variable
    String message = "Invalid parameter format";

    if (ex.getName().equals("walletId") && ex.getRequiredType() == UUID.class) {
      message = "Invalid UUID format for walletId";
    }

    ErrorResponse errorResponse = ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, message);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
    // Обработка ошибок валидации @Valid
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(FieldError::getDefaultMessage)
        .findFirst()
        .orElse("Validation failed");

    ErrorResponse errorResponse = ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, message);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}

