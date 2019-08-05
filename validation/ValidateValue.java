package com.zetcode;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Min;

public class ValidateValue {

    @Min(5)
    private String word;

    public static void main(String[] args) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        validator.validateValue(ValidateValue.class, "word", "cup")
                .forEach(System.out::println);
    }
}
