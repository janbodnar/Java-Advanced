package com.zetcode;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureCancelEx {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        int delay = new Random().nextInt(6) + 1;

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        long startTime = System.nanoTime();

        Future<String> future = executorService.submit(() -> {

            Thread.sleep(2000);
            return "message from callable";
        });

        while (!future.isDone()) {

            System.out.println("working on task ...");
            Thread.sleep(400);

            double elapsedTimeInSec = (System.nanoTime() - startTime) / 1000000000.0;

            if (elapsedTimeInSec > delay) {

                future.cancel(true);
            }
        }

        if (!future.isCancelled()) {

            System.out.println("task completed");
            String result = future.get();
            System.out.println(result);
        } else {

            System.out.println("task cancelled");
        }

        executorService.shutdown();
    }
}
