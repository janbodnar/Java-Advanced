package com.zetcode;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

class Data {

    @Past
    private LocalDate date;

    public Data(LocalDate date) {

        this.date = date;
    }
}

public class PastDate {

    public static void main(String[] args) {

        var data = new Data(LocalDate.of(2030, 12, 10));

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Data>> cvs = validator.validate(data);

        System.out.println(cvs.size());

        for (ConstraintViolation<Data> cv : cvs) {

            System.out.printf("%s: %s%n", cv.getPropertyPath(), cv.getMessage());
        }
    }
}
