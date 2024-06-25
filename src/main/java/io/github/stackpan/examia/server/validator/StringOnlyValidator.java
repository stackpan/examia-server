package io.github.stackpan.examia.server.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StringOnlyValidator implements ConstraintValidator<StringOnly, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        if (value.equals("true") || value.equals("false")) {
            return false;
        }

        try {
            Double.parseDouble(value);
            return false;
        } catch (NumberFormatException ignored) {
        }
        return true;
    }
}
