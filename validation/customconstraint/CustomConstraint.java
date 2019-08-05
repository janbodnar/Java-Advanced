package com.zetcode;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class CustomConstraint {

    @LocaleLang
    private String lang;

    public static void main(String[] args) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        validator.validateValue(CustomConstraint.class, "lang", "english")
                .forEach(System.out::println);

        validator.validateValue(CustomConstraint.class, "lang", "scottish")
                .forEach(System.out::println);
    }

}
