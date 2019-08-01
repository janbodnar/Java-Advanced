package com.zetcode;

class Worker extends Thread {

    private int delay;
    private String msg;

    public Worker(int delay, String msg) {

        this.delay = delay;
        this.msg = msg;
    }

    public void run() {

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(msg);
    }
}

public class JoinEx {

    public static void main(String[] args) {

        var w1 = new Worker(2000, "Hello there");
        var w2 = new Worker(1000, "New mail received");
        var w3 = new Worker(500, "Notes taken");

        // start three threads
        w1.start();
        w2.start();
        w3.start();

        // wait for threads to end
        try {

            // join is a blocker method which waits for a thread to complete.

            // the w1.join() causes the current (main) thread to pause execution
            // until w1's thread terminates.
            w1.join();
            w2.join();
            w3.join();

        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        System.out.println("finished tasks");
    }
}
