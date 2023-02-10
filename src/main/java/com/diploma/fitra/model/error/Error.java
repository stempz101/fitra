package com.diploma.fitra.model.error;

public enum Error {

    // NotFoundException messages
    USER_NOT_FOUND("User is not found!"),
    TRAVEL_NOT_FOUND("Travel is not found!"),
    COUNTRY_NOT_FOUND("Country is not found!"),
    TYPE_NOT_FOUND("Type is not found!"),
    INVITATION_NOT_FOUND("Invitation is not found!"),
    PARTICIPANT_NOT_FOUND("Participant is not found!"),

    // BadRequestException messages
    CITY_NOT_IN_COUNTRY("City is not part of specified country!"),
    NOT_SUPPORTED_LANGUAGE("The specified language is not supported!"),
    LIMIT_IS_LOWER_THAN_CURRENT_COUNT("The specified limit is lower than the current count of participants in the travel!"),
    ADMIN_CANT_BE_ADDED_TO_TRAVEL("Admin can't be added to a travel!"),
    INVITATION_IS_CONFIRMED("Invitation is already confirmed!"),
    INVITATION_IS_REJECTED("Invitation is already rejected!"),
    INVITATION_IS_CANCELLED("Invitation is already cancelled!"),
    INVITATION_IS_REJECTED_OR_CANCELLED("Invitation is already rejected or cancelled!"),
    INVITATION_IS_CONFIRMED_OR_CANCELLED("Invitation is already confirmed or cancelled!"),
    INVITATION_IS_CONFIRMED_OR_REJECTED("Invitation is already confirmed or rejected!"),

    // ExistenceException messages
    USER_EXISTS_WITH_EMAIL("User with specified email already exists!"),
    USER_EXISTS_IN_TRAVEL("User with specified id already exists in the travel!"),
    USER_DOES_NOT_EXIST_IN_TRAVEL("User with specified id doesn't exist in the travel!"),
    TYPE_EXISTS_WITH_NAME_EN("Type with specified name (EN) already exists!"),
    TYPE_EXISTS_WITH_NAME_UA("Type with specified name (UA) already exists!"),
    INVITATION_IS_WAITING("Invitation is already waiting!"),

    // VerificationException messages
    PASSWORD_CONFIRMATION_IS_FAILED("Password confirmation is failed!"),

    // ForbiddenException messages
    ACCESS_DENIED("Access denied!");

    private final String message;

    Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
