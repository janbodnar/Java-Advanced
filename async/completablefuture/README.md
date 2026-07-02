# CompletableFuture

## The get method

The `get` method waits if necessary for a future to complete, and then returns its result.  

The execution flow is:

```
main: "Program started"
main: calls runAsync → task starts on pool thread
main: registers whenComplete callback (non-blocking)
main: calls future.get() → BLOCKS here
                     ↓
    (3 seconds later)
                     ↓
pool thread: "Task run in: ForkJoinPool..."
pool thread: "completed"
main: unblocks, "Program finished"
```

```java
package com.zetcode;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class RunBlocking {

    public static void main(String[] args)
            throws ExecutionException, InterruptedException {

        System.out.println("Program started");

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {

            // Simulate a long-running job
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            System.out.printf("Task run in: %s %n",
                    Thread.currentThread().getName());
        });

        future.whenComplete(
                (aVoid, throwable) -> System.out.println("completed"));

        future.get();

        System.out.println("Program finished");
    }
}
```
The `get` method blocks the `main` thread until the asynchronous task  
finishes (i.e., until the 3-second sleep ends).

## Async addition

```java

## Async addition

```java
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;

void complexCalculation() {

    try {
        Thread.sleep(Duration.of(1, ChronoUnit.SECONDS));
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        e.printStackTrace();
    }
}

int a() {
    complexCalculation();
    return 4;
}

int b() {
    complexCalculation();
    return 5;
}

void main() {

    long startTime = System.currentTimeMillis();

    // int res = a() + b();
    var a = CompletableFuture.supplyAsync(this::a);
    var b = CompletableFuture.supplyAsync(this::b);
    var res = a.thenCombine(b, (x, y) -> x + y).join();
    
    System.out.println(res);
    System.out.println("Elapsed time: " + (System.currentTimeMillis() - startTime) + "ms");

}
```
