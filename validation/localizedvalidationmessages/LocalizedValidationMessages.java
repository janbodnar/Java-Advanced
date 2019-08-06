package com.zetcode;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Locale;

public class LocalizedValidationMessages {

    @NotNull(message = "{licensePlate.notnull}")
    @Size(min = 2, max = 14, message = "{licensePlate.size}")
    private String licensePlate;

    public static void main(String[] args) {

        var locale = new Locale("sk", "SK");
        Locale.setDefault(locale);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        validator.validateValue(LocalizedValidationMessages.class, "licensePlate", null)
                .forEach(System.out::println);
    }
}

