package com.diploma.fitra.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoLinkValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoLink {

    String message() default "Text must not contain links";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
