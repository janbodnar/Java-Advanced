package com.zetcode;

interface Item {

    String info();
}

interface Plant {

    String getColor();
}

class Bike implements Item {

    @Override
    public String info() {

        return "This is a bike";
    }
}

class Chair implements Item {

    @Override
    public String info() {

        return "This is a chair";
    }
}

class Flower implements Item, Plant {

    private String color;

    public Flower(String color) {
        this.color = color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String getColor() {
        return this.color;
    }

    @Override
    public String info() {

        return String.format("This is %s flower", this.color);
    }
}

// Generic bounded example

public class GenericBoundedEx {

    public static void main(String[] args) {

        Chair chair = new Chair();
        doInform2(chair);
        
        Flower flower = new Flower("red");
        doInform(flower);
    }

    public static <T extends Item & Plant> void doInform(T item) {

        System.out.println(item.info());
    }

    public static <T extends Item> void doInform2(T item) {

        System.out.println(item.info());
    }
}
