package com.zetcode;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterList {

    public static void main(String[] args) {

        var p1 = new Person(34, "Michael", Gender.MALE);
        var p2 = new Person(17, "Jane", Gender.FEMALE);
        var p3 = new Person(28, "John", Gender.MALE);
        var p4 = new Person(47, "Peter", Gender.MALE);
        var p5 = new Person(27, "Lucy", Gender.FEMALE);

        var persons = List.of(p1, p2, p3, p4, p5);

        Predicate<Person> byAge = person -> person.getAge() > 30;

        var result = persons.stream().filter(byAge)
                .collect(Collectors.toList());

        System.out.println(result);
    }
}
