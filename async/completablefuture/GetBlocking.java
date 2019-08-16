package com.zetcode;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GetBlocking {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {

            System.out.printf("Task run in: %s %n", Thread.currentThread().getName());

            // Simulate a long-running job
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            return "success";
        });

        System.out.printf("This is %s thread %n", Thread.currentThread().getName());

        // blocking main thread
        var result = future.get();

        System.out.println(result);
    }
}

