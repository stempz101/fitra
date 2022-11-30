package com.diploma.fitra.model.error;

public enum Error {

    // ExistenceException messages
    USER_EXISTS_WITH_EMAIL("User with entered email already exists!"),

    // VerificationException messages
    PASSWORD_CONFIRMATION_IS_FAILED("Password confirmation is failed!");

    private final String message;

    Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
