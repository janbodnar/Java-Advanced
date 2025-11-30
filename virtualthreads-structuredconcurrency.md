# Virtual Threads and Structured Concurrency

Virtual threads and structured concurrency represent a paradigm shift in how Java  
handles concurrent programming. Introduced through Project Loom, these features  
address fundamental limitations in traditional thread-based concurrency models.  
Virtual threads are lightweight threads managed by the JVM rather than the  
operating system, enabling applications to create millions of concurrent tasks  
without exhausting system resources. Structured concurrency provides a framework  
for organizing concurrent tasks into well-defined scopes, ensuring proper  
lifecycle management, error handling, and resource cleanup.  

Traditional Java concurrency relies on platform threads, which are expensive OS  
resources. Each platform thread consumes significant memory (typically 1MB for  
the stack) and incurs context-switching overhead when the OS scheduler moves  
between threads. This limits applications to thousands of concurrent threads at  
best, forcing developers to use complex asynchronous programming patterns like  
callbacks, futures, and reactive streams to achieve high concurrency.  

Project Loom solves these problems by introducing virtual threads that are  
scheduled by the JVM on a small pool of carrier (platform) threads. When a  
virtual thread blocks on I/O or synchronization, it automatically yields its  
carrier thread, allowing other virtual threads to run. This enables the simple  
thread-per-request programming model to scale to millions of concurrent tasks.  
Combined with structured concurrency, which ensures that child tasks complete  
before their parent scope exits, Java now offers a powerful, intuitive approach  
to concurrent programming.  

| Aspect | Traditional Threads | Virtual Threads |
|--------|---------------------|-----------------|
| Memory per thread | ~1MB stack | ~1KB initial |
| Creation cost | High (OS call) | Low (JVM managed) |
| Max concurrent | Thousands | Millions |
| Blocking impact | Wastes OS thread | Yields carrier |
| Programming model | Thread pools, async | Thread-per-task |
| Debugging | Complex stack traces | Clear stack traces |

## Why Project Loom matters  

Project Loom addresses the fundamental mismatch between the thread-per-request  
model and the scalability requirements of modern applications. Before Loom,  
developers faced a difficult choice: use simple blocking code that doesn't scale,  
or use complex async code that's hard to write, debug, and maintain. Virtual  
threads eliminate this trade-off by making blocking cheap.  

```java
import java.util.concurrent.Executors;

void main() throws Exception {

    // Create 100,000 concurrent tasks - impossible with platform threads
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 0; i < 100_000; i++) {
            int taskId = i;
            executor.submit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (taskId % 10_000 == 0) {
                    println("Task " + taskId + " completed");
                }
            });
        }
    }
    println("All 100,000 tasks completed");
}
```

This example launches 100,000 concurrent tasks, each sleeping for one second.  
With platform threads, this would require 100GB of memory just for thread stacks  
and would likely crash the system. With virtual threads, it runs smoothly using  
minimal resources because sleeping virtual threads don't consume carrier threads.  

## Virtual threads fundamentals  

Virtual threads are instances of Thread that run on carrier threads managed by  
a ForkJoinPool. They look and behave like regular threads but are scheduled by  
the JVM rather than the operating system.  

```java
void main() throws InterruptedException {

    var virtualThread = Thread.ofVirtual()
        .name("my-virtual-thread")
        .start(() -> {
            println("Running in: " + Thread.currentThread());
            println("Is virtual: " + Thread.currentThread().isVirtual());
        });

    virtualThread.join();

    var platformThread = Thread.ofPlatform()
        .name("my-platform-thread")
        .start(() -> {
            println("Running in: " + Thread.currentThread());
            println("Is virtual: " + Thread.currentThread().isVirtual());
        });

    platformThread.join();
}
```

The Thread.ofVirtual factory creates a virtual thread builder, while  
Thread.ofPlatform creates a platform thread builder. The isVirtual method  
distinguishes between thread types. Virtual threads have names prefixed with  
empty string by default but can be named for debugging purposes.  

## Starting virtual threads  

Virtual threads can be started using several methods, from direct creation to  
executor services.  

```java
void main() throws InterruptedException {

    // Method 1: Thread.startVirtualThread
    var t1 = Thread.startVirtualThread(() -> {
        println("Method 1: startVirtualThread");
    });

    // Method 2: Thread.ofVirtual().start()
    var t2 = Thread.ofVirtual().start(() -> {
        println("Method 2: ofVirtual().start()");
    });

    // Method 3: Thread.ofVirtual().unstarted() then start()
    var t3 = Thread.ofVirtual().unstarted(() -> {
        println("Method 3: unstarted then start");
    });
    t3.start();

    t1.join();
    t2.join();
    t3.join();
}
```

The startVirtualThread method is the most concise for fire-and-forget tasks.  
The builder pattern with ofVirtual provides more control over thread  
configuration. Using unstarted allows configuring the thread before starting  
it, useful when you need to set up the thread before execution begins.  

## Virtual thread per task executor  

The newVirtualThreadPerTaskExecutor creates a new virtual thread for each  
submitted task, providing seamless integration with existing executor-based code.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

void main() throws Exception {

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        var futures = new ArrayList<Future<String>>();

        for (int i = 1; i <= 10; i++) {
            int taskId = i;
            futures.add(executor.submit(() -> {
                Thread.sleep(100);
                return "Result from task " + taskId;
            }));
        }

        for (var future : futures) {
            println(future.get());
        }
    }
}
```

Each task runs in its own virtual thread, allowing massive concurrency without  
thread pool sizing concerns. The try-with-resources pattern ensures all tasks  
complete before the executor closes. This executor is ideal for I/O-bound  
workloads where tasks spend most time waiting.  


## Virtual threads with thread factory  

Custom thread factories enable consistent naming and configuration of virtual  
threads.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

void main() throws Exception {

    var counter = new AtomicInteger(0);
    ThreadFactory factory = Thread.ofVirtual()
        .name("worker-", counter.getAndIncrement())
        .factory();

    try (var executor = Executors.newThreadPerTaskExecutor(factory)) {
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                println("Running: " + Thread.currentThread().getName());
            });
        }
    }
}
```

