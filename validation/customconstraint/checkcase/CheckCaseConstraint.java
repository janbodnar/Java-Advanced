package com.zetcode;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CheckCaseConstraint {

    @NotNull
    @Size(min = 2, max = 14)
    @CheckCase(CaseMode.UPPER)
    private String licensePlate;

    public static void main(String[] args) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        validator.validateValue(CheckCaseConstraint.class, "licensePlate", "abd 123")
                .forEach(System.out::println);
    }
}
