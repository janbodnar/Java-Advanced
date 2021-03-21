package com.zetcode;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

// sorting a list of objects by multiple fields

public class ListSortMultiple {

    public static void main(String[] args) {

        var persons = List.of(
                new Person("Peter", LocalDate.of(1998, 5, 11), "New York"),
                new Person("Sarah", LocalDate.of(2008, 8, 21), "Las Vegas"),
                new Person("Lucy", LocalDate.of(1988, 12, 10), "Toronto"),
                new Person("Sarah", LocalDate.of(2000, 9, 19), "New York"),
                new Person("Tom", LocalDate.of(2004, 8, 30), "Toronto"),
                new Person("Robert", LocalDate.of(2008, 11, 1), "San Diego"),
                new Person("Lucy", LocalDate.of(2008, 10, 5), "Los Angeles"),
                new Person("Sam", LocalDate.of(1986, 6, 17), "Dallas"),
                new Person("Elisabeth", LocalDate.of(1985, 7, 12), "New York"),
                new Person("Ruth", LocalDate.of(1994, 4, 28), "New York"),
                new Person("Sarah", LocalDate.of(1977, 11, 30), "New York")
        );

        var sorted = persons.stream().sorted(Comparator.comparing(Person::age)
                .thenComparing(Person::name).reversed());

        sorted.forEach(System.out::println);
    }
}

record Person(String name, LocalDate dateOfBirth, String city) {

    public int age() {

        return Period.between(dateOfBirth(), LocalDate.now()).getYears();
    }
}