The thread factory creates virtual threads with incrementing names like  
worker-0, worker-1, etc. This improves debugging and monitoring by providing  
meaningful thread names in logs and profilers. The factory can be reused  
across multiple executors for consistent naming.  

## Platform threads vs virtual threads comparison  

Understanding the performance difference between platform and virtual threads  
helps in choosing the right approach.  

```java
import java.util.concurrent.Executors;

void main() throws Exception {

    int taskCount = 10_000;

    // Platform threads (limited pool)
    long startPlatform = System.currentTimeMillis();
    try (var executor = Executors.newFixedThreadPool(100)) {
        for (int i = 0; i < taskCount; i++) {
            executor.submit(() -> {
                try { Thread.sleep(10); } catch (InterruptedException e) {}
            });
        }
    }
    long platformTime = System.currentTimeMillis() - startPlatform;

    // Virtual threads (unlimited)
    long startVirtual = System.currentTimeMillis();
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 0; i < taskCount; i++) {
            executor.submit(() -> {
                try { Thread.sleep(10); } catch (InterruptedException e) {}
            });
        }
    }
    long virtualTime = System.currentTimeMillis() - startVirtual;

    println("Platform threads (100 pool): " + platformTime + "ms");
    println("Virtual threads: " + virtualTime + "ms");
}
```

Platform threads with a fixed pool of 100 must process 10,000 tasks in batches,  
taking approximately 1 second (10,000 / 100 * 10ms). Virtual threads run all  
tasks concurrently, completing in roughly 10ms plus overhead. This demonstrates  
the dramatic throughput improvement for I/O-bound workloads.  

## Structured concurrency introduction  

Structured concurrency organizes concurrent tasks into scopes with clear  
lifecycles. Tasks started within a scope are guaranteed to complete before the  
scope exits, preventing orphaned threads and resource leaks.  

```java
import java.util.concurrent.StructuredTaskScope;

void main() throws Exception {

    try (var scope = new StructuredTaskScope<String>()) {
        var task1 = scope.fork(() -> {
            Thread.sleep(500);
            return "Result 1";
        });

        var task2 = scope.fork(() -> {
            Thread.sleep(300);
            return "Result 2";
        });

        scope.join();

        println("Task 1: " + task1.get());
        println("Task 2: " + task2.get());
    }
}
```

The StructuredTaskScope creates a scope for forking subtasks. Each fork  
creates a new virtual thread to execute the task. The join method waits for  
all forked tasks to complete. The try-with-resources ensures proper cleanup  
even if exceptions occur.  

## ShutdownOnFailure policy  

The ShutdownOnFailure policy cancels remaining tasks when any task fails,  
enabling fail-fast behavior.  

```java
import java.util.concurrent.StructuredTaskScope;

void main() {

    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        var userTask = scope.fork(() -> fetchUser(1));
        var orderTask = scope.fork(() -> fetchOrder(1));

        scope.join();
        scope.throwIfFailed();

        println("User: " + userTask.get());
        println("Order: " + orderTask.get());
    } catch (Exception e) {
        println("Operation failed: " + e.getMessage());
    }
}

String fetchUser(int id) throws InterruptedException {
    Thread.sleep(200);
    return "User-" + id;
}

String fetchOrder(int id) throws InterruptedException {
    Thread.sleep(300);
    return "Order-" + id;
}
```

When one task fails, ShutdownOnFailure cancels all other tasks in the scope.  
The throwIfFailed method propagates the first exception that occurred. This  
is ideal when all subtasks must succeed for the overall operation to be valid,  
like fetching related data that must be consistent.  

## ShutdownOnSuccess policy  

The ShutdownOnSuccess policy returns as soon as any task succeeds, cancelling  
the remaining tasks.  

```java
import java.util.concurrent.StructuredTaskScope;

void main() throws Exception {

    try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
        scope.fork(() -> queryServer("Primary", 500));
        scope.fork(() -> queryServer("Backup", 300));
        scope.fork(() -> queryServer("Fallback", 700));

        scope.join();

        String result = scope.result();
        println("First successful result: " + result);
    }
}

String queryServer(String name, int latency) throws InterruptedException {
    Thread.sleep(latency);
    return name + " responded";
}
```

The first task to complete successfully provides the result, and other tasks  
are cancelled. This pattern is perfect for redundant requests to multiple  
servers, hedged requests, or any scenario where you need the fastest successful  
response. The result method returns the winning task's value.  

## Handling task failures  

Structured concurrency provides clean mechanisms for handling failures in  
concurrent tasks.  

```java
import java.util.concurrent.StructuredTaskScope;

void main() {

    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        var task1 = scope.fork(() -> {
            Thread.sleep(100);
            return "Success";
        });

        var task2 = scope.fork(() -> {
            Thread.sleep(50);
            throw new RuntimeException("Task 2 failed!");
        });

        scope.join();
        scope.throwIfFailed(e -> new RuntimeException("Custom: " + e.getMessage()));

        println(task1.get());
    } catch (Exception e) {
        println("Caught: " + e.getMessage());
    }
}
```

The overloaded throwIfFailed method accepts a function to transform  
exceptions, enabling custom error messages or exception types. When task2 fails,  
task1 is cancelled even if it hasn't completed yet. The exception includes the  
full context of what failed.  

## Nested structured concurrency  

Structured concurrency scopes can be nested, creating hierarchies of concurrent  
operations.  

```java
import java.util.concurrent.StructuredTaskScope;

void main() throws Exception {

    try (var outer = new StructuredTaskScope.ShutdownOnFailure()) {
        var result = outer.fork(() -> {
            try (var inner = new StructuredTaskScope.ShutdownOnFailure()) {
                var a = inner.fork(() -> compute("A"));
                var b = inner.fork(() -> compute("B"));
                inner.join();
                inner.throwIfFailed();
                return a.get() + " + " + b.get();
            }
        });

        outer.join();
        outer.throwIfFailed();

        println("Combined: " + result.get());
    }
}

String compute(String name) throws InterruptedException {
    Thread.sleep(100);
    return "Result-" + name;
}
```

The inner scope runs two parallel computations, and its combined result becomes  
the return value of the outer task. If any inner task fails, the inner scope  
shuts down first, which then causes the outer task to fail. This hierarchical  
structure makes complex concurrent workflows manageable.  

