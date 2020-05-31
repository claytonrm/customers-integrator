package com.compasso.customersintegrator.exception;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException e, final HttpHeaders headers,
                                                               final HttpStatus status, final WebRequest request) {
        final Set<String> errors = e.getBindingResult().getFieldErrors().stream().map(err -> err.getDefaultMessage()).collect(Collectors.toSet());
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, errors));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(InstanceAlreadyExistsException.class)
    public ResponseEntity<Object> handleInstanceAlreadyExistsException(final InstanceAlreadyExistsException e) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, new HashSet<>(Arrays.asList(e.getMessage()))));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InstanceNotFoundException.class)
    public ResponseEntity<Object> handleInstanceNotFoundException(final InstanceNotFoundException e) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, new HashSet<>(Arrays.asList(e.getMessage()))));
    }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(final IllegalStateException e) {
        return buildResponseEntity(new ApiError(HttpStatus.PRECONDITION_FAILED, new HashSet<>(Arrays.asList(e.getMessage()))));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException e) {
        final Set<String> errors = e.getConstraintViolations().stream().map(constraintViolation -> constraintViolation.getMessageTemplate()).collect(Collectors.toSet());
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, errors));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException e) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, new HashSet<>(Arrays.asList(e.getMessage()))));
    }

    private ResponseEntity<Object> buildResponseEntity(final ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
