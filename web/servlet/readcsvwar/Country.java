package com.zetcode.bean;

import com.opencsv.bean.CsvBindByName;

import java.util.Objects;

public class Country {

    @CsvBindByName
    private String name;

    @CsvBindByName
    private int population;

    public Country() {
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return population == country.population &&
                Objects.equals(name, country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, population);
    }
}