## Timeout with structured concurrency  

Structured concurrency integrates seamlessly with timeout handling.  

```java
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.StructuredTaskScope;

void main() {

    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        var task = scope.fork(() -> {
            Thread.sleep(5000);
            return "Completed";
        });

        scope.joinUntil(Instant.now().plus(Duration.ofSeconds(1)));
        scope.throwIfFailed();

        println(task.get());
    } catch (Exception e) {
        println("Timed out or failed: " + e.getClass().getSimpleName());
    }
}
```

The joinUntil method specifies a deadline for all tasks to complete. If tasks  
don't finish by the deadline, they are interrupted and the scope throws a  
TimeoutException. This prevents operations from hanging indefinitely and  
enables responsive applications.  

## Collecting results from multiple tasks  

Aggregating results from multiple concurrent tasks is a common pattern in  
structured concurrency.  

```java
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

void main() throws Exception {

    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        var tasks = new ArrayList<Subtask<String>>();

        for (int i = 1; i <= 5; i++) {
            int id = i;
            tasks.add(scope.fork(() -> fetchData(id)));
        }

        scope.join();
        scope.throwIfFailed();

        var results = tasks.stream()
            .map(Subtask::get)
            .toList();

        println("Results: " + results);
    }
}

String fetchData(int id) throws InterruptedException {
    Thread.sleep((long) (Math.random() * 500));
    return "Data-" + id;
}
```

Multiple tasks are forked and their Subtask handles are collected. After  
joining, results are extracted using stream operations. All tasks run  
concurrently, so total time is the maximum individual task time, not the sum.  
This pattern is ideal for parallel data fetching.  


## Custom StructuredTaskScope  

Creating custom scopes enables specialized behavior for domain-specific  
requirements.  

```java
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.AtomicInteger;

class CountingScope<T> extends StructuredTaskScope<T> {
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger failureCount = new AtomicInteger(0);

    @Override
    protected void handleComplete(Subtask<? extends T> subtask) {
        if (subtask.state() == Subtask.State.SUCCESS) {
            successCount.incrementAndGet();
        } else if (subtask.state() == Subtask.State.FAILED) {
            failureCount.incrementAndGet();
        }
    }

    int getSuccessCount() { return successCount.get(); }
    int getFailureCount() { return failureCount.get(); }
}

void main() throws Exception {

    try (var scope = new CountingScope<String>()) {
        scope.fork(() -> "Success 1");
        scope.fork(() -> "Success 2");
        scope.fork(() -> { throw new RuntimeException("Fail"); });

        scope.join();

        println("Successes: " + scope.getSuccessCount());
        println("Failures: " + scope.getFailureCount());
    }
}
```

Custom scopes override handleComplete to react when tasks finish. This example  
counts successful and failed tasks. Custom scopes can implement voting policies,  
result aggregation, partial success handling, or any domain-specific logic.  

## Virtual threads with blocking I/O  

Virtual threads excel at blocking I/O operations, automatically yielding their  
carrier thread while waiting.  

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;

void main() throws Exception {

    var client = HttpClient.newHttpClient();
    var urls = List.of(
        "https://httpbin.org/delay/1",
        "https://httpbin.org/delay/1",
        "https://httpbin.org/delay/1"
    );

    long start = System.currentTimeMillis();

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        var futures = urls.stream()
            .map(url -> executor.submit(() -> {
                var request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
                return client.send(request, 
                    HttpResponse.BodyHandlers.ofString()).statusCode();
            }))
            .toList();

        for (var future : futures) {
            println("Status: " + future.get());
        }
    }

    println("Total time: " + (System.currentTimeMillis() - start) + "ms");
}
```

Three HTTP requests that each take 1 second complete in about 1 second total  
because they run concurrently on virtual threads. While waiting for network  
responses, the virtual threads yield their carriers, allowing efficient use of  
the underlying thread pool.  

## Database connection simulation  

Virtual threads handle database-like blocking operations efficiently.  

```java
import java.util.concurrent.Executors;

void main() throws Exception {

    record User(int id, String name) {}

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        var futures = new ArrayList<java.util.concurrent.Future<User>>();

        for (int i = 1; i <= 1000; i++) {
            int userId = i;
            futures.add(executor.submit(() -> {
                Thread.sleep(50);
                return new User(userId, "User-" + userId);
            }));
        }

        int count = 0;
        for (var future : futures) {
            future.get();
            count++;
        }
        println("Fetched " + count + " users");
    }
}
```

Simulating 1000 database queries that each take 50ms completes in about 50ms  
with virtual threads, not 50 seconds. Each query runs in its own virtual  
thread, and blocking calls like Thread.sleep don't waste platform threads.  

## Semaphore with virtual threads  

Semaphores control access to limited resources when using virtual threads.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

void main() throws Exception {

    var connectionPool = new Semaphore(10);

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 1; i <= 100; i++) {
            int taskId = i;
            executor.submit(() -> {
                try {
                    connectionPool.acquire();
                    println("Task " + taskId + " acquired connection");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    connectionPool.release();
                }
            });
        }
    }
    println("All tasks completed");
}
```

A semaphore limits concurrent database connections to 10, even though 100  
virtual threads are running. When a virtual thread blocks on acquire, it  
yields its carrier thread. This pattern enables resource pooling while  
maintaining the simplicity of thread-per-task programming.  

## Parallel data processing  

Virtual threads simplify parallel processing of data collections.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

void main() throws Exception {

    var items = List.of("apple", "banana", "cherry", "date", "elderberry",
                        "fig", "grape", "honeydew", "kiwi", "lemon");

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        var futures = items.stream()
            .map(item -> executor.submit(() -> processItem(item)))
            .toList();

        var results = new ArrayList<String>();
        for (var future : futures) {
            results.add(future.get());
        }

        println("Processed: " + results);
    }
}

String processItem(String item) throws InterruptedException {
    Thread.sleep((long) (Math.random() * 200));
    return item.toUpperCase();
}
```

Each item is processed in its own virtual thread, allowing concurrent execution.  
The random sleep simulates varying processing times. Results are collected in  
submission order, preserving the original sequence while gaining parallelism.  


## Structured concurrency for API aggregation  

Structured concurrency excels at aggregating data from multiple APIs.  

```java
import java.util.concurrent.StructuredTaskScope;

