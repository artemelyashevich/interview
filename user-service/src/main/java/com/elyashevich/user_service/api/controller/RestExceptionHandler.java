package com.elyashevich.user_service.api.controller;

import com.elyashevich.user_service.api.dto.ExceptionBody;
import com.elyashevich.user_service.exception.ResourceAlreadyExistsException;
import com.elyashevich.user_service.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    private static final String NOT_FOUND_MESSAGE = "Resource was not found.";
    private static final String ALREADY_EXISTS_MESSAGE = "Resource already exists.";
    private static final String NOT_SUPPORTED_MESSAGE = "Http method with this URL not found.";
    private static final String FAILED_VALIDATION_MESSAGE = "Validation failed.";
    private static final String UNEXPECTED_ERROR_MESSAGE = "Something went wrong.";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ExceptionBody handleNotFound(ResourceNotFoundException exception) {
        return this.handleException(exception, NOT_FOUND_MESSAGE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ExceptionBody handleAlreadyExists(final ResourceAlreadyExistsException exception) {
        return this.handleException(exception, ALREADY_EXISTS_MESSAGE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ExceptionBody handleException(final HttpRequestMethodNotSupportedException exception) {
        return this.handleException(exception, NOT_SUPPORTED_MESSAGE);
    }

    @SuppressWarnings("all")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionBody handleValidation(final MethodArgumentNotValidException exception) {
        var errors = exception.getBindingResult()
            .getFieldErrors().stream()
            .collect(Collectors.toMap(
                    FieldError::getField,
                    fieldError -> fieldError.getDefaultMessage(),
                    (exist, newMessage) -> exist + " " + newMessage
                )
            );
        return new ExceptionBody(FAILED_VALIDATION_MESSAGE, errors);
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(Exception.class)
    public ExceptionBody handleException(final Exception exception) {
        return this.handleException(exception, UNEXPECTED_ERROR_MESSAGE);
    }

    private ExceptionBody handleException(final Exception exception, final String defaultMessage) {
        var message = exception.getMessage() == null ? defaultMessage : exception.getMessage();
        log.warn("{} '{}'.", defaultMessage, message);
        return new ExceptionBody(message);
    }
}
