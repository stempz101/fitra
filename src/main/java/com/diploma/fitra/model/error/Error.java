package com.diploma.fitra.model.error;

public enum Error {

    // NotFoundException messages
    USER_NOT_FOUND("User is not found!"),
    TRAVEL_NOT_FOUND("Travel is not found!"),
    COUNTRY_NOT_FOUND("Country is not found!"),
    CITY_NOT_FOUND("City is not found!"),
    TYPE_NOT_FOUND("Type is not found!"),
    INVITATION_NOT_FOUND("Invitation is not found!"),
    PARTICIPANT_NOT_FOUND("Participant is not found!"),
    REQUEST_NOT_FOUND("Request is not found!"),
    PLACE_NOT_FOUND("Place is not found!"),
    PLACE_REVIEW_NOT_FOUND("Place review is not found!"),
    COMMENT_NOT_FOUND("Comment is not found!"),
    ROUTE_NOT_FOUND("Route is not found!"),
    EVENT_NOT_FOUND("Event is not found!"),
    EMAIL_UPDATE_NOT_FOUND("Email update is not found!"),

    // BadRequestException messages
    TRAVEL_IS_APPROVED("Travel is already approved!"),
    TRAVEL_IS_REJECTED("Travel is already rejected!"),
    CITY_NOT_IN_COUNTRY("City is not part of specified country!"),
    NOT_SUPPORTED_LANGUAGE("The specified language is not supported!"),
    LIMIT_IS_LOWER_THAN_CURRENT_COUNT("The specified limit is lower than the current count of participants in the travel!"),
    END_DATE_MUST_BE_AFTER_START_DATE("End date must be after start date!"),
    AGE_FROM_MUST_BE_LOWER_THAN_OR_EQUAL_TO_AGE_TO("Age from must be lower than or equal to age to!"),
    CREATOR_CANT_REMOVE_HIMSELF("Creator can't remove himself!"),
    ADMIN_CANT_BE_ADDED_TO_TRAVEL("Admin can't be added to a travel!"),
    ADMIN_CANT_DELETE_HIMSELF("Admin can't delete himself!"),
    INVITATION_IS_APPROVED("Invitation is already approved!"),
    INVITATION_IS_REJECTED("Invitation is already rejected!"),
    INVITATION_IS_APPROVED_OR_REJECTED("Invitation is already approved or rejected!"),
    REQUEST_IS_APPROVED("Request is already approved!"),
    REQUEST_IS_REJECTED("Request is already rejected!"),
    REQUEST_IS_APPROVED_OR_REJECTED("Request is already approved or rejected!"),
    COMMENT_IS_NOT_FOR_SPECIFIED_REVIEW("The specified comment is not for the specified place review!"),
    END_TIME_MUST_BE_EQUAL_TO_OR_GREATER_THAN_START_TIME("The end time must be equal to or greater than start time!"),
    ROUTE_SIZE_MUST_BE_GREATER_THAN_ZERO("Route size must be greater than 0!"),
    OTP_DOES_NOT_MATCH("Otp does not match to the sent one!"),
    EMAIL_CONFIRMED("Email is already confirmed!"),
    EMAIL_IS_CURRENT("Entered email is current!"),

    // ExistenceException messages
    USER_EXISTS_WITH_EMAIL("User with specified email already exists!"),
    USER_EXISTS_IN_TRAVEL("User with specified id already exists in the travel!"),
    USER_DOES_NOT_EXIST_IN_TRAVEL("User with specified id doesn't exist in the travel!"),
    TYPE_EXISTS_WITH_NAME_EN("Type with specified name (EN) already exists!"),
    TYPE_EXISTS_WITH_NAME_UA("Type with specified name (UA) already exists!"),
    INVITATION_IS_PENDING("Invitation is already pending!"),
    REQUEST_IS_PENDING("Request is already pending!"),

    // VerificationException messages
    PASSWORD_CONFIRMATION_IS_FAILED("Password confirmation is failed!"),

    // ForbiddenException messages
    ACCESS_DENIED("Access denied!"),
    EMAIL_NOT_CONFIRMED("Email is not confirmed yet!"),

    // UnauthorizedException
    UNAUTHORIZED("User is not authorized!"),
    EMAIL_CONFIRMATION_EXPIRED("Email confirmation link has expired!"),

    // EmailException
    FAILED_TO_SEND_REGISTRATION_CONFIRMATION_LINK("Failed to send registration confirmation link to the specified email!"),

    // ConflictException
    EMAIL_CAN_NOT_BE_UPDATED("Email can not be updated because confirmation link is still valid and has not yet been clicked!"),
    PASSWORD_PREVIOUSLY_USED("Entered new password was previously used!");

    private final String message;

    Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
