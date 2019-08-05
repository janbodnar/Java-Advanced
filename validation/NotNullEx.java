package com.zetcode;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import java.util.Set;

class Data {

    @NotNull
    private String name;

    public Data(String name) {

        this.name = name;
    }
}

public class NotNullEx {

    public static void main(String[] args) {

        var data = new Data(null);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Data>> cvs = validator.validate(data);

        for (ConstraintViolation<Data> cv : cvs) {

            System.out.printf("%s: %s%n", cv.getPropertyPath(), cv.getMessage());
        }
    }
}
