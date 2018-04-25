package com.zetcode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// Comparing objects by their fields

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
}

public class ListSortEx2 {

    public static void main(String[] args) {

        List<Car> cars = Arrays.asList(new Car("Volvo", 23400), 
                new Car("Mazda", 13700), new Car("Porsche", 353800), 
                new Car("Skoda", 8900),  new Car("Volkswagen", 19900));
        
        cars.sort(Comparator.comparing(Car::getPrice));
        System.out.println(cars);
        
        cars.sort(Comparator.comparing(Car::getName));
        System.out.println(cars);
    }
}
