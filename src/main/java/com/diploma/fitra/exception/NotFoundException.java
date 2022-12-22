package com.diploma.fitra.exception;

public class NotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Entity is not found!";

    public NotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public NotFoundException(String message) {
        super(message);
    }
}
