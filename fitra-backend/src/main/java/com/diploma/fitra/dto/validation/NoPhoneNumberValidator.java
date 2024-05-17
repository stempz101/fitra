package com.diploma.fitra.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NoPhoneNumberValidator implements ConstraintValidator<NoPhoneNumber, String> {

    @Override
    public void initialize(NoPhoneNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pattern = Pattern.compile("\\b(\\+?\\d{1,3}\\s?)?(\\(?\\d{2,3}\\)?[\\s.-]?)?\\d{3}[\\s.-]?\\d{4}\\b");
        return !pattern.matcher(text).find();
    }
}
