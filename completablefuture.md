# CompletableFuture and Asynchronous Programming

Asynchronous programming enables non-blocking execution of tasks, allowing  
applications to remain responsive while performing time-consuming operations.  
`CompletableFuture`, introduced in Java 8, provides a powerful and flexible API  
for composing asynchronous computations. This document explores the fundamental  
concepts, practical patterns, and best practices for mastering asynchronous  
programming in Java.  

## Why Asynchronous Programming Matters  

Traditional synchronous programming executes operations sequentially, blocking  
the current thread until each operation completes. While simple to understand,  
this approach leads to poor resource utilization and unresponsive applications,  
especially when dealing with I/O operations, network calls, or database queries.  

Consider a web server handling thousands of concurrent requests. If each request  
blocks a thread while waiting for a database response, the server quickly runs  
out of available threads, leading to increased latency and reduced throughput.  
Asynchronous programming addresses this by allowing threads to handle other work  
while waiting for slow operations to complete.  

| Aspect | Synchronous | Asynchronous |
|--------|-------------|--------------|
| Execution | Sequential, blocking | Concurrent, non-blocking |
| Thread usage | One thread per task | Threads shared across tasks |
| Responsiveness | Can freeze UI/server | Remains responsive |
| Complexity | Simple control flow | Callback management |
| Scalability | Limited by threads | Highly scalable |

## Evolution of Java Concurrency  

Java's concurrency model has evolved significantly over the years:  

**Java 1.0 (1996)**: Basic `Thread` class and `Runnable` interface introduced  
low-level thread management. Developers manually created and synchronized threads,  
leading to complex and error-prone code.  

**Java 5 (2004)**: The `java.util.concurrent` package brought `ExecutorService`,  
`Future`, and thread pools. This abstracted thread lifecycle management but still  
required blocking calls to retrieve results.  

**Java 8 (2014)**: `CompletableFuture` revolutionized async programming with  
fluent APIs for chaining, combining, and handling errors in asynchronous  
computations.  

**Java 21 (2023)**: Virtual threads and structured concurrency further simplified  
concurrent programming, making thread-per-request architectures practical.  

## Limitations of Future  

The `Future` interface, while useful, has significant limitations that  
`CompletableFuture` addresses:  

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

void main() throws Exception {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    Future<String> future = executor.submit(() -> {
        Thread.sleep(1000);
        return "Result from Future";
    });

    // Blocking call - thread waits here
    String result = future.get();
    println("Received: " + result);

    executor.shutdown();
}
```

The traditional `Future` requires blocking calls to retrieve results. There is  
no way to chain operations, combine multiple futures, or handle exceptions in  
a functional style. `CompletableFuture` solves all of these limitations by  
providing callback-based completion, fluent chaining APIs, and comprehensive  
exception handling mechanisms.  

## Creating a CompletableFuture  

`CompletableFuture` can be created in several ways, from simple completed  
futures to asynchronous suppliers.  

```java
import java.util.concurrent.CompletableFuture;

void main() throws Exception {

    // Create an already completed future
    var completed = CompletableFuture.completedFuture("Hello");
    println("Completed value: " + completed.get());

    // Create an empty future to complete later
    var manual = new CompletableFuture<String>();
    manual.complete("Manually completed");
    println("Manual value: " + manual.get());

    // Create a failed future
    var failed = CompletableFuture.<String>failedFuture(
        new RuntimeException("Intentional failure")
    );
    println("Failed: " + failed.isCompletedExceptionally());
}
```

These factory methods provide flexibility for different scenarios. Completed  
futures are useful for testing or when a value is immediately available.  
Manually completing futures enables integration with callback-based APIs or  
external systems that don't return `CompletableFuture` directly.  

## Asynchronous execution with supplyAsync  

The `supplyAsync` method runs a `Supplier` asynchronously and returns a  
`CompletableFuture` with the result.  

```java
import java.util.concurrent.CompletableFuture;

