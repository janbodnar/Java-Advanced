package com.zetcode;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureEx {

    public static void main(String[] args) throws InterruptedException {

        CompletableFuture<String> comFut = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "success";
        });

        System.out.println("First pile of tasks ...");

        for (int i = 0; i < 10; i++) {

            Thread.sleep(300);
            System.out.println("1. doing task...");
        }

        // this call does not block and other pile of
        // tasks can run
        comFut.thenAccept(System.out::println);

        System.out.println("Second pile of tasks ...");

        for (int i = 0; i < 10; i++) {

            Thread.sleep(300);
            System.out.println("2. doing task ...");
        }
    }
}
