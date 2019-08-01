package com.zetcode;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IvokeAnyEx {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Callable<String> task1 = () -> {

            Thread.sleep(3000);
            return "task 1 result";
        };

        Callable<String> task2 = () -> {
            Thread.sleep(1500);
            return "task 2 result";
        };

        Callable<String> task3 = () -> {

            Thread.sleep(4000);
            return "task3 result";
        };

        // invokeAny returns the result of the fastest callable

        var result = executorService.invokeAny(Arrays.asList(task1, task2, task3));

        System.out.println(result);

        executorService.shutdown();
    }
}
