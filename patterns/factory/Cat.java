package com.zetcode;

public class Cat implements Pet {

    private String name;
    private String breed;

    public Cat(String name, String breed) {
        this.name = name;
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Cat{");
        sb.append("name='").append(name).append('\'');
        sb.append(", breed='").append(breed).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
