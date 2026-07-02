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

## runAfterEitherAsync


The `runAfterEitherAsync` method returns a new `CompletableFuture` that runs  
a given action as soon as either of two supplied stages completes, without  
waiting for the other. This is useful when two computations race to produce  
some effect and only the first to finish matters — for example, querying a  
cache and a database in parallel and reacting the moment either responds.  


```java
package com.zetcode;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

// runAfterEitherAsync returns a future which runs an async task
// when either of the supplied futures completes

public class RunAfterEitherAsync {

    @SuppressWarnings("java:S4507")
    public static void main(String[] args) throws InterruptedException {

        List<String> results = new CopyOnWriteArrayList<>();

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {

            int randTimeout = ThreadLocalRandom.current().nextInt(1, 6);

            try {
                TimeUnit.SECONDS.sleep(randTimeout);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
            results.add("future 1 finished with A");
        });

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {

            int randTimeout = ThreadLocalRandom.current().nextInt(1, 6);

            try {
                TimeUnit.SECONDS.sleep(randTimeout);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
            results.add("future 2 finished with B");
        });

        CompletableFuture<Void> finisher = future1.runAfterEitherAsync(future2,
                () -> System.out.println(results));

        System.out.println(finisher.isDone());

        TimeUnit.SECONDS.sleep(8);

        System.out.println(finisher.isDone());
    }
}
```

In the example, two futures each sleep for a random duration between one  
and five seconds before appending a message to a shared  
`CopyOnWriteArrayList`. The `finisher` future prints that list as soon as  
either future completes. Since the delays are random, either one may win,  
and the output would seem to contain only the winning future's message.  

Running the program repeatedly shows otherwise: sometimes both messages  
appear. This happens because `runAfterEitherAsync` only guarantees that its  
action is *triggered* by the first completing future — not that the action  
runs instantly or that the second future is paused meanwhile. The "Async"  
variant schedules the callback onto a thread pool, introducing a small  
delay before it actually executes. If the losing future finishes during  
that window, it adds its own message before the callback runs, and both  
entries show up.  

This illustrates that `runAfterEitherAsync` guarantees *when* its action is  
triggered, not the state of any shared data the action reads. The race  
isn't a flaw in the method — it's a consequence of pairing a  
fire-on-first-completion trigger with mutable state that isn't scoped to  
the winning future alone.

## Website status

Asynchronously checks status of multiple websites.

```java
package com.zetcode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class WebSiteStatus {

    public static void main(String[] args) {

        List<URI> uris = Stream
                .of("https://www.google.com/", "https://clojure.org",
                        "https://www.rust-lang.org", "https://golang.org",
                        "https://www.python.org",
                        "https://code.visualstudio.com", "https://ifconfig.me",
                        "http://termbin.com", "https://www.github.com/")
                .map(URI::create).toList();

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.ALWAYS).build();

        var futures = uris.stream().map(uri -> verifyUri(httpClient, uri))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();
    }

    private static CompletableFuture<Void> verifyUri(HttpClient httpClient,
            URI uri) {
        HttpRequest request = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(5)).uri(uri).build();

        return httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .thenApply(HttpResponse::statusCode)
                .thenApply(statusCode -> statusCode == 200)
                .exceptionally(ex -> false).thenAccept(valid -> {
                    if (Boolean.TRUE.equals(valid)) {
                        System.out.printf("[SUCCESS] Verified %s%n", uri);
                    } else {
                        System.out.printf("[FAILURE] Failed to verify%s%n",
                                uri);
                    }
                });
    }
}
```


