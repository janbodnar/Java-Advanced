package com.zetcode;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class RunBlocking {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {

            // Simulate a long-running job
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            System.out.printf("Task run in: %s %n", Thread.currentThread().getName());
        });

        future.whenComplete((aVoid, throwable) -> System.out.println("completed"));

        future.get();

        System.out.println("Program finished");
    }
}