void main() throws Exception {

    var future = CompletableFuture.supplyAsync(() -> {
        println("Computing in: " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return 42;
    });

    println("Main thread continues immediately...");
    println("Result: " + future.get());
}
```

By default, `supplyAsync` uses the common `ForkJoinPool`. The computation runs  
in a separate thread while the main thread continues. Calling `get` blocks  
until the result is available, but the async computation has already started  
in parallel.  

## Fire-and-forget with runAsync  

When you don't need a return value, use `runAsync` to execute a `Runnable`  
asynchronously.  

```java
import java.util.concurrent.CompletableFuture;

void main() throws Exception {

    var future = CompletableFuture.runAsync(() -> {
        println("Logging in: " + Thread.currentThread().getName());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        println("Log entry saved");
    });

    println("Main thread not blocked");
    future.join();
    println("Logging complete");
}
```

The `runAsync` method returns `CompletableFuture<Void>` since there's no result.  
Using `join` instead of `get` avoids checked exceptions but still blocks until  
completion. This pattern is ideal for side effects like logging, notifications,  
or cache updates.  

## Transforming results with thenApply  

The `thenApply` method transforms the result of a `CompletableFuture` using a  
function, similar to `map` on streams.  

```java
import java.util.concurrent.CompletableFuture;

void main() {

    var result = CompletableFuture.supplyAsync(() -> "hello")
        .thenApply(s -> s.toUpperCase())
        .thenApply(s -> s + " WORLD")
        .thenApply(s -> s.length())
        .join();

    println("Final length: " + result);
}
```

Each `thenApply` creates a new stage in the async pipeline. The transformations  
execute sequentially after the previous stage completes. This enables fluent,  
functional-style processing of async results without manual callback nesting.  

## Consuming results with thenAccept  

Use `thenAccept` when you want to consume the result without producing a new  
value.  

```java
import java.util.concurrent.CompletableFuture;

void main() throws Exception {

    CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        return List.of("apple", "banana", "cherry");
    })
    .thenAccept(fruits -> {
        println("Received " + fruits.size() + " fruits:");
        fruits.forEach(f -> println("  - " + f));
    })
    .join();
}
```

The `thenAccept` method returns `CompletableFuture<Void>` since the consumer  
doesn't produce a value. This is useful for final processing steps like  
displaying results, sending notifications, or updating UI elements.  

## Running actions with thenRun  

The `thenRun` method executes a `Runnable` after completion, ignoring both the  
result and any transformation.  

```java
import java.util.concurrent.CompletableFuture;

void main() {

    CompletableFuture.supplyAsync(() -> {
        println("Step 1: Fetching data...");
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        return "data";
    })
    .thenRun(() -> println("Step 2: Processing complete notification"))
    .thenRun(() -> println("Step 3: Cleanup started"))
    .join();

    println("All steps finished");
}
```

Use `thenRun` for actions that don't depend on the result, such as cleanup,  
logging, or triggering follow-up processes. Multiple `thenRun` calls chain  
sequentially, each waiting for the previous to complete.  

## Async variants for non-blocking chains  

Each chaining method has an `Async` variant that runs in a separate thread.  

```java
import java.util.concurrent.CompletableFuture;

void main() {

    CompletableFuture.supplyAsync(() -> {
        println("Supply: " + Thread.currentThread().getName());
        return "hello";
    })
    .thenApplyAsync(s -> {
        println("Apply: " + Thread.currentThread().getName());
        return s.toUpperCase();
    })
    .thenAcceptAsync(s -> {
        println("Accept: " + Thread.currentThread().getName());
        println("Result: " + s);
    })
    .join();
}
```

Without the `Async` suffix, callbacks run in the same thread that completed the  
previous stage. With `Async`, each stage may run in a different thread from the  
common pool. Use async variants when transformations are CPU-intensive or when  
you need to avoid blocking the completing thread.  

## Combining two futures with thenCombine  

The `thenCombine` method waits for two independent futures and combines their  
results.  

```java
import java.util.concurrent.CompletableFuture;

void main() {

    var priceFuture = CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep(800); } catch (InterruptedException e) {}
        return 100.0;
    });

    var discountFuture = CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        return 0.15;
    });

    var finalPrice = priceFuture
        .thenCombine(discountFuture, (price, discount) -> 
            price * (1 - discount))
        .join();

    println("Final price: $" + finalPrice);
}
```

Both futures run concurrently. When both complete, the combining function  
executes with their results. This is ideal for aggregating data from multiple  
independent sources, such as fetching user profile and preferences  
simultaneously.  

## Sequential composition with thenCompose  

Use `thenCompose` when the next async operation depends on the result of the  
previous one, similar to `flatMap` on streams.  

```java
import java.util.concurrent.CompletableFuture;

void main() {

    var result = fetchUserId("alice")
        .thenCompose(userId -> fetchUserDetails(userId))
        .thenCompose(details -> fetchUserOrders(details))
        .join();

    println("Orders: " + result);
}