void main() throws Exception {

    record UserProfile(String user, String orders, String preferences) {}

    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        var userTask = scope.fork(() -> fetchUserData(1));
        var ordersTask = scope.fork(() -> fetchOrders(1));
        var prefsTask = scope.fork(() -> fetchPreferences(1));

        scope.join();
        scope.throwIfFailed();

        var profile = new UserProfile(
            userTask.get(),
            ordersTask.get(),
            prefsTask.get()
        );

        println("Profile: " + profile);
    }
}

String fetchUserData(int id) throws InterruptedException {
    Thread.sleep(300);
    return "User-" + id;
}

String fetchOrders(int id) throws InterruptedException {
    Thread.sleep(500);
    return "Orders for user " + id;
}

String fetchPreferences(int id) throws InterruptedException {
    Thread.sleep(200);
    return "Prefs for user " + id;
}
```

Three API calls run concurrently, and their results are combined into a  
UserProfile record. Total latency is the maximum of individual calls (500ms),  
not the sum (1000ms). If any call fails, all others are cancelled, preventing  
wasted work.  

## Error propagation in structured concurrency  

Structured concurrency ensures errors propagate correctly through task  
hierarchies.  

```java
import java.util.concurrent.StructuredTaskScope;

void main() {

    try {
        processOrder(123);
        println("Order processed successfully");
    } catch (Exception e) {
        println("Order failed: " + e.getMessage());
    }
}

void processOrder(int orderId) throws Exception {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        var inventory = scope.fork(() -> checkInventory(orderId));
        var payment = scope.fork(() -> processPayment(orderId));

        scope.join();
        scope.throwIfFailed();

        println("Inventory: " + inventory.get());
        println("Payment: " + payment.get());
    }
}

String checkInventory(int orderId) throws Exception {
    Thread.sleep(100);
    if (orderId % 2 == 1) {
        throw new Exception("Inventory check failed for order " + orderId);
    }
    return "In stock";
}

String processPayment(int orderId) throws InterruptedException {
    Thread.sleep(200);
    return "Payment confirmed";
}
```

When checkInventory fails, the payment task is cancelled and the exception  
propagates to the caller. This prevents partial operations and ensures either  
complete success or clean failure. The exception includes full context for  
debugging.  

## Cancellation handling  

Virtual threads respond to interruption, enabling proper cancellation handling.  

```java
import java.util.concurrent.StructuredTaskScope;

void main() throws Exception {

    try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
        scope.fork(() -> {
            for (int i = 0; i < 100; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    println("Task 1 was cancelled at iteration " + i);
                    return "Cancelled";
                }
                Thread.sleep(50);
            }
            return "Task 1 completed";
        });

        scope.fork(() -> {
            Thread.sleep(100);
            return "Task 2 completed quickly";
        });

        scope.join();
        println("Result: " + scope.result());
    }
}
```

Task 2 completes first, triggering ShutdownOnSuccess to cancel Task 1. Task 1  
checks for interruption and exits gracefully. Proper cancellation handling  
prevents wasted computation and enables responsive shutdown.  

## Virtual threads with locks  

Virtual threads work with standard Java synchronization primitives.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

void main() throws Exception {

    var lock = new ReentrantLock();
    var counter = new int[]{0};

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                lock.lock();
                try {
                    counter[0]++;
                } finally {
                    lock.unlock();
                }
            });
        }
    }

    println("Counter: " + counter[0]);
}
```

ReentrantLock works correctly with virtual threads. When a virtual thread  
blocks on lock, it may pin its carrier thread (in some cases), so prefer  
using synchronized or lock-free constructs where possible. For short critical  
sections, the pinning overhead is minimal.  

## Pinning demonstration  

Pinning occurs when a virtual thread cannot yield its carrier thread, typically  
inside synchronized blocks.  

```java
import java.util.concurrent.Executors;

void main() throws Exception {

    var monitor = new Object();

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 0; i < 10; i++) {
            int taskId = i;
            executor.submit(() -> {
                synchronized (monitor) {
                    println("Task " + taskId + " entered synchronized block");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    println("Task " + taskId + " exiting synchronized block");
                }
            });
        }
    }
}
```

Sleeping inside a synchronized block pins the virtual thread to its carrier.  
This limits concurrency because other virtual threads cannot use that carrier.  
Avoid blocking operations inside synchronized blocks when using virtual threads.  
Use ReentrantLock with careful design instead.  

## Avoiding pinning with ReentrantLock  

Using ReentrantLock instead of synchronized can reduce pinning issues.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

void main() throws Exception {

    var lock = new ReentrantLock();
    var sharedResource = new StringBuilder();

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 0; i < 10; i++) {
            int taskId = i;
            executor.submit(() -> {
                lock.lock();
                try {
                    Thread.sleep(100);
                    sharedResource.append(taskId).append(" ");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            });
        }
    }

    println("Result: " + sharedResource);
}
```

With ReentrantLock, the virtual thread may still experience some pinning, but  
it is generally handled better than synchronized blocks. The lock ensures mutual  
exclusion while allowing other virtual threads to make progress on their  
carriers.  

## Concurrent map updates  

ConcurrentHashMap works well with virtual threads for concurrent data access.  

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

void main() throws Exception {

    var wordCounts = new ConcurrentHashMap<String, Integer>();
    var words = List.of("apple", "banana", "apple", "cherry", "banana", 
                        "apple", "date", "cherry", "banana", "apple");

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (var word : words) {
            executor.submit(() -> {
                wordCounts.merge(word, 1, Integer::sum);
            });
        }
    }

    println("Word counts: " + wordCounts);
}
```

The merge method atomically updates counts without explicit locking. Each  
word is processed in its own virtual thread, but ConcurrentHashMap ensures  
thread-safe updates. This pattern scales to millions of concurrent updates.  


## Fan-out pattern  

The fan-out pattern distributes work across many virtual threads.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

