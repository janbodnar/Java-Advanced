package com.zetcode;

import java.util.Arrays;
import java.util.Comparator;

// Sorting list of objects by their double fields

public class ListSortDouble {

    public static void main(String[] args) {

        var companies = Arrays.asList(new Company("Comp A", 4.5),
                new Company("Comp B", 6.5), new Company("Comp C", 3.1),
                new Company("Comp D", 8.4), new Company("Comp E", 7.8));

        companies.sort(Comparator.comparingDouble(Company::rating));
        companies.forEach(System.out::println);

        System.out.println("Reversed");

        companies.sort(Comparator.comparingDouble(Company::rating).reversed());
        companies.forEach(System.out::println);
    }
}

record Company(String name, Double rating) {}
