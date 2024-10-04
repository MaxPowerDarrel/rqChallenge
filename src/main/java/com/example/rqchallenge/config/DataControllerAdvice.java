package com.example.rqchallenge.config;

import com.example.rqchallenge.data.DataRetrievalException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.example.rqchallenge.config.MDCFilter.CORRELATION_ID;

@ControllerAdvice
@Slf4j
public class DataControllerAdvice extends ResponseEntityExceptionHandler {
    /**
     * Handle a database error and concealing data from the consumer
     */
    @ExceptionHandler(value = {DataRetrievalException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        String correlationId;
        try {
            correlationId = MDC.get(CORRELATION_ID);
        } catch (IllegalArgumentException e) {
            correlationId = "NOT FOUND";
        }
        var bodyOfResponse = "There was an error with your request, correlationId is " + correlationId;
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
