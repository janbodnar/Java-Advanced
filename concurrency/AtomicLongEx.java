package com.zetcode;

import java.util.concurrent.atomic.AtomicLong;

class Counter {

    private AtomicLong counter = new AtomicLong(0);

    public void inc() {

        counter.getAndIncrement();
    }

    public long get() {

        return counter.get();
    }
}

public class AtomicLongEx {

    public static void main(String[] args) throws InterruptedException {

        final Counter counter = new Counter();

        // 500 threads
        for (int i = 0; i < 500; i++) {

            var thread = new Thread(() -> counter.inc());

            thread.start();
        }

        // sleep three seconds
        Thread.sleep(3000);

        System.out.println("Value: " + counter.get());
    }
}
