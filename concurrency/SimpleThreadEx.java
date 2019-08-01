package com.zetcode;

public class SimpleThreadEx extends Thread {

    // run() method contains the code that is executed by the thread
    @Override
    public void run() {

        System.out.printf("inside %s%n", Thread.currentThread().getName());
    }

    public static void main(String[] args) {

        System.out.printf("inside %s%n", Thread.currentThread().getName());

        var thread = new SimpleThreadEx();
        
        System.out.println("starting thread");
        thread.start();
    }
}
