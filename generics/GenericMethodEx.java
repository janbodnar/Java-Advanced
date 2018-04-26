package com.zetcode;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// Generic method

class Car {

    private String name;
    private int price;

    public Car(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Car{" + "name=" + name + ", price=" + price + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + this.price;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Car other = (Car) obj;
        if (this.price != other.price) {
            return false;
        }
        return Objects.equals(this.name, other.name);
    }
}

public class GenericMethodEx {

    public static void main(String[] args) {
        
        Set<String> words = new HashSet<>();
        words.add("blue");
        words.add("pen");
        words.add("dog");
        words.add("narrow");
        
        Set<String> words2 = new HashSet<>();
        words.add("blue");
        words.add("pencil");
        words.add("forest");
        words.add("narrow");
        
        Set<String> res = union(words, words2);
        System.out.println(res);
        
        Set<Car> cars = new HashSet<>();
        cars.add(new Car("Volvo", 23144));
        cars.add(new Car("Mazda", 20500));
        cars.add(new Car("Mercedes", 50000));
        
        Set<Car> cars2 = new HashSet<>();
        cars.add(new Car("Volvo", 23144));
        cars.add(new Car("Mazda", 20500));
        cars.add(new Car("Skoda", 9800));
        
        Set<Car> res2 = union(cars, cars2);
        res2.forEach(System.out::println);
        
    }
    
    public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
        
        Set<E> result = new HashSet<>(s1);
        result.addAll(s2);
        
        return result;
    }
}
