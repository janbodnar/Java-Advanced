package com.zetcode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Person {

    private int age;
    private String name;

    public Person(int age, String name) {

        this.age = age;
        this.name = name;
    }

    public int getAge() {

        return age;
    }

    @Override
    public String toString() {
        return "Age: " + age + " Name: " + name;
    }
}

public class ListSort {

    public static void main(String[] args) {

        List<Person> persons = createList();

        persons.sort(Comparator.comparing(Person::getAge).reversed());

        System.out.println(persons);
    }

    private static List<Person> createList() {

        List<Person> persons = new ArrayList<>();

        persons.add(new Person(17, "Jane"));
        persons.add(new Person(32, "Peter"));
        persons.add(new Person(47, "Patrick"));
        persons.add(new Person(22, "Mary"));
        persons.add(new Person(39, "Robert"));
        persons.add(new Person(54, "Greg"));

        return persons;
    }
}
