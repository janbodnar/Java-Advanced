package com.zetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

// runAfterEitherAsync returns a future which runs an async task
// when either of the supplied futures completes

public class RunAfterEitherAsync {

    public static void main(String[] args) throws InterruptedException {

        List<String> results = new ArrayList<>();

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            results.add("deep forest");
        });

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            results.add("blue sky");
        });

        CompletableFuture<Void> finisher = future1.runAfterEitherAsync(future2,
                () -> System.out.println(results));

        System.out.println(finisher.isDone());

        TimeUnit.SECONDS.sleep(8);

        System.out.println(finisher.isDone());
    }
}
