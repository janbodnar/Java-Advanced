package com.zetcode;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureEx {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> result = executorService.submit(() -> {
            
            Thread.sleep(5000);
            
            return "success";
        });

        for (int i =0; i <10; i++) {

            Thread.sleep(300);
            System.out.println("Doing task...");
        }

        // get is blocking
        // If the thread never finishes or takes too long to compute,
        // we can pass a timeout as an argument.
        // The isDone() method to check if the task has completed.
        var res = result.get();

        System.out.println(res);

        executorService.shutdown();
    }
}
