package com.zetcode;

import java.time.LocalDate;

public class BuilderPatternEx {

    public static void main(String[] args) {

        var task1 = new TaskBuilder().setName("Task A").setDescription("finish book")
                .setDueDate(LocalDate.of(2019, 5, 11)).build();

        System.out.println(task1);

        var task2 = new TaskBuilder().setName("Task B").setDescription("go shopping")
                .setDueDate(LocalDate.of(2019, 3, 12)).build();

        System.out.println(task2);


    }
}
