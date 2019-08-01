package com.zetcode;

import java.time.LocalTime;

class MessageSender {

    public void send(String msg) {

        var now = LocalTime.now();

        System.out.printf("Sending message, %s%n", now);

        try {
            Thread.sleep(1000);

        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        System.out.printf("%s sent%n", msg);
    }
}

class Worker extends Thread {

    private String msg;
    private final MessageSender messageSender;

    public Worker(String msg, MessageSender messageSender) {

        this.msg = msg;
        this.messageSender = messageSender;
    }

    public void run() {
        
        // Only one thread can send a message
        // at a time.
        synchronized (messageSender) {
            messageSender.send(msg);
        }
    }
}

public class SynchronizedMessages {

    public static void main(String[] args) {

        var snd = new MessageSender();

        var w1 = new Worker("Hello there", snd);
        var w2 = new Worker("New mail received", snd);
        var w3 = new Worker("Notes taken", snd);

        // start three threads
        w1.start();
        w2.start();
        w3.start();

        // wait for threads to end
        try {

            // join is a blocker method which waits for a thread to complete.

            // the w1.join() causes the current thread to pause execution
            // until w1's thread terminates.
            w1.join();

            w2.join();
            w3.join();

        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }
}