CompletableFuture<Integer> fetchUserId(String username) {
    return CompletableFuture.supplyAsync(() -> {
        println("Fetching user ID for: " + username);
        try { Thread.sleep(300); } catch (InterruptedException e) {}
        return 42;
    });
}

CompletableFuture<String> fetchUserDetails(int userId) {
    return CompletableFuture.supplyAsync(() -> {
        println("Fetching details for user: " + userId);
        try { Thread.sleep(300); } catch (InterruptedException e) {}
        return "UserDetails{id=" + userId + "}";
    });
}

CompletableFuture<List<String>> fetchUserOrders(String details) {
    return CompletableFuture.supplyAsync(() -> {
        println("Fetching orders for: " + details);
        try { Thread.sleep(300); } catch (InterruptedException e) {}
        return List.of("Order1", "Order2", "Order3");
    });
}
```

Unlike `thenApply`, which would return `CompletableFuture<CompletableFuture<T>>`,  
`thenCompose` flattens the result. This creates clean, readable chains for  
dependent async operations like database lookups or API calls.  

## Handling exceptions with exceptionally  

The `exceptionally` method provides a fallback value when an exception occurs.  

```java
import java.util.concurrent.CompletableFuture;

void main() {

    var result = CompletableFuture.supplyAsync(() -> {
        if (Math.random() > 0.5) {
            throw new RuntimeException("Random failure!");
        }
        return "Success";
    })
    .exceptionally(ex -> {
        println("Caught: " + ex.getMessage());
        return "Fallback value";
    })
    .join();

    println("Result: " + result);
}
```

The `exceptionally` callback receives the exception and returns a replacement  
value of the same type. This enables graceful degradation, like returning  
cached data or default values when a service call fails.  

## Comprehensive handling with handle  

The `handle` method processes both success and failure cases in a single  
callback.  

```java
import java.util.concurrent.CompletableFuture;

void main() {

    processValue(10);
    processValue(-5);
}

void processValue(int input) {
    var result = CompletableFuture.supplyAsync(() -> {
        if (input < 0) {
            throw new IllegalArgumentException("Negative not allowed: " + input);
        }
        return input * 2;
    })
    .handle((value, ex) -> {
        if (ex != null) {
            println("Error for " + input + ": " + ex.getCause().getMessage());
            return 0;
        }
        return value;
    })
    .join();

    println("Processed " + input + " -> " + result);
}
```

Unlike `exceptionally`, `handle` always executes, receiving either the result  
or the exception (one will be null). This is useful for logging, cleanup, or  
transforming both success and failure into a unified response format.  

## Observing completion with whenComplete  

The `whenComplete` method runs a callback after completion without transforming  
the result.  

```java
import java.util.concurrent.CompletableFuture;

void main() {

    CompletableFuture.supplyAsync(() -> {
        if (Math.random() > 0.5) {
            throw new RuntimeException("Task failed");
        }
        return "Task succeeded";
    })
    .whenComplete((result, ex) -> {
        if (ex != null) {
            println("Completed with error: " + ex.getMessage());
        } else {
            println("Completed with result: " + result);
        }
    })
    .exceptionally(ex -> "Recovered")
    .thenAccept(r -> println("Final: " + r))
    .join();
}
```

The `whenComplete` callback observes the result but doesn't change it. Unlike  
`handle`, exceptions propagate to downstream stages. This is perfect for  
logging, metrics, or auditing without affecting the pipeline's result.  

## Waiting for all futures with allOf  

The `allOf` method creates a future that completes when all provided futures  
complete.  

```java
import java.util.concurrent.CompletableFuture;

void main() {

    var futures = List.of(
        fetchData("Service A", 1000),
        fetchData("Service B", 500),
        fetchData("Service C", 750)
    );

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
        .thenRun(() -> {
            println("All services responded:");
            futures.forEach(f -> println("  - " + f.join()));
        })
        .join();
}

CompletableFuture<String> fetchData(String service, int delay) {
    return CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep(delay); } catch (InterruptedException e) {}
        return service + " data";
    });
}
```

All futures execute concurrently. The combined future completes when the  
slowest one finishes. Note that `allOf` returns `CompletableFuture<Void>`, so  
you must access individual futures to get their results.  

## Racing futures with anyOf  

The `anyOf` method completes as soon as any of the provided futures completes.  

```java
import java.util.concurrent.CompletableFuture;

