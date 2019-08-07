package com.zetcode.service;

public class MessageService {

    private static int count = 0;

    public void say() {

        count++;

        if (count == 1) {

            System.out.printf("Method called %d time%n", count);
        } else {

            System.out.printf("Method called %d times%n", count);
        }
    }
}