void main() throws Exception {

    var numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        var futures = numbers.stream()
            .map(n -> executor.submit(() -> {
                Thread.sleep((long) (Math.random() * 100));
                return n * n;
            }))
            .toList();

        int sum = 0;
        for (var future : futures) {
            sum += future.get();
        }

        println("Sum of squares: " + sum);
    }
}
```

Each number is squared in its own virtual thread, with results aggregated.  
The fan-out pattern is ideal for embarrassingly parallel computations where  
tasks are independent. Total time approaches the maximum task time rather than  
the sum.  

## Fan-in pattern  

The fan-in pattern collects results from multiple virtual threads into a  
single result.  

```java
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.AtomicInteger;

void main() throws Exception {

    var total = new AtomicInteger(0);

    try (var scope = new StructuredTaskScope<Integer>()) {
        for (int i = 1; i <= 10; i++) {
            int value = i;
            scope.fork(() -> {
                Thread.sleep((long) (Math.random() * 100));
                int result = value * value;
                total.addAndGet(result);
                return result;
            });
        }

        scope.join();
    }

    println("Total: " + total.get());
}
```

Multiple tasks compute partial results that are aggregated into a single total.  
AtomicInteger ensures thread-safe accumulation. This pattern is common in  
map-reduce style computations where intermediate results must be combined.  

## Pipeline processing  

Virtual threads enable efficient pipeline processing of data.  

```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

void main() throws Exception {

    BlockingQueue<String> stage1Output = new LinkedBlockingQueue<>();
    BlockingQueue<String> stage2Output = new LinkedBlockingQueue<>();

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        executor.submit(() -> {
            for (int i = 1; i <= 5; i++) {
                stage1Output.put("Item-" + i);
                Thread.sleep(100);
            }
            stage1Output.put("DONE");
            return null;
        });

        executor.submit(() -> {
            while (true) {
                String item = stage1Output.take();
                if ("DONE".equals(item)) {
                    stage2Output.put("DONE");
                    break;
                }
                stage2Output.put(item.toUpperCase());
            }
            return null;
        });

        executor.submit(() -> {
            while (true) {
                String item = stage2Output.take();
                if ("DONE".equals(item)) break;
                println("Final: " + item);
            }
            return null;
        });
    }
}
```

Three pipeline stages run concurrently, connected by blocking queues. Virtual  
threads make blocking queue operations efficient. The producer-transformer-  
consumer pattern enables streaming data processing with natural backpressure.  

## Retry with virtual threads  

Implementing retry logic is straightforward with virtual threads.  

```java
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

void main() throws Exception {

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        var result = executor.submit(() -> 
            retryWithBackoff(() -> unreliableOperation(), 3, 100))
            .get();

        println("Result: " + result);
    }
}

String unreliableOperation() throws Exception {
    if (Math.random() < 0.7) {
        println("Operation failed, will retry...");
        throw new Exception("Random failure");
    }
    return "Success!";
}

<T> T retryWithBackoff(Callable<T> operation, int maxRetries, 
                        long initialDelay) throws Exception {
    Exception lastException = null;
    for (int attempt = 0; attempt < maxRetries; attempt++) {
        try {
            return operation.call();
        } catch (Exception e) {
            lastException = e;
            Thread.sleep(initialDelay * (1L << attempt));
        }
    }
    throw lastException;
}
```

The retry logic uses exponential backoff between attempts. Sleeping in a  
virtual thread doesn't waste platform threads. This pattern is essential for  
resilient applications that handle transient failures gracefully.  

## Scatter-gather pattern  

The scatter-gather pattern sends requests to multiple services and collects  
responses.  

```java
import java.util.concurrent.StructuredTaskScope;

void main() throws Exception {

    record SearchResult(String source, List<String> results) {}

    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        var webTask = scope.fork(() -> searchWeb("java virtual threads"));
        var newsTask = scope.fork(() -> searchNews("java virtual threads"));
        var academicTask = scope.fork(() -> searchAcademic("java virtual threads"));

        scope.join();
        scope.throwIfFailed();

        var allResults = List.of(
            webTask.get(),
            newsTask.get(),
            academicTask.get()
        );

        println("Combined results from " + allResults.size() + " sources");
        allResults.forEach(r -> 
            println(r.source() + ": " + r.results().size() + " results"));
    }
}

SearchResult searchWeb(String query) throws InterruptedException {
    Thread.sleep(300);
    return new SearchResult("Web", List.of("Result 1", "Result 2"));
}

SearchResult searchNews(String query) throws InterruptedException {
    Thread.sleep(200);
    return new SearchResult("News", List.of("Article 1"));
}

SearchResult searchAcademic(String query) throws InterruptedException {
    Thread.sleep(400);
    return new SearchResult("Academic", List.of("Paper 1", "Paper 2", "Paper 3"));
}
```

Three search services are queried concurrently, and results are aggregated.  
If any service fails, all searches are cancelled. This pattern improves latency  
by parallelizing independent service calls.  

## Rate limiting with virtual threads  

Rate limiting controls the pace of operations when using virtual threads.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

void main() throws Exception {

    var rateLimiter = new Semaphore(5);

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 1; i <= 20; i++) {
            int requestId = i;
            executor.submit(() -> {
                try {
                    rateLimiter.acquire();
                    try {
                        println("Processing request " + requestId + 
                            " at " + System.currentTimeMillis() % 10000);
                        Thread.sleep(1000);
                    } finally {
                        rateLimiter.release();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }
}
```

A semaphore limits concurrent operations to 5, even with 20 virtual threads  
requesting access. Virtual threads blocked on the semaphore don't consume  
platform threads. This pattern prevents overwhelming external services or  
databases.  

## Timeout per operation  

Individual operation timeouts ensure responsive behavior.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() {

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        var future = executor.submit(() -> {
            Thread.sleep(5000);
            return "Completed";
        });

        try {
            var result = future.get(1, TimeUnit.SECONDS);
            println("Result: " + result);
        } catch (java.util.concurrent.TimeoutException e) {
            println("Operation timed out");
            future.cancel(true);
        } catch (Exception e) {
            println("Error: " + e.getMessage());
        }
    }
}
```

The Future.get method with timeout limits how long to wait for a result.  
Cancelling the future after timeout stops the virtual thread. This prevents  
individual slow operations from blocking the entire application.  

## Bulk operation timeout  

Timeouts can apply to groups of operations using structured concurrency.  

```java
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.StructuredTaskScope;

