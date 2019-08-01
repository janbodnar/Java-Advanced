package com.zetcode;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureIsDoneEx {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future = executorService.submit(() -> {

            Thread.sleep(2000);
            return "result from callable";
        });

        while (!future.isDone()) {

            System.out.println("working on task...");
            Thread.sleep(400);
        }

        System.out.println("task completed");

        var result = future.get();
        System.out.println(result);

        executorService.shutdown();
    }
}
