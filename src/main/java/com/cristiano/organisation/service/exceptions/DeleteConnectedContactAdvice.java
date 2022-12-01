package com.cristiano.organisation.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DeleteConnectedContactAdvice {
    @ResponseBody
    @ExceptionHandler(DeleteConnectedContactException.class)
    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    String deleteConnectedContactHandler(DeleteConnectedContactException ex) {
        return ex.getMessage();
    }
}
