package com.zetcode;

import java.util.Arrays;
import java.util.Comparator;

// Sorting objects by their fields

record Car(String name, int price) {}

public class ListSortByFields {

    public static void main(String[] args) {

        var cars = Arrays.asList(new Car("Volvo", 23400),
                new Car("Mazda", 13700), new Car("Porsche", 353800),
                new Car("Skoda", 8900),  new Car("Volkswagen", 19900));

        cars.sort(Comparator.comparing(Car::price));
        System.out.println(cars);

        cars.sort(Comparator.comparing(Car::name));
        System.out.println(cars);
    }
}
