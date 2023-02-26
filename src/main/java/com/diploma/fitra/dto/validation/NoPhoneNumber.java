package com.diploma.fitra.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoPhoneNumberValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoPhoneNumber {

    String message() default "Text must not contain phone numbers";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
