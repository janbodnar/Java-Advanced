package com.zetcode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class SynchronizedCounter {

    private int counter = 0;

    // synchronized method
    public synchronized void inc() {

        counter = counter + 1;
    }

    public int get() {

        return counter;
    }
}

public class SynchronizedCounterEx {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        var synchronizedCounter = new SynchronizedCounter();

        for (int i = 0; i < 50; i++) {

            executorService.submit(() -> synchronizedCounter.inc());
        }

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);

        System.out.println("Final value: " + synchronizedCounter.get());
    }
}
