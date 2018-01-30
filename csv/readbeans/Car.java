package com.zetcode.bean;

import com.opencsv.bean.CsvBindByName;

public class Car {
    
    @CsvBindByName
    private int id;
    
    @CsvBindByName
    private String name;
    
    @CsvBindByName
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
        builder.append("Car{id=").append(name).append(", name=")
                .append(name).append(", price=").append(price).append("}");
        
        return builder.toString();
    }    
}
