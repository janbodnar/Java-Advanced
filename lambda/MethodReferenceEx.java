package com.zetcode;

import java.util.Arrays;
import java.util.Comparator;

// Sorting list of objects by their double fields

class Company {

    private String name;
    private Double rating;

    public Company(String name, Double rating) {
        this.name = name;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {

        final var sb = new StringBuilder("Company{");
        sb.append("name='").append(name).append('\'');
        sb.append(", rating=").append(rating);
        sb.append('}');
        return sb.toString();
    }
}

public class MethodReferenceEx {

    public static void main(String[] args) {

        var companies = Arrays.asList(new Company("Comp A", 4.5),
                new Company("Comp B", 6.5), new Company("Comp C", 3.1),
                new Company("Comp D", 8.4), new Company("Comp E", 7.8));

        companies.sort(Comparator.comparingDouble(Company::getRating));
        companies.forEach(System.out::println);

        System.out.println("Reversed");

        companies.sort(Comparator.comparingDouble(Company::getRating).reversed());
        companies.forEach(System.out::println);
    }
}
