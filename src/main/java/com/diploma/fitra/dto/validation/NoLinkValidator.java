package com.diploma.fitra.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NoLinkValidator implements ConstraintValidator<NoLink, String> {

    @Override
    public void initialize(NoLink constraintAnnotation) {

    }

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pattern = Pattern.compile("\\b(?:https?|ftp)?(://)?(\\S+\\.)+\\S{2,}\\b");
        return !pattern.matcher(text).find();
    }
}
