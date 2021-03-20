package com.zetcode;

import java.util.Arrays;
import java.util.Comparator;

// Sorting list of objects by multiple object fields

public class ListSortMultiple {

    public static void main(String[] args) {

        var persons = Arrays.asList(
                new Person("Peter", 23, "New York"),
                new Person("Sarah", 13, "Las Vegas"),
                new Person("Lucy", 33, "Toronto"),
                new Person("Sarah", 21, "New York"),
                new Person("Tom", 18, "Toronto"),
                new Person("Robert", 23, "San Diego"),
                new Person("Lucy", 23, "Los Angeles"),
                new Person("Sam", 36, "Dallas"),
                new Person("Elisabeth", 31, "New York"),
                new Person("Ruth", 29, "New York"),
                new Person("Sarah", 41, "New York")
        );

        persons.sort(Comparator.comparing(Person::name)
                .thenComparing(Person::city)
                .thenComparing(Person::age));

        persons.forEach(System.out::println);
    }
}

record Person(String name, int age, String city) {}
