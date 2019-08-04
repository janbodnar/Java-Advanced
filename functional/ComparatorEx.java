package com.zetcode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class Person {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {

        this.age = age;
    }

    public int getAge() {

        return this.age;
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append('}');
        return sb.toString();
    }
}

// Comparator compares its two arguments for order.  Returns a negative integer,
// zero, or a positive integer as the first argument is less than, equal
// to, or greater than the second.

public class ComparatorEx {

    public static void main(String[] args) {

        var p1 = new Person("Robert", 23);
        var p2 = new Person("Monika", 18);
        var p3 = new Person("Tom", 37);
        var p4 = new Person("Elisabeth", 31);

        List<Person> persons = Arrays.asList(p1, p2, p3, p4);

//        persons.sort(Comparator.comparingInt(Person::getAge));
        persons.sort(new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getAge() - p2.getAge();
            }
        });

        persons.forEach(System.out::println);
    }
}