void main() {

    var primary = queryServer("Primary", 1000);
    var backup = queryServer("Backup", 500);
    var fallback = queryServer("Fallback", 750);

    var fastest = CompletableFuture.anyOf(primary, backup, fallback)
        .thenApply(result -> (String) result)
        .join();

    println("Fastest response: " + fastest);
}

CompletableFuture<String> queryServer(String name, int responseTime) {
    return CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep(responseTime); } catch (InterruptedException e) {}
        println(name + " responded");
        return name + " data";
    });
}
```

This pattern is useful for redundant requests to multiple servers, taking the  
fastest response. The other futures continue running unless explicitly  
cancelled. Note that `anyOf` returns `CompletableFuture<Object>`, requiring a  
cast.  

## Collecting results from multiple futures  

Aggregating results from multiple `CompletableFutures` into a single list  
requires combining `allOf` with stream operations.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

void main() {

    var productIds = List.of(1, 2, 3, 4, 5);

    var futures = productIds.stream()
        .map(id -> fetchProduct(id))
        .toList();

    var allProducts = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0]))
        .thenApply(v -> futures.stream()
            .map(CompletableFuture::join)
            .toList())
        .join();

    println("Fetched " + allProducts.size() + " products:");
    allProducts.forEach(p -> println("  " + p));
}

CompletableFuture<String> fetchProduct(int id) {
    return CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep((long)(Math.random() * 500)); } 
        catch (InterruptedException e) {}
        return "Product-" + id;
    });
}
```

This pattern launches all fetches concurrently, waits for all to complete,  
then collects results into a list. The total time is approximately that of the  
slowest fetch, not the sum of all fetches.  

## Using custom executors  

You can provide a custom `Executor` to control where async tasks run.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

void main() {

    ExecutorService customPool = Executors.newFixedThreadPool(4, r -> {
        var thread = new Thread(r);
        thread.setName("CustomWorker-" + thread.getId());
        return thread;
    });

    try {
        var result = CompletableFuture.supplyAsync(() -> {
            println("Running in: " + Thread.currentThread().getName());
            return "Hello";
        }, customPool)
        .thenApplyAsync(s -> {
            println("Transforming in: " + Thread.currentThread().getName());
            return s.toUpperCase();
        }, customPool)
        .join();

        println("Result: " + result);
    } finally {
        customPool.shutdown();
    }
}
```

Custom executors allow tuning pool sizes for specific workloads, isolating  
different task types, or implementing custom thread naming for debugging.  
Always shut down custom executors to prevent resource leaks.  

## Virtual thread executor integration  

Java 21's virtual threads work seamlessly with `CompletableFuture` for highly  
scalable async operations.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

void main() {

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        var futures = new ArrayList<CompletableFuture<String>>();

        for (int i = 1; i <= 100; i++) {
            int id = i;
            futures.add(CompletableFuture.supplyAsync(() -> {
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                return "Task-" + id + " on " + Thread.currentThread();
            }, executor));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .join();

        println("Completed " + futures.size() + " tasks");
        println("Sample: " + futures.get(0).join());
    }
}
```

Virtual threads are lightweight and ideal for I/O-bound tasks. Each task gets  
its own virtual thread without the overhead of platform threads, enabling  
millions of concurrent operations.  

## Timeout handling  

The `orTimeout` and `completeOnTimeout` methods provide built-in timeout  
support.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

void main() {

    // Fails with TimeoutException
    try {
        CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
            return "Slow result";
        })
        .orTimeout(1, TimeUnit.SECONDS)
        .join();
    } catch (Exception e) {
        println("Timed out: " + e.getCause().getClass().getSimpleName());
    }

    // Completes with default value on timeout
    var result = CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        return "Slow result";
    })
    .completeOnTimeout("Default value", 1, TimeUnit.SECONDS)
    .join();

    println("Result: " + result);
}
```

Use `orTimeout` when timeouts should fail fast and propagate errors. Use  
`completeOnTimeout` when you want a graceful fallback. Both methods were  
added in Java 9 and simplify timeout handling compared to manual approaches.  

## Delayed execution  

The `delayedExecutor` method creates an executor that delays task execution.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() {

    var delayedExecutor = CompletableFuture.delayedExecutor(
        2, TimeUnit.SECONDS
    );

    println("Starting at: " + java.time.LocalTime.now());

    CompletableFuture.supplyAsync(() -> {
        println("Executing at: " + java.time.LocalTime.now());
        return "Delayed result";
    }, delayedExecutor)
    .thenAccept(r -> println("Received: " + r))
    .join();
}
```

