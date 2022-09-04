package com.example.codeuploader.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.ServiceUnavailableException;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(
            value = HttpStatus.NOT_FOUND, reason = "Resource not found")
    @ExceptionHandler(ResourceNotFoundException.class)
    public void handleResourceNotFoundException(ResourceNotFoundException e) {

    }

    @ResponseStatus(
            value = HttpStatus.BAD_REQUEST,
            reason = "Recieved invalid request")
    @ExceptionHandler(InvalidRequestException.class)
    public void handleInvalidRequestException(InvalidRequestException e) {

    }

    @ResponseStatus(
            value = HttpStatus.GATEWAY_TIMEOUT,
            reason = "Upstream Service Failed to Respond")
    @ExceptionHandler(ServiceUnavailableException.class)
    public void handleServiceUnavailableException(ServiceUnavailableException e) {

    }


}
