package com.cristiano.organisation.service.exceptions;

public class DeleteConnectedContactException extends RuntimeException {
    public DeleteConnectedContactException(Long id) {
        super("Could not delete contact " + id + ". Remove from organisation first.");
    }
}