This is useful for implementing retry delays, scheduled tasks, or rate  
limiting. The delayed executor wraps the common pool by default but can  
wrap a custom executor for more control.  

## Parallel API calls  

A common use case is fetching data from multiple APIs concurrently.  

```java
import java.util.concurrent.CompletableFuture;

void main() {

    var startTime = System.currentTimeMillis();

    var userFuture = fetchFromApi("/users/1", 500);
    var ordersFuture = fetchFromApi("/orders?user=1", 700);
    var preferencesFuture = fetchFromApi("/preferences/1", 300);

    var combined = userFuture
        .thenCombine(ordersFuture, (user, orders) -> 
            "User: " + user + ", Orders: " + orders)
        .thenCombine(preferencesFuture, (partial, prefs) -> 
            partial + ", Prefs: " + prefs)
        .join();

    var elapsed = System.currentTimeMillis() - startTime;
    println("Combined result: " + combined);
    println("Total time: " + elapsed + "ms (parallel, not 1500ms)");
}

CompletableFuture<String> fetchFromApi(String endpoint, int latency) {
    return CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep(latency); } catch (InterruptedException e) {}
        return endpoint.substring(1).replace("/", "_");
    });
}
```

By launching all requests simultaneously, the total time is limited by the  
slowest request (~700ms) rather than the sum (~1500ms). This pattern  
dramatically improves response times in microservices architectures.  

## Processing a stream asynchronously  

Combining streams with `CompletableFuture` enables parallel data processing.  

```java
import java.util.concurrent.CompletableFuture;

void main() {

    var items = List.of("apple", "banana", "cherry", "date", "elderberry");

    var processedFutures = items.stream()
        .map(item -> processAsync(item))
        .toList();

    var results = processedFutures.stream()
        .map(CompletableFuture::join)
        .toList();

    println("Processed items:");
    results.forEach(r -> println("  " + r));
}

CompletableFuture<String> processAsync(String item) {
    return CompletableFuture.supplyAsync(() -> {
        try { 
            Thread.sleep((long)(Math.random() * 500)); 
        } catch (InterruptedException e) {}
        return item.toUpperCase() + " (processed)";
    });
}
```

Each item is processed concurrently. The `map` to `CompletableFuture` launches  
tasks immediately, then `join` collects results. This pattern is more flexible  
than parallel streams when you need fine-grained control over execution.  

## Retry pattern with CompletableFuture  

Implementing retry logic for transient failures.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

void main() {

    var result = retryAsync(() -> unreliableOperation(), 3)
        .exceptionally(ex -> "All retries failed: " + ex.getMessage())
        .join();

    println("Final result: " + result);
}

AtomicInteger attempts = new AtomicInteger(0);

CompletableFuture<String> unreliableOperation() {
    return CompletableFuture.supplyAsync(() -> {
        int attempt = attempts.incrementAndGet();
        println("Attempt " + attempt);
        if (attempt < 3) {
            throw new RuntimeException("Transient failure");
        }
        return "Success on attempt " + attempt;
    });
}

<T> CompletableFuture<T> retryAsync(
    java.util.function.Supplier<CompletableFuture<T>> operation, 
    int maxRetries
) {
    return operation.get().exceptionallyCompose(ex -> {
        if (maxRetries > 0) {
            println("Retrying... " + maxRetries + " attempts left");
            return retryAsync(operation, maxRetries - 1);
        }
        return CompletableFuture.failedFuture(ex);
    });
}
```

The `exceptionallyCompose` method enables returning a new `CompletableFuture`  
on failure, perfect for retry scenarios. This pattern handles transient  
failures gracefully without blocking the calling thread.  

## Cache-aside pattern  

Implementing asynchronous cache lookups with fallback to the source.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

void main() {

    var cache = new ConcurrentHashMap<String, String>();

    // First call - cache miss, fetches from source
    var result1 = getWithCache("user:123", cache).join();
    println("First call: " + result1);

    // Second call - cache hit
    var result2 = getWithCache("user:123", cache).join();
    println("Second call: " + result2);
}

CompletableFuture<String> getWithCache(
    String key, 
    ConcurrentHashMap<String, String> cache
) {
    var cached = cache.get(key);
    if (cached != null) {
        println("Cache hit for: " + key);
        return CompletableFuture.completedFuture(cached);
    }

    println("Cache miss for: " + key);
    return fetchFromDatabase(key)
        .thenApply(value -> {
            cache.put(key, value);
            return value;
        });
}

CompletableFuture<String> fetchFromDatabase(String key) {
    return CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        return "Data for " + key + " from DB";
    });
}
```

