package com.zetcode;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

class NewWord implements Supplier<String> {

    @Override
    public String get() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "deep sky";
    }
}

// we combine an completed future with an async task
// the two results are passed as arguments to the supplied function (transform)

public class CombineCompletedWithAsyncFuture {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> startValue = CompletableFuture.completedFuture("deep ocean");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(new NewWord());

        BinaryOperator<String> transform = (f, s) -> f.toUpperCase() + "  " + s.toUpperCase();
        CompletableFuture<String> combinedFuture = future.thenCombine(startValue, transform);

        var result = combinedFuture.get();
        System.out.println(result);
    }
}
