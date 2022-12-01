package com.cristiano.organisation.service.exceptions;

public class OrganisationNotFoundException extends RuntimeException {
    public OrganisationNotFoundException(Long id) {
        super("Could not find organisation " + id);
    }
}
