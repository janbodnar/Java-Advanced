package com.zetcode;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public class TheyApply {

     // A random supplier that sleeps for a second, and then returns
     // a random value
    public static class RandomSupplier implements Supplier<Integer> {

        @Override
        public Integer get() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            return new Random().nextInt(10);
        }
    }


    // A (pure) function that adds one to a given Integer
    public static class AddOne implements Function<Integer, Integer> {

        @Override
        public Integer apply(Integer x) {
            
            return x + 1;
        }
    }

    public static void main(String[] args) throws Exception {

        ExecutorService exec = Executors.newSingleThreadExecutor();

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(new RandomSupplier(), exec);
        System.out.println(future.isDone()); // false
        CompletableFuture<Integer> f2 = future.thenApply(new AddOne());
        System.out.println(f2.get()); // Waits until the "calculation" is done and prints the result

        exec.shutdown();

        try {
            if (!exec.awaitTermination(4, TimeUnit.SECONDS)) {
                exec.shutdownNow();
            }
        } catch (InterruptedException e) {
            exec.shutdownNow();
        }
    }
}
