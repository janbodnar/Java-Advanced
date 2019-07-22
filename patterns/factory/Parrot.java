package com.zetcode;

public class Parrot implements Pet {

    private String name;
    private String species;

    public Parrot(String name, String species) {
        this.name = name;
        this.species = species;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Parrot{");
        sb.append("name='").append(name).append('\'');
        sb.append(", species='").append(species).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
