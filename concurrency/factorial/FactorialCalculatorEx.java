package com.zetcode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class FactorialCalculatorEx {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        var executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        List<Map<Integer, Future<BigInteger>>> resultList = new ArrayList<>();

        var random = new Random();

        for (int i = 0; i < 6; i++) {

            int number = random.nextInt(100) + 10;
            var factorialCalculator = new FactorialCalculator(number);

            Map<Integer, Future<BigInteger>> result = new HashMap<>();
            result.put(number, executor.submit(factorialCalculator));
            resultList.add(result);
        }

        for (Map<Integer, Future<BigInteger>> pair : resultList) {

            var optional = pair.keySet().stream().findFirst();

            if (!optional.isPresent()) {
                return;
            }

            var key = optional.get();

            System.out.printf("Value is: %d%n", key);

            var future = pair.get(key);
            var result = future.get();
            var isDone = future.isDone();

            System.out.printf("Result is %d%n", result);
            System.out.printf("Task done: %b%n", isDone);
            System.out.println("--------------------");
        }

        executor.shutdown();
    }
}