This pattern checks the cache synchronously (fast path) and only goes async  
for cache misses. The cache is updated after fetching, benefiting subsequent  
requests. Consider using `computeIfAbsent` for atomic cache population.  

## Circuit breaker integration  

Implementing a simple circuit breaker to prevent cascading failures.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

void main() {

    var breaker = new CircuitBreaker(3, 5000);

    for (int i = 1; i <= 10; i++) {
        int call = i;
        breaker.execute(() -> unreliableService(call))
            .exceptionally(ex -> "Call " + call + " failed: " + ex.getMessage())
            .thenAccept(r -> println(r))
            .join();
    }
}

CompletableFuture<String> unreliableService(int callNum) {
    return CompletableFuture.supplyAsync(() -> {
        if (callNum <= 4) {
            throw new RuntimeException("Service unavailable");
        }
        return "Call " + callNum + " succeeded";
    });
}

class CircuitBreaker {
    private final int threshold;
    private final long resetTimeout;
    private final AtomicInteger failures = new AtomicInteger(0);
    private final AtomicLong lastFailure = new AtomicLong(0);

    CircuitBreaker(int threshold, long resetTimeout) {
        this.threshold = threshold;
        this.resetTimeout = resetTimeout;
    }

    <T> CompletableFuture<T> execute(
        java.util.function.Supplier<CompletableFuture<T>> operation
    ) {
        if (isOpen()) {
            return CompletableFuture.failedFuture(
                new RuntimeException("Circuit breaker is OPEN")
            );
        }

        return operation.get()
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    failures.incrementAndGet();
                    lastFailure.set(System.currentTimeMillis());
                } else {
                    failures.set(0);
                }
            });
    }

    boolean isOpen() {
        if (failures.get() >= threshold) {
            if (System.currentTimeMillis() - lastFailure.get() > resetTimeout) {
                failures.set(0);
                return false;
            }
            return true;
        }
        return false;
    }
}
```

The circuit breaker tracks failures and "opens" after a threshold, rejecting  
requests without attempting the operation. After a timeout, it "half-opens"  
to test recovery. This prevents overwhelming failing services.  

## Fan-out fan-in pattern  

Distributing work across multiple workers and aggregating results.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

void main() {

    var data = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    var partitionSize = 3;

    ExecutorService workers = Executors.newFixedThreadPool(4);

    try {
        // Fan-out: partition data and process concurrently
        var partitions = partition(data, partitionSize);
        
        var futures = partitions.stream()
            .map(partition -> CompletableFuture.supplyAsync(
                () -> processPartition(partition), workers))
            .toList();

        // Fan-in: aggregate results
        var total = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .mapToInt(CompletableFuture::join)
                .sum())
            .join();

        println("Total sum: " + total);
    } finally {
        workers.shutdown();
    }
}

int processPartition(List<Integer> partition) {
    println("Processing " + partition + " on " + 
        Thread.currentThread().getName());
    try { Thread.sleep(500); } catch (InterruptedException e) {}
    return partition.stream().mapToInt(Integer::intValue).sum();
}

<T> List<List<T>> partition(List<T> list, int size) {
    var result = new ArrayList<List<T>>();
    for (int i = 0; i < list.size(); i += size) {
        result.add(list.subList(i, Math.min(i + size, list.size())));
    }
    return result;
}
```

This pattern divides work into partitions processed concurrently, then  
combines results. It's effective for parallel aggregations, map-reduce  
operations, or batch processing.  

## Event-driven completion  

Using `CompletableFuture` to bridge callback-based APIs.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

void main() {

    // Simulated async API with callbacks
    var result = toCompletableFuture(callback -> {
        new Thread(() -> {
            try { Thread.sleep(500); } catch (InterruptedException e) {}
            callback.accept("Data from callback API");
        }).start();
    }).join();

    println("Result: " + result);
}

