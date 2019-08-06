package com.zetcode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Locale;

public class LocaleLangValidator implements ConstraintValidator<LocaleLang, String> {

    @Override
    public void initialize (LocaleLang constraintAnnotation) {
    }

    @Override
    public boolean isValid (String value, ConstraintValidatorContext context) {

        if (value == null) {
            return false;
        }

        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getDisplayLanguage().equalsIgnoreCase(value)) {
                return true;
            }
        }

        return false;
    }
}
