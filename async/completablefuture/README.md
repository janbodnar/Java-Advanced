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


