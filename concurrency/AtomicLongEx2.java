package com.zetcode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongEx2 {

    private static AtomicLong sum = new AtomicLong();

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 5; i++) {

            sum.set(0);

            ExecutorService executorService = Executors.newFixedThreadPool(50);

            for (int j = 1; j <= 50; j++) {

                int finalI = i;

                executorService.execute(() -> sum.addAndGet(finalI));
            }

            executorService.shutdown();
            executorService.awaitTermination(3, TimeUnit.SECONDS);

            System.out.println(sum);
        }
    }
}
