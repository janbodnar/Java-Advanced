package com.zetcode;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InvokeAllEx {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Callable<String> task1 = () -> {

            Thread.sleep(2000);
            return "task 1 result";
        };

        Callable<String> task2 = () -> {

            Thread.sleep(1000);
            return "task 2 result";
        };

        Callable<String> task3 = () -> {

            Thread.sleep(4000);
            return "task 3 result";
        };

        List<Callable<String>> tasks = List.of(task1, task2, task3);

        List<Future<String>> futures = executorService.invokeAll(tasks);

        for (Future<String> future : futures) {

            // results are printed after all the futures are completed
            System.out.println(future.get());
        }

        executorService.shutdown();
    }
}
