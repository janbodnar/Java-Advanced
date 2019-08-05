package com.zetcode;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;

class Data {

    private List<@Positive Integer> values = List.of(2, 3, 4, 5, -4, 3);
}

public class PositiveValues {

    public static void main(String[] args) {

        var data = new Data();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Data>> cvs = validator.validate(data);

        System.out.println(cvs.size());

        for (ConstraintViolation<Data> cv : cvs) {

            System.out.printf("%s: %s%n", cv.getPropertyPath(), cv.getMessage());
        }
    }
}
