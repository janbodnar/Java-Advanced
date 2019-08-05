package com.zetcode;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class SimpleValidation {

    private static Validator validator;

    public static void main(String[] args) {

        var car1 = new Car("Volvo", 29000);
        var car2 = new Car("Skoda", 900);
        var car3 = new Car(null, 29000);
        var car4 = new Car("Cit", 21000);
        var car5 = new Car("Bentley", 8000000);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        validate(car1);
        validate(car2);
        validate(car3);
        validate(car4);
        validate(car5);
    }

    private static void validate(Car car) {

        Set<ConstraintViolation<Car>> cvs = validator.validate(car);

        for (ConstraintViolation<Car> cv : cvs) {
            System.out.printf("%s: %s%n", cv.getPropertyPath(), cv.getMessage());
        }
    }
}
