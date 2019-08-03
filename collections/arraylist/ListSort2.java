package com.zetcode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Country {

    private String name;
    private int population;

    public Country(String name, int population) {
        this.name = name;
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return "Country{" + "name=" + name
                + ", population=" + population + '}';
    }
}

public class ListSort2 {

    public static void main(String[] args) {

        List<Country> countries = createList();

//        List<Country> sorted_countries = countries.stream()
//                .sorted((e1, e2) -> Integer.compare(e1.getPopulation(),
//                        e2.getPopulation())).collect(Collectors.toList());

        List<Country> sorted_countries = countries.stream()
                .sorted(Comparator.comparingInt(Country::getPopulation))
                .collect(Collectors.toList());

        System.out.println(sorted_countries);
    }

    private static List<Country> createList() {

        List<Country> countries = new ArrayList<>();

        countries.add(new Country("Slovakia", 5424000));
        countries.add(new Country("Hungary", 9845000));
        countries.add(new Country("Poland", 38485000));
        countries.add(new Country("Germany", 81084000));
        countries.add(new Country("Latvia", 1978000));

        return countries;
    }
}