void main() {

    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            scope.fork(() -> {
                Thread.sleep(taskId * 500L);
                return "Task " + taskId + " completed";
            });
        }

        try {
            scope.joinUntil(Instant.now().plus(Duration.ofSeconds(1)));
            scope.throwIfFailed();
            println("All tasks completed in time");
        } catch (java.util.concurrent.TimeoutException e) {
            println("Some tasks timed out");
        }
    } catch (Exception e) {
        println("Error: " + e.getMessage());
    }
}
```

Tasks taking 500ms, 1000ms, 1500ms, 2000ms, and 2500ms are started. With a 1  
second deadline, only the first two complete; others are interrupted. This  
ensures predictable response times for aggregate operations.  


## Graceful shutdown  

Virtual thread executors support graceful shutdown patterns.  

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    for (int i = 1; i <= 10; i++) {
        int taskId = i;
        executor.submit(() -> {
            try {
                Thread.sleep(taskId * 200L);
                println("Task " + taskId + " completed");
            } catch (InterruptedException e) {
                println("Task " + taskId + " was interrupted");
            }
        });
    }

    Thread.sleep(500);
    println("Initiating shutdown...");
    executor.shutdown();

    if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
        println("Forcing shutdown...");
        executor.shutdownNow();
    }

    println("Executor terminated");
}
```

Graceful shutdown waits for running tasks, while shutdownNow interrupts them.  
Tasks should handle interruption to enable clean shutdown. This pattern ensures  
resources are released properly when the application stops.  

## Monitoring virtual threads  

JFR events enable monitoring of virtual thread behavior.  

```java
import java.util.concurrent.Executors;

void main() throws Exception {

    println("Starting virtual thread monitoring demo...");
    println("Use JFR to capture events: jdk.VirtualThreadStart, " +
            "jdk.VirtualThreadEnd, jdk.VirtualThreadPinned");

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                Thread.sleep(10);
                return null;
            });
        }
    }

    println("Monitoring complete. Analyze with JFR/JMC for:");
    println("- Virtual thread creation/termination");
    println("- Carrier thread utilization");
    println("- Pinning events");
}
```

Java Flight Recorder (JFR) provides events for virtual thread lifecycle,  
pinning, and carrier thread usage. Use JDK Mission Control to analyze  
recordings. Monitoring helps identify performance issues and pinning hotspots.  

## Thread-local with virtual threads  

Thread-locals work with virtual threads but have caveats.  

```java
import java.util.concurrent.Executors;

void main() throws Exception {

    ThreadLocal<String> context = new ThreadLocal<>();

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            executor.submit(() -> {
                context.set("Context-" + taskId);
                println(Thread.currentThread().getName() + 
                    " has context: " + context.get());
                context.remove();
            });
        }
    }
}
```

Each virtual thread has its own thread-local storage. However, with millions of  
virtual threads, thread-locals can consume significant memory. Consider using  
scoped values (preview feature) or passing context explicitly for better  
scalability.  

## Scoped values preview  

Scoped values offer a more efficient alternative to thread-locals for virtual  
threads.  

```java
import java.util.concurrent.StructuredTaskScope;

void main() throws Exception {

    ScopedValue<String> USER = ScopedValue.newInstance();

    ScopedValue.where(USER, "Alice").run(() -> {
        try (var scope = new StructuredTaskScope<String>()) {
            var task1 = scope.fork(() -> {
                return "Task 1 sees user: " + USER.get();
            });

            var task2 = scope.fork(() -> {
                return "Task 2 sees user: " + USER.get();
            });

            scope.join();

            println(task1.get());
            println(task2.get());
        } catch (Exception e) {
            println("Error: " + e.getMessage());
        }
    });
}
```

Scoped values are inherited by child virtual threads automatically. They are  
immutable within a scope and more memory-efficient than thread-locals. This  
feature integrates naturally with structured concurrency.  

## Server request handling simulation  

Virtual threads excel at handling many concurrent server requests.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

void main() throws Exception {

    var requestCount = new AtomicInteger(0);
    var completedCount = new AtomicInteger(0);

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 0; i < 10_000; i++) {
            executor.submit(() -> {
                requestCount.incrementAndGet();
                handleRequest();
                completedCount.incrementAndGet();
            });
        }
    }

    println("Received: " + requestCount.get() + " requests");
    println("Completed: " + completedCount.get() + " requests");
}

