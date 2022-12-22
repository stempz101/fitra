package com.diploma.fitra.model.error;

public enum Error {

    // NotFoundException messages
    COUNTRY_NOT_FOUND("Country is not found!"),

    // BadRequestException messages
    CITY_NOT_IN_COUNTRY("City is not part of specified country!"),
    NOT_SUPPORTED_LANGUAGE("Specified language is not supported!"),

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
