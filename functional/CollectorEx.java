package com.zetcode;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collector;

public class CollectorEx {

    public static void main(String[] args) {

        List<User> persons = List.of(
                new User("Robert", 28),
                new User("Peter", 37),
                new User("Lucy", 23),
                new User("David", 28));

        Collector<User, StringJoiner, String> personNameCollector =
                Collector.of(
                        () -> new StringJoiner(" | "),          // supplier
                        (j, p) -> j.add(p.getName()),  // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner
                        StringJoiner::toString);                // finisher

        String names = persons
                .stream()
                .collect(personNameCollector);

        System.out.println(names);
    }

}

class User {

    private String name;
    private int age;

    User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {

        return this.name;
    }

    @Override
    public String toString() {

        return String.format("%s is %d years old", name, age);
    }
}