void handleRequest() {
    try {
        Thread.sleep(100);
        parseRequest();
        Thread.sleep(50);
        queryDatabase();
        Thread.sleep(30);
        formatResponse();
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}

void parseRequest() {}
void queryDatabase() {}
void formatResponse() {}
```

Ten thousand concurrent requests are handled efficiently. Each request involves  
multiple blocking operations (simulated with sleep). Virtual threads enable  
thread-per-request architecture without resource exhaustion.  

## Comparison with CompletableFuture  

Virtual threads simplify code compared to CompletableFuture chains.  

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;

void main() throws Exception {

    // CompletableFuture approach
    var cf = CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        return "user";
    }).thenCompose(user -> CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        return user + "-orders";
    })).thenApply(data -> data.toUpperCase());

    println("CF result: " + cf.get());

    // Virtual thread approach
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        var task = scope.fork(() -> {
            Thread.sleep(100);
            String user = "user";
            Thread.sleep(100);
            String orders = user + "-orders";
            return orders.toUpperCase();
        });
        scope.join();
        scope.throwIfFailed();
        println("VT result: " + task.get());
    }
}
```

The virtual thread version uses straightforward sequential code while  
maintaining concurrency benefits. CompletableFuture requires chaining and  
lambdas, making code harder to read and debug. Virtual threads preserve  
natural code structure.  

## Microservice call aggregation  

Structured concurrency simplifies calling multiple microservices.  

```java
import java.util.concurrent.StructuredTaskScope;

void main() throws Exception {

    record Order(String id, String userId, List<String> items, 
                 String shipping, String payment) {}

    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        var userTask = scope.fork(() -> callUserService("user-123"));
        var inventoryTask = scope.fork(() -> callInventoryService("order-456"));
        var shippingTask = scope.fork(() -> callShippingService("addr-789"));
        var paymentTask = scope.fork(() -> callPaymentService("pay-012"));

        scope.join();
        scope.throwIfFailed();

        var order = new Order(
            "order-456",
            userTask.get(),
            inventoryTask.get(),
            shippingTask.get(),
            paymentTask.get()
        );

        println("Order assembled: " + order);
    }
}

String callUserService(String id) throws InterruptedException {
    Thread.sleep(200);
    return "John Doe";
}

List<String> callInventoryService(String id) throws InterruptedException {
    Thread.sleep(300);
    return List.of("Widget", "Gadget");
}

String callShippingService(String id) throws InterruptedException {
    Thread.sleep(150);
    return "Express Delivery";
}

String callPaymentService(String id) throws InterruptedException {
    Thread.sleep(250);
    return "Credit Card ****1234";
}
```

Four microservice calls run concurrently, with results combined into an Order  
record. If any service fails, others are cancelled. Total latency is the  
maximum individual call time (300ms), not the sum (900ms).  


## Best practice: prefer structured concurrency  

Structured concurrency should be preferred over unstructured alternatives.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;

void main() throws Exception {

    // Anti-pattern: unstructured concurrency
    println("Unstructured approach (not recommended):");
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        Future<String> orphan = executor.submit(() -> {
            Thread.sleep(1000);
            return "Orphan result";
        });
        println("Executor closed, but task might still run...");
    }

    // Best practice: structured concurrency
    println("\nStructured approach (recommended):");
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        var managed = scope.fork(() -> {
            Thread.sleep(100);
            return "Managed result";
        });
        scope.join();
        scope.throwIfFailed();
        println("Result: " + managed.get());
    }
    println("Scope closed, all tasks guaranteed complete");
}
```

Structured concurrency guarantees task completion before scope exit. Unstructured  
approaches can leave orphan tasks running, causing resource leaks and unexpected  
behavior. Always prefer StructuredTaskScope for concurrent operations.  

## Best practice: avoid blocking in synchronized  

Minimize blocking operations inside synchronized blocks to prevent pinning.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

void main() throws Exception {

    var lock = new ReentrantLock();
    var monitor = new Object();

    println("Using ReentrantLock (better for virtual threads):");
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 0; i < 5; i++) {
            int id = i;
            executor.submit(() -> {
                lock.lock();
                try {
                    println("Task " + id + " in critical section");
                } finally {
                    lock.unlock();
                }
            });
        }
    }

    println("\nUsing synchronized (may cause pinning):");
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 0; i < 5; i++) {
            int id = i;
            executor.submit(() -> {
                synchronized (monitor) {
                    println("Task " + id + " in synchronized block");
                }
            });
        }
    }
}
```

While both approaches work, ReentrantLock handles virtual thread scheduling  
more gracefully than synchronized blocks. For new code using virtual threads,  
prefer explicit locks or lock-free algorithms.  

## Best practice: resource cleanup  

Proper resource cleanup is essential with virtual threads.  

```java
import java.util.concurrent.StructuredTaskScope;

void main() throws Exception {

    record Resource(String name) implements AutoCloseable {
        Resource { println("Opening: " + name); }
        @Override
        public void close() { println("Closing: " + name); }
    }

    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        scope.fork(() -> {
            try (var resource = new Resource("Resource A")) {
                Thread.sleep(100);
                return "Used " + resource.name();
            }
        });

        scope.fork(() -> {
            try (var resource = new Resource("Resource B")) {
                Thread.sleep(150);
                throw new RuntimeException("Task B failed");
            }
        });

        scope.join();

        try {
            scope.throwIfFailed();
        } catch (Exception e) {
            println("Caught: " + e.getMessage());
        }
    }

    println("All resources cleaned up");
}
```

Use try-with-resources for automatic cleanup. When tasks fail, resources are  
still closed properly. Structured concurrency ensures cleanup happens before  
the scope exits, even during failures.  

## Common pitfall: thread pool sizing  

Fixed thread pools limit virtual thread benefits.  

```java
import java.util.concurrent.Executors;

void main() throws Exception {

    // Anti-pattern: using fixed pool with virtual threads
    println("Fixed pool (bad pattern):");
    long start1 = System.currentTimeMillis();
    try (var executor = Executors.newFixedThreadPool(10)) {
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            });
        }
    }
    println("Fixed pool time: " + (System.currentTimeMillis() - start1) + "ms");

    // Best practice: virtual thread per task
    println("\nVirtual thread executor (good pattern):");
    long start2 = System.currentTimeMillis();
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            });
        }
    }
    println("Virtual threads time: " + (System.currentTimeMillis() - start2) + "ms");
}
```

Fixed thread pools serialize work through limited threads. Virtual thread  
executors allow all tasks to run concurrently. Use newVirtualThreadPerTaskExecutor  
for I/O-bound workloads instead of sizing thread pools.  

## Common pitfall: CPU-bound tasks  

Virtual threads don't improve CPU-bound task performance.  

```java
import java.util.concurrent.Executors;

void main() throws Exception {

    // CPU-bound work - virtual threads don't help
    println("CPU-bound computation (virtual threads not beneficial):");
    long start = System.currentTimeMillis();
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 0; i < 4; i++) {
            int taskId = i;
            executor.submit(() -> {
                long sum = 0;
                for (long j = 0; j < 100_000_000; j++) {
                    sum += j;
                }
                println("Task " + taskId + " sum: " + sum);
            });
        }
    }
    println("CPU-bound time: " + (System.currentTimeMillis() - start) + "ms");

    // For CPU-bound work, use platform threads with parallelism
    println("\nUse parallel streams or ForkJoinPool for CPU-bound work");
}
```

Virtual threads are optimized for I/O-bound tasks that frequently block. For  
CPU-intensive computations, use platform threads, parallel streams, or  
ForkJoinPool. Virtual threads share carrier threads, so CPU-bound tasks can  
monopolize them.  

## Batch processing with virtual threads  

Virtual threads enable efficient batch processing of large datasets.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

