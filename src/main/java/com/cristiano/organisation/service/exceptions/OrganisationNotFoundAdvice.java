package com.cristiano.organisation.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class OrganisationNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(OrganisationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String contactNotFoundHandler(OrganisationNotFoundException ex) {
        return ex.getMessage();
    }
}
