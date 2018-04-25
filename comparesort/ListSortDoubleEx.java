package com.zetcode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// Sortring list of objects by their double fields

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
        return "Company{" + "name=" + name + ", rating=" + rating + '}';
    }
}

public class ListSortDoubleEx {

    public static void main(String[] args) {
        
        List<Company> companies = Arrays.asList(new Company("Comp A", 4.5), 
                new Company("Comp B", 6.5), new Company("Comp C", 3.1), 
                new Company("Comp D", 8.4), new Company("Comp E", 7.8));
        
        companies.sort(Comparator.comparingDouble(Company::getRating));
        companies.forEach(System.out::println);
        
        System.out.println("Reversed");
        
        companies.sort(Comparator.comparingDouble(Company::getRating).reversed());
        companies.forEach(System.out::println);
    }
}