void main() throws Exception {

    var records = new ArrayList<String>();
    for (int i = 1; i <= 1000; i++) {
        records.add("Record-" + i);
    }

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        var futures = records.stream()
            .map(record -> executor.submit(() -> processRecord(record)))
            .toList();

        int processed = 0;
        for (var future : futures) {
            future.get();
            processed++;
        }
        println("Processed " + processed + " records");
    }
}

String processRecord(String record) throws InterruptedException {
    Thread.sleep(10);
    return record.toUpperCase();
}
```

Each record is processed in its own virtual thread, enabling high throughput.  
With 1000 records taking 10ms each, sequential processing would take 10 seconds.  
Virtual threads complete all processing in approximately 10ms plus overhead.  

## Async event handling  

Virtual threads simplify event-driven architectures.  

```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

void main() throws Exception {

    BlockingQueue<String> eventQueue = new LinkedBlockingQueue<>();

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        executor.submit(() -> {
            for (int i = 1; i <= 10; i++) {
                eventQueue.put("Event-" + i);
                Thread.sleep(100);
            }
            eventQueue.put("SHUTDOWN");
            return null;
        });

        for (int i = 0; i < 3; i++) {
            int handlerId = i;
            executor.submit(() -> {
                while (true) {
                    String event = eventQueue.take();
                    if ("SHUTDOWN".equals(event)) {
                        eventQueue.put("SHUTDOWN");
                        break;
                    }
                    println("Handler " + handlerId + " processed: " + event);
                    Thread.sleep(50);
                }
                return null;
            });
        }
    }
}
```

Multiple event handlers process events from a shared queue. Blocking on  
queue.take doesn't waste threads. The shutdown signal propagates to all  
handlers. This pattern scales to thousands of concurrent event handlers.  

## Connection pooling with virtual threads  

Connection pools work seamlessly with virtual threads.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

void main() throws Exception {

    var connectionPool = new ConnectionPool(5);

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 1; i <= 20; i++) {
            int queryId = i;
            executor.submit(() -> {
                try (var connection = connectionPool.acquire()) {
                    println("Query " + queryId + " using " + connection);
                    Thread.sleep(100);
                }
            });
        }
    }

    println("All queries completed");
}

class ConnectionPool {
    private final Semaphore semaphore;
    private final java.util.concurrent.atomic.AtomicInteger connectionId = new java.util.concurrent.atomic.AtomicInteger(0);

    ConnectionPool(int size) {
        this.semaphore = new Semaphore(size);
    }

    Connection acquire() throws InterruptedException {
        semaphore.acquire();
        return new Connection(connectionId.incrementAndGet(), this);
    }

    void release() {
        semaphore.release();
    }
}

record Connection(int id, ConnectionPool pool) implements AutoCloseable {
    @Override
    public void close() {
        pool.release();
    }
}
```

A semaphore-based pool limits concurrent connections. Virtual threads waiting  
for connections don't block carrier threads. The AutoCloseable pattern ensures  
connections are returned to the pool.  

## Parallel file processing  

Virtual threads efficiently process multiple files concurrently.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

void main() throws Exception {

    var files = List.of("file1.txt", "file2.txt", "file3.txt", 
                        "file4.txt", "file5.txt");

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        var futures = files.stream()
            .map(file -> executor.submit(() -> processFile(file)))
            .toList();

        for (var future : futures) {
            println(future.get());
        }
    }
}

String processFile(String filename) throws InterruptedException {
    println("Processing: " + filename);
    Thread.sleep((long) (Math.random() * 500));
    return filename + " processed successfully";
}
```

Each file is processed in its own virtual thread. I/O-bound file operations  
benefit from virtual threads because they spend most time waiting. Total  
processing time is the maximum individual file time, not the sum.  


## Summary comparison table  

| Feature | Platform Threads | Virtual Threads | Structured Concurrency |
|---------|------------------|-----------------|------------------------|
| Memory overhead | High (~1MB) | Low (~1KB) | Inherits from virtual |
| Creation cost | Expensive | Cheap | Adds scope overhead |
| Max concurrency | Thousands | Millions | Depends on tasks |
| Blocking cost | Wastes thread | Yields carrier | Same as virtual |
| Error handling | Manual | Manual | Automatic propagation |
| Task lifecycle | Unmanaged | Unmanaged | Scoped and managed |
| Cancellation | Manual interrupt | Manual interrupt | Automatic on failure |
| Debug/profile | Complex | Clear stacks | Hierarchical context |
| Best for | CPU-bound | I/O-bound | Coordinated I/O tasks |

## Conclusion  

Virtual threads and structured concurrency fundamentally transform concurrent  
programming in Java. Virtual threads eliminate the scalability limitations of  
platform threads, enabling millions of concurrent tasks with minimal memory  
overhead. When a virtual thread blocks on I/O, it automatically yields its  
carrier thread, allowing other tasks to proceed.  

Structured concurrency adds organizational structure to concurrent code. By  
binding task lifecycles to lexical scopes, it ensures that child tasks complete  
before their parent scope exits. This prevents orphaned threads, simplifies  
error handling, and makes concurrent code easier to reason about.  

Key takeaways for adopting these features:  

1. **Use virtual threads for I/O-bound workloads**: Web servers, database  
   clients, and microservices benefit most from virtual threads.  

2. **Prefer structured concurrency**: Use StructuredTaskScope instead of raw  
   executors to ensure proper task lifecycle management.  

3. **Avoid pinning**: Minimize blocking operations inside synchronized blocks;  
   prefer ReentrantLock or lock-free algorithms.  

4. **Don't pool virtual threads**: Create a new virtual thread per task;  
   pooling defeats their purpose.  

5. **Keep CPU-bound work on platform threads**: Virtual threads don't improve  
   compute-intensive operations.  

6. **Monitor with JFR**: Use Java Flight Recorder to identify pinning and  
   understand carrier thread utilization.  

7. **Migrate incrementally**: Existing ExecutorService code can switch to  
   newVirtualThreadPerTaskExecutor with minimal changes.  

Together, virtual threads and structured concurrency make Java a compelling  
choice for high-concurrency applications, combining the simplicity of  
thread-per-task programming with the scalability of asynchronous systems.  