<T> CompletableFuture<T> toCompletableFuture(
    Consumer<Consumer<T>> asyncOperation
) {
    var future = new CompletableFuture<T>();
    asyncOperation.accept(future::complete);
    return future;
}
```

Many legacy APIs use callbacks instead of returning futures. This adapter  
pattern creates a `CompletableFuture` and completes it from the callback,  
enabling integration with modern async pipelines.  

## Structured concurrency with CompletableFuture  

Ensuring related tasks are properly scoped and managed together.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CancellationException;

void main() {

    try {
        var result = fetchUserProfile(42).join();
        println("Profile: " + result);
    } catch (Exception e) {
        println("Failed: " + e.getMessage());
    }
}

CompletableFuture<String> fetchUserProfile(int userId) {
    var userFuture = fetchUser(userId);
    var settingsFuture = fetchSettings(userId);
    var avatarFuture = fetchAvatar(userId);

    return userFuture
        .thenCombine(settingsFuture, (user, settings) -> 
            user + " | " + settings)
        .thenCombine(avatarFuture, (partial, avatar) -> 
            partial + " | " + avatar)
        .exceptionally(ex -> {
            // Cancel remaining futures on failure
            userFuture.cancel(true);
            settingsFuture.cancel(true);
            avatarFuture.cancel(true);
            throw new CancellationException("Profile fetch cancelled");
        });
}

CompletableFuture<String> fetchUser(int id) {
    return CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep(300); } catch (InterruptedException e) {}
        return "User-" + id;
    });
}

CompletableFuture<String> fetchSettings(int id) {
    return CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep(200); } catch (InterruptedException e) {}
        return "Settings-" + id;
    });
}

CompletableFuture<String> fetchAvatar(int id) {
    return CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep(400); } catch (InterruptedException e) {}
        return "Avatar-" + id;
    });
}
```

While not true structured concurrency (which Java 21+ provides via  
`StructuredTaskScope`), this pattern groups related operations and handles  
cancellation. All sub-tasks share a common lifecycle and failure handling.  

## Comparison with parallel streams  

Understanding when to use `CompletableFuture` versus parallel streams.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

void main() {

    var data = IntStream.rangeClosed(1, 10).boxed().toList();

    // Parallel Stream - uses ForkJoinPool, limited control
    var startParallel = System.currentTimeMillis();
    var parallelResult = data.parallelStream()
        .map(n -> {
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            return n * n;
        })
        .toList();
    println("Parallel stream: " + (System.currentTimeMillis() - startParallel) + 
        "ms, sum=" + parallelResult.stream().mapToInt(i -> i).sum());

    // CompletableFuture - more control over execution
    var startCF = System.currentTimeMillis();
    var futures = data.stream()
        .map(n -> CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            return n * n;
        }))
        .toList();
    var cfResult = futures.stream()
        .map(CompletableFuture::join)
        .toList();
    println("CompletableFuture: " + (System.currentTimeMillis() - startCF) + 
        "ms, sum=" + cfResult.stream().mapToInt(i -> i).sum());
}
```

| Feature | Parallel Streams | CompletableFuture |
|---------|------------------|-------------------|
| Control | Limited | Full control |
| Executor | Common ForkJoinPool | Any executor |
| Chaining | Limited | Extensive |
| Error handling | Exceptions propagate | exceptionally, handle |
| Use case | CPU-bound, simple | I/O-bound, complex |

## Best practices summary  

Following established patterns ensures robust async code.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() {

    // 1. Use custom executors for I/O tasks
    ExecutorService ioPool = Executors.newFixedThreadPool(10);

    try {
        // 2. Always handle exceptions
        var result = CompletableFuture.supplyAsync(() -> 
            riskyOperation(), ioPool)
            .exceptionally(ex -> {
                println("Handled error: " + ex.getMessage());
                return "fallback";
            })
            // 3. Use timeouts for external calls
            .orTimeout(5, TimeUnit.SECONDS)
            // 4. Log completion status
            .whenComplete((r, ex) -> 
                println("Completed: " + (ex == null ? "success" : "failure")))
            .join();

        println("Result: " + result);
    } finally {
        // 5. Always shutdown executors
        ioPool.shutdown();
    }
}

String riskyOperation() {
    if (Math.random() > 0.7) {
        throw new RuntimeException("Random failure");
    }
    return "success";
}
```

Key best practices include: using dedicated thread pools for different  
workload types, always handling exceptions, applying timeouts to prevent  
indefinite waits, logging for observability, and properly shutting down  
resources.  

