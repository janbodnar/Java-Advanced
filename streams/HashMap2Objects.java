package com.zetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Stream;


public class HashMap2Objects {

    public static void main(String[] args) {

        Map<String, Integer> values = new HashMap<>();
        values.put("Ollie", 3);
        values.put("Finn", 4);
        values.put("Teddy", 6);
        values.put("Jasper", 1);
        values.put("Lucky", 2);

        Stream<Cat> cats = values.entrySet().stream().map(entry ->
            new Cat(entry.getKey(), entry.getValue())
        );

        cats.forEach(System.out::println);
    }
}

class Cat {

    private String name;
    private Integer age;

    public Cat(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Cat.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("age=" + age)
                .toString();
    }
}
