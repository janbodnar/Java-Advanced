package com.zetcode;

import java.util.Arrays;
import java.util.Comparator;

// Comparing list of objects by multiple object fields

class Person {

    private String name;
    private int age;
    private String city;

    public Person(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {

        final var sb = new StringBuilder("Person{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append(", city='").append(city).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

public class ListSortMultipleEx {

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

        persons.sort(Comparator.comparing(Person::getName)
                .thenComparing(Person::getCity)
                .thenComparing(Person::getAge));

        persons.forEach(System.out::println);
    }
}