## Common pitfall: blocking inside async  

Blocking operations inside async tasks can cause thread starvation.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

void main() {

    println("Common pool parallelism: " + 
        ForkJoinPool.commonPool().getParallelism());

    // BAD: Blocking inside supplyAsync uses up pool threads
    var badFutures = new ArrayList<CompletableFuture<String>>();
    for (int i = 0; i < 20; i++) {
        int id = i;
        badFutures.add(CompletableFuture.supplyAsync(() -> {
            try { 
                Thread.sleep(1000); // Blocking!
            } catch (InterruptedException e) {}
            return "Task-" + id;
        }));
    }

    var start = System.currentTimeMillis();
    CompletableFuture.allOf(badFutures.toArray(new CompletableFuture[0])).join();
    println("With blocking: " + (System.currentTimeMillis() - start) + "ms");

    // The common pool has limited threads, so tasks queue up
    // Use virtual threads or a larger pool for blocking I/O
}
```

The common `ForkJoinPool` has limited threads (typically CPU count - 1).  
Blocking operations exhaust these threads, preventing other tasks from  
running. Use virtual threads, dedicated I/O pools, or truly async APIs.  

## Common pitfall: ignoring exceptions  

Unhandled exceptions in async chains are silently swallowed.  

```java
import java.util.concurrent.CompletableFuture;

void main() throws Exception {

    // BAD: Exception is lost
    CompletableFuture.runAsync(() -> {
        throw new RuntimeException("Silent failure!");
    });

    Thread.sleep(500);
    println("No exception visible - it was swallowed!");

    // GOOD: Always handle or observe exceptions
    CompletableFuture.runAsync(() -> {
        throw new RuntimeException("Visible failure!");
    })
    .exceptionally(ex -> {
        println("Caught: " + ex.getMessage());
        return null;
    })
    .join();

    println("Exception was properly handled");
}
```

Without `exceptionally`, `handle`, or calling `join`/`get`, exceptions  
disappear silently. Always attach exception handlers or use `join` which  
will throw `CompletionException` for failures.  

## Common pitfall: deadlock with join  

Calling `join` inside a `CompletableFuture` can cause deadlock.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

void main() {

    // Using a single-thread executor to demonstrate
    var executor = Executors.newSingleThreadExecutor();

    try {
        // This will deadlock with a single-thread executor!
        // The outer task waits for inner, but inner can't start
        // because outer holds the only thread

        // var result = CompletableFuture.supplyAsync(() -> {
        //     var inner = CompletableFuture.supplyAsync(() -> "inner", executor);
        //     return "outer-" + inner.join(); // DEADLOCK
        // }, executor).join();

        // GOOD: Use thenCompose for dependent async operations
        var result = CompletableFuture.supplyAsync(() -> "outer", executor)
            .thenComposeAsync(outer -> 
                CompletableFuture.supplyAsync(() -> outer + "-inner", executor),
                executor)
            .join();

        println("Result: " + result);
    } finally {
        executor.shutdown();
    }
}
```

Using `join` inside async tasks creates dependency cycles. If all pool  
threads are waiting, no thread is available to complete the inner future.  
Use `thenCompose` for dependent async operations instead.  

## Conclusion  

`CompletableFuture` transforms asynchronous programming in Java from complex  
callback-based code into clean, composable pipelines. Its fluent API enables  
chaining transformations, combining independent operations, and handling errors  
gracefully—all without blocking threads unnecessarily.  

Key takeaways from this guide:  

1. **Prefer non-blocking operations**: Use `thenApply`, `thenCompose`, and  
   `thenCombine` over `get` and `join` where possible  

2. **Handle exceptions properly**: Always attach `exceptionally` or `handle`  
   to prevent silent failures  

3. **Choose the right executor**: Use virtual threads for I/O-bound work,  
   dedicated pools for CPU-intensive tasks  

4. **Apply timeouts**: Use `orTimeout` or `completeOnTimeout` for external  
   service calls  

5. **Avoid blocking in async contexts**: Never call `join` inside a  
   `CompletableFuture` running on a limited thread pool  

As Java continues to evolve with virtual threads and structured concurrency,  
`CompletableFuture` remains essential for building responsive, scalable  
applications. Whether you're implementing microservices, processing data  
pipelines, or building reactive systems, mastering `CompletableFuture` is  
fundamental to modern Java development.  
