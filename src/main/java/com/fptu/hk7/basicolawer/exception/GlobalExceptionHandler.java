package com.fptu.hk7.basicolawer.exception;

import com.fptu.hk7.basicolawer.dto.Response.ApiResponse;
import io.jsonwebtoken.io.IOException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<?> handlingUncategorizedException(RuntimeException exception){
        ApiResponse<Void> apiResponse = new ApiResponse<>();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<?> handlingIOException(IOException exception){
        ApiResponse<Void> apiResponse = new ApiResponse<>();

        apiResponse.setCode(ErrorCode.IO_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.IO_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AuthException.class)
    ResponseEntity<?> handlingAuthentication(AuthException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<?> handlingAppException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<Void> apiResponse = new ApiResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<?> handlingValidation(MethodArgumentNotValidException exception){
        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage).toList();

        ApiResponse<Void> apiResponse = new ApiResponse<>();

        apiResponse.setCode(ErrorCode.FAIL_VALIDATION_DATA.getCode());
        apiResponse.setMessage(ErrorCode.FAIL_VALIDATION_DATA.getMessage());

        return ResponseEntity.badRequest()
                .body(ApiResponse.<List<String>>builder()
                        .message(ErrorCode.FAIL_VALIDATION_DATA.getMessage())
                        .code(ErrorCode.FAIL_VALIDATION_DATA.getCode())
                        .result(errors)
                        .build()
                );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    public ResponseEntity<?> handleNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


}
