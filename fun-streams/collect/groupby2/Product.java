package com.zetcode;

import java.math.BigDecimal;

public class Product {

    private String name;
    private String category;
    private BigDecimal price;

    public Product(String name, String category, BigDecimal price) {

        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {

        var sb = new StringBuilder("Product{");
        sb.append("name='").append(name).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }
}
