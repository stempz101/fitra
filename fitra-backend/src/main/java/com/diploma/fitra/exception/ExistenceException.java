package com.diploma.fitra.exception;

public class ExistenceException extends BadRequestException {

    private static final String DEFAULT_MESSAGE = "Entity already exists!";

    public ExistenceException() {
        super(DEFAULT_MESSAGE);
    }

    public ExistenceException(String message) {
        super(message);
    }
}
