package com.zetcode;

public class SimpleThreadEx2 implements Runnable {

    public static void main(String[] args) {

        System.out.printf("inside %s%n", Thread.currentThread().getName());

        var runnable = new SimpleThreadEx2();
        var thread = new Thread(runnable);

        System.out.println("starting thread");
        thread.start();
    }

    @Override
    public void run() {

        System.out.printf("inside %s%n", Thread.currentThread().getName());
    }
}

