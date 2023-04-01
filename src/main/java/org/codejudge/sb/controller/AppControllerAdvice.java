package org.codejudge.sb.controller;

import java.util.stream.Collectors;

import org.codejudge.sb.dto.ErrorUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorUserResponse> handleInValidBindException(BindException exception) {
        ErrorUserResponse errorUserResponse = ErrorUserResponse.builder().reason(
                HttpStatus.BAD_REQUEST.value() + "").message(
                        exception.getFieldErrors().stream()
                                .map(e -> String.join(": ", e.getField(), e.getDefaultMessage()))
                                .collect(Collectors.joining(", ")))
                .build();
        return ResponseEntity.badRequest().body(errorUserResponse);
    }
}
