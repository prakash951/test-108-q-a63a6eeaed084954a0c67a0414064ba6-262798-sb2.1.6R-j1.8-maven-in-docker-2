package org.codejudge.sb.controller;

import java.util.stream.Collectors;

import org.codejudge.sb.dto.ErrorUserResponse;
import org.codejudge.sb.exception.NoFriendRequestsPendingException;
import org.codejudge.sb.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppControllerAdvice {
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorUserResponse> handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException exception) {
                ErrorUserResponse errorUserResponse = ErrorUserResponse.builder().status(
                                "failure").reason(
                                                exception.getBindingResult().getAllErrors().stream()
                                                                .map(e -> String.join(": ", e.getCode(),
                                                                                e.getDefaultMessage()))
                                                                .collect(Collectors.joining(", ")))
                                .build();
                return ResponseEntity.badRequest().body(errorUserResponse);
        }

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<ErrorUserResponse> handleUserNotFoundException(
                        UserNotFoundException exception) {
                ErrorUserResponse errorUserResponse = ErrorUserResponse.builder().status(
                                "failure").reason(
                                                exception.getMessage())
                                .build();
                return ResponseEntity.badRequest().body(errorUserResponse);
        }

        @ExceptionHandler(NoFriendRequestsPendingException.class)
        public ResponseEntity<ErrorUserResponse> handleNoFriendRequestsPendingException(
                        NoFriendRequestsPendingException exception) {
                ErrorUserResponse errorUserResponse = ErrorUserResponse.builder().status(
                                "failure").reason(
                                                exception.getMessage())
                                .build();
                return new ResponseEntity<>(errorUserResponse, HttpStatus.NOT_FOUND);
        }
}
