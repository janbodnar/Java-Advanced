package com.zetcode;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Email;
import java.util.Set;

class Data {

    @Email
    private String email;

    public Data(String email) {

        this.email = email;
    }
}

public class EmailEx {

    public static void main(String[] args) {

        var data = new Data("johndoe#example.com");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Data>> cvs = validator.validate(data);

        for (ConstraintViolation<Data> cv : cvs) {

            System.out.printf("%s: %s%n", cv.getPropertyPath(), cv.getMessage());
        }
    }
}
