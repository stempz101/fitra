package com.diploma.fitra.exception;

public class VerificationException extends BadRequestException {

    private static final String DEFAULT_MESSAGE = "Verification is failed!";

    public VerificationException() {
        super(DEFAULT_MESSAGE);
    }

    public VerificationException(String message) {
        super(message);
    }
}
