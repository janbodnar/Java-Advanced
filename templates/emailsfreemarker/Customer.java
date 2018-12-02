package com.zetcode.bean;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import java.util.Date;

public class Customer {

    @CsvBindByName
    private String name;

    @CsvBindByName
    private Long amount;

    @CsvBindByName
    @CsvDate(value = "yyyy-mm-dd")
    private Date dueDate; // OpenCSV does not support Java 8 DateTime API

    public Customer() {

    }

    public Customer(String name, Long amount, Date dueDate) {
        this.name = name;
        this.amount = amount;
        this.dueDate = dueDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {

        final var sb = new StringBuilder("Customer{");
        sb.append("name='").append(name).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", dueDate=").append(dueDate);
        sb.append('}');
        return sb.toString();
    }
}
