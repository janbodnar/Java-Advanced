package com.zetcode;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class CallableEx {

    private static class RandomTask implements Callable<Integer> {

        private static int nth = 0;
        private final int id = ++nth;

        @Override
        public Integer call() {

            int value = new Random().nextInt(1000);

            try {
                System.out.printf("task %d started computing...%n", id);
                MILLISECONDS.sleep(value);

            } catch (InterruptedException e) {

                e.printStackTrace();
            }

            System.out.printf("task %d is returning value: %d%n", id, value);
            return value;
        }
    }

    public static void main(String[] args) {

        ExecutorService executor = Executors.newCachedThreadPool();

        System.out.println("submitting tasks for execution");
        List<Future<Integer>> results = new LinkedList<>();

        for (int i = 0; i < 6; i++) {

            results.add(executor.submit(new RandomTask()));
        }

        System.out.println("getting results from futures");

        for (Future<Integer> result : results) {

            try {
                System.out.printf("computed result: %d%n", result.get());

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
    }
}
