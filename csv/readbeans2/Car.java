package com.zetcode.bean;

import com.opencsv.bean.CsvBindByPosition;

public class Car {

    @CsvBindByPosition(position = 0)
    private int id;

    @CsvBindByPosition(position = 1)
    private String name;

    @CsvBindByPosition(position = 2)
    private int price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

        StringBuilder builder = new StringBuilder();
        builder.append("Car{id=").append(id).append(", name=")
                .append(name).append(", price=").append(price).append("}");

        return builder.toString();
    }
}
