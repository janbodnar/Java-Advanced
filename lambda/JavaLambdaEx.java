package com.zetcode;

public class JavaLambdaEx {

    public static void main(String[] args) {

        // Anonymous Runnable
        Runnable r1 = new Runnable() {

            @Override
            public void run() {
                System.out.println("Runnable one");
            }
        };

        // Lambda Runnable
        Runnable r2 = () -> System.out.println("Runnable two");

        r1.run();
        r2.run();

    }
}
