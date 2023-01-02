package com.diploma.fitra.model.error;

public enum Error {

    // NotFoundException messages
    USER_NOT_FOUND("User is not found!"),
    TRAVEL_NOT_FOUND("Travel is not found!"),
    COUNTRY_NOT_FOUND("Country is not found!"),
    TYPE_NOT_FOUND("Type is not found!"),

    // BadRequestException messages
    CITY_NOT_IN_COUNTRY("City is not part of specified country!"),
    NOT_SUPPORTED_LANGUAGE("Specified language is not supported!"),

    // ExistenceException messages
    USER_EXISTS_WITH_EMAIL("User with specified email already exists!"),
    TYPE_EXISTS_WITH_NAME_EN("Type with specified name (EN) already exists!"),
    TYPE_EXISTS_WITH_NAME_UA("Type with specified name (UA) already exists!"),

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
