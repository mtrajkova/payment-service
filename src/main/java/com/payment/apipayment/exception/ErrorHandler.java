package com.payment.apipayment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({CustomerCreationException.class, SubscriptionCreationException.class, ChargeCreationException.class})
    protected ResponseEntity<Object> handleCreationException(RuntimeException e, WebRequest request) {
        return handleExceptionInternal(e, e.getMessage(), null, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({TokenMissingException.class})
    protected ResponseEntity<Object> handleMissingToken(RuntimeException e, WebRequest request) {
        return handleExceptionInternal(e, e.getMessage(), null, HttpStatus.UNAUTHORIZED, request);
    }
}

