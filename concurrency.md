# Java Concurrency

Concurrency in Java refers to the ability of a program to execute multiple tasks  
simultaneously, allowing for more efficient use of system resources and improved  
application performance. Java provides a rich set of APIs and language features  
for building concurrent applications, from low-level thread management to  
high-level abstractions like executors and concurrent collections.  

**Concurrency** is the composition of independently executing processes, while  
**parallelism** is the simultaneous execution of computations. In Java, threads  
are the fundamental unit of concurrency. A thread is a lightweight subprocess,  
the smallest unit of processing that can be scheduled by the operating system.  

Java's concurrency model is built around shared memory and synchronization. When  
multiple threads access shared data, proper synchronization is essential to  
prevent race conditions, deadlocks, and other concurrency issues. The Java  
Memory Model (JMM) defines how threads interact through memory and what  
behaviors are allowed in concurrent programs.  

## Key Concepts  

- **Thread**: A single sequential flow of control within a program  
- **Process**: An executing instance of a program with its own memory space  
- **Race Condition**: When multiple threads access shared data concurrently and  
  at least one modifies it, leading to unpredictable results  
- **Deadlock**: When two or more threads are blocked forever, each waiting for  
  the other to release a resource  
- **Starvation**: When a thread is unable to gain regular access to shared  
  resources and is unable to make progress  
- **Livelock**: When threads are not blocked but cannot make progress because  
  they keep responding to each other  
- **Critical Section**: A code segment that accesses shared resources and must  
  not be executed by more than one thread at a time  
- **Mutual Exclusion**: Ensuring that only one thread can execute a critical  
  section at a time  
- **Atomicity**: An operation that completes in a single step relative to other  
  threads  
- **Visibility**: Ensuring that changes made by one thread are visible to other  
  threads  
- **Ordering**: The order in which operations appear to execute  

## Thread Lifecycle  

A thread in Java can be in one of the following states:  

1. **NEW**: Thread has been created but not yet started  
2. **RUNNABLE**: Thread is executing or ready to execute  
3. **BLOCKED**: Thread is waiting to acquire a monitor lock  
4. **WAITING**: Thread is waiting indefinitely for another thread  
5. **TIMED_WAITING**: Thread is waiting for a specified time  
6. **TERMINATED**: Thread has completed execution  


## Creating a thread by extending Thread  

The simplest way to create a thread is by extending the `Thread` class and  
overriding the `run` method.  

```java
void main() {

    var thread = new Thread() {
        @Override
        public void run() {
            println("Hello from thread: " + Thread.currentThread().getName());
        }
    };

    thread.start();
    println("Main thread: " + Thread.currentThread().getName());
}
```

This example demonstrates the basic thread creation pattern. The `run` method  
contains the code that executes in the new thread. Calling `start` creates a  
new thread and invokes `run` asynchronously. The main thread continues executing  
while the new thread runs concurrently.  


## Creating a thread with Runnable  

Implementing `Runnable` is the preferred approach as it separates the task from  
the thread mechanism and allows the class to extend another class.  

```java
void main() {

    Runnable task = () -> {
        println("Running in: " + Thread.currentThread().getName());
    };

    var thread = new Thread(task);
    thread.start();

    println("Main thread continues...");
}
```

Using a lambda expression for `Runnable` provides clean, concise code. This  
approach is more flexible because the task logic is decoupled from the thread  
creation, making it easier to submit tasks to thread pools later.  


## Thread sleep  

The `sleep` method pauses the current thread for a specified duration without  
releasing any locks it holds.  

```java
void main() throws InterruptedException {

    println("Starting...");

    for (int i = 1; i <= 5; i++) {
        Thread.sleep(1000);
        println("Tick " + i);
    }

    println("Done!");
}
```

The `sleep` method throws `InterruptedException` if another thread interrupts  
the sleeping thread. This is useful for creating delays, polling, or simulating  
time-consuming operations in examples and tests.  


## Thread join  

The `join` method allows one thread to wait for the completion of another  
thread before continuing execution.  

```java
void main() throws InterruptedException {

    var worker = new Thread(() -> {
        for (int i = 0; i < 5; i++) {
            println("Working... " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    });

    worker.start();
    println("Waiting for worker to complete...");

    worker.join();
    println("Worker completed!");
}
```

Without `join`, the main thread would continue immediately. With `join`, the  
main thread blocks until the worker thread finishes. This is essential when  
you need results from a thread before proceeding.  


## Thread interrupt  

Interruption is a cooperative mechanism for signaling a thread that it should  
stop what it's doing.  

```java
void main() throws InterruptedException {

    var worker = new Thread(() -> {
        while (!Thread.currentThread().isInterrupted()) {
            println("Working...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                println("Interrupted during sleep!");
                Thread.currentThread().interrupt();
                break;
            }
        }
        println("Worker stopped.");
    });

    worker.start();
    Thread.sleep(3500);
    worker.interrupt();
    worker.join();
}
```

Threads should regularly check their interrupt status and respond appropriately.  
When `sleep` or `wait` is interrupted, an `InterruptedException` is thrown and  
the interrupt status is cleared, so it must be restored or handled.  


## Multiple threads  

Running multiple threads concurrently demonstrates true parallel execution on  
multi-core systems.  

```java
void main() throws InterruptedException {

    var threads = new ArrayList<Thread>();

    for (int i = 1; i <= 5; i++) {
        int threadNum = i;
        var thread = new Thread(() -> {
            println("Thread " + threadNum + " started");
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            println("Thread " + threadNum + " finished");
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    println("All threads completed");
}
```

Each thread executes independently and may complete in any order. The random  
sleep simulates varying workloads. Using `join` on all threads ensures the main  
thread waits for all workers before proceeding.  


## Daemon threads  

Daemon threads are background threads that don't prevent the JVM from exiting  
when all user threads have finished.  

```java
void main() throws InterruptedException {

    var daemon = new Thread(() -> {
        while (true) {
            println("Daemon running...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    });

    daemon.setDaemon(true);
    daemon.start();

    Thread.sleep(2000);
    println("Main thread exiting...");
}
```

When the main thread exits after 2 seconds, the daemon thread is automatically  
terminated. Daemon threads are useful for background services like garbage  
collection or monitoring that shouldn't keep the application alive.  


## Thread priority  

Thread priority hints to the scheduler about the relative importance of threads,  
though behavior is platform-dependent.  

```java
void main() throws InterruptedException {

    Runnable task = () -> {
        var name = Thread.currentThread().getName();
        var priority = Thread.currentThread().getPriority();
        println(name + " with priority " + priority + " running");
    };

    var lowPriority = new Thread(task, "LowPriority");
    var normalPriority = new Thread(task, "NormalPriority");
    var highPriority = new Thread(task, "HighPriority");

    lowPriority.setPriority(Thread.MIN_PRIORITY);
    normalPriority.setPriority(Thread.NORM_PRIORITY);
    highPriority.setPriority(Thread.MAX_PRIORITY);

    lowPriority.start();
    normalPriority.start();
    highPriority.start();

    lowPriority.join();
    normalPriority.join();
    highPriority.join();
}
```

Priority values range from 1 (MIN_PRIORITY) to 10 (MAX_PRIORITY), with 5  
(NORM_PRIORITY) as default. Higher priority threads may get more CPU time, but  
this is not guaranteed and depends on the OS scheduler.  


## Race condition demonstration  

A race condition occurs when multiple threads access shared data without proper  
synchronization.  

```java
void main() throws InterruptedException {

    var counter = new int[]{0};
    var threads = new ArrayList<Thread>();

    for (int i = 0; i < 10; i++) {
        var thread = new Thread(() -> {
            for (int j = 0; j < 1000; j++) {
                counter[0]++;
            }
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    println("Expected: 10000, Actual: " + counter[0]);
}
```

The increment operation `counter[0]++` is not atomic; it involves read, modify,  
and write steps. Without synchronization, threads may overwrite each other's  
updates, resulting in a count less than expected.  


## Synchronized method  

The `synchronized` keyword ensures that only one thread can execute a method  
on an object at a time.  

```java
class Counter {
    private int count = 0;

    synchronized void increment() {
        count++;
    }

    synchronized int getCount() {
        return count;
    }
}

void main() throws InterruptedException {

    var counter = new Counter();
    var threads = new ArrayList<Thread>();

    for (int i = 0; i < 10; i++) {
        var thread = new Thread(() -> {
            for (int j = 0; j < 1000; j++) {
                counter.increment();
            }
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    println("Count: " + counter.getCount());
}
```

The `synchronized` keyword acquires the object's intrinsic lock before entering  
the method. This ensures mutual exclusion—only one thread can execute any  
synchronized method on the same object at a time.  


## Synchronized block  

Synchronized blocks allow finer-grained locking by synchronizing only the  
critical section rather than the entire method.  

```java
class BankAccount {
    private int balance = 1000;
    private final Object lock = new Object();

    void withdraw(int amount) {
        synchronized (lock) {
            if (balance >= amount) {
                balance -= amount;
                println(Thread.currentThread().getName() + 
                    " withdrew " + amount + ", balance: " + balance);
            }
        }
    }

    int getBalance() {
        synchronized (lock) {
            return balance;
        }
    }
}

void main() throws InterruptedException {

    var account = new BankAccount();
    var threads = new ArrayList<Thread>();

    for (int i = 0; i < 5; i++) {
        var thread = new Thread(() -> {
            account.withdraw(100);
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    println("Final balance: " + account.getBalance());
}
```

Using a private lock object is preferred over `synchronized(this)` because it  
prevents external code from acquiring the same lock. This provides better  
encapsulation and reduces the risk of unintended lock contention.  


## Volatile keyword  

The `volatile` keyword ensures visibility of changes across threads but does not  
provide atomicity.  

```java
class Flag {
    volatile boolean running = true;
}

void main() throws InterruptedException {

    var flag = new Flag();

    var worker = new Thread(() -> {
        long count = 0;
        while (flag.running) {
            count++;
        }
        println("Counted to: " + count);
    });

    worker.start();
    Thread.sleep(100);
    flag.running = false;
    worker.join();
    println("Worker stopped");
}
```

Without `volatile`, the worker thread might cache the `running` variable and  
never see the update. The `volatile` keyword forces reads and writes to go  
directly to main memory, ensuring visibility across threads.  


## AtomicInteger  

`AtomicInteger` provides thread-safe operations on integers without explicit  
synchronization.  

```java
import java.util.concurrent.atomic.AtomicInteger;

void main() throws InterruptedException {

    var counter = new AtomicInteger(0);
    var threads = new ArrayList<Thread>();

    for (int i = 0; i < 10; i++) {
        var thread = new Thread(() -> {
            for (int j = 0; j < 1000; j++) {
                counter.incrementAndGet();
            }
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    println("Count: " + counter.get());
}
```

`AtomicInteger` uses compare-and-swap (CAS) operations implemented at the  
hardware level. This is more efficient than synchronization for simple atomic  
operations and is lock-free, avoiding potential deadlocks.  


## AtomicLong and AtomicBoolean  

Other atomic classes provide thread-safe operations for different primitive  
types.  

```java
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicBoolean;

void main() throws InterruptedException {

    var sum = new AtomicLong(0);
    var isComplete = new AtomicBoolean(false);

    var threads = new ArrayList<Thread>();

    for (int i = 1; i <= 5; i++) {
        int value = i * 100;
        var thread = new Thread(() -> {
            sum.addAndGet(value);
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    isComplete.set(true);
    println("Sum: " + sum.get());
    println("Complete: " + isComplete.get());
}
```

`AtomicLong` is useful for counters and accumulators in concurrent code.  
`AtomicBoolean` is commonly used as a thread-safe flag for signaling between  
threads, such as for graceful shutdown.  


## AtomicReference  

`AtomicReference` provides atomic operations on object references, enabling  
lock-free algorithms.  

```java
import java.util.concurrent.atomic.AtomicReference;

record User(String name, int age) {}

void main() throws InterruptedException {

    var userRef = new AtomicReference<>(new User("Alice", 25));

    var updater = new Thread(() -> {
        var current = userRef.get();
        var updated = new User(current.name(), current.age() + 1);
        userRef.compareAndSet(current, updated);
    });

    updater.start();
    updater.join();

    println("Updated user: " + userRef.get());
}
```

`compareAndSet` atomically updates the reference only if it equals the expected  
value. This is the foundation of many lock-free data structures and algorithms,  
enabling safe concurrent updates without locks.  


## ReentrantLock  

`ReentrantLock` provides more flexibility than synchronized blocks, including  
tryLock, timed lock, and interruptible lock acquisition.  

```java
import java.util.concurrent.locks.ReentrantLock;

void main() throws InterruptedException {

    var lock = new ReentrantLock();
    var counter = new int[]{0};

    Runnable task = () -> {
        for (int i = 0; i < 1000; i++) {
            lock.lock();
            try {
                counter[0]++;
            } finally {
                lock.unlock();
            }
        }
    };

    var threads = new ArrayList<Thread>();
    for (int i = 0; i < 10; i++) {
        var thread = new Thread(task);
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    println("Count: " + counter[0]);
}
```

Unlike `synchronized`, `ReentrantLock` must be explicitly unlocked in a finally  
block to ensure release even if an exception occurs. The lock is reentrant,  
meaning the same thread can acquire it multiple times.  


## TryLock  

The `tryLock` method attempts to acquire a lock without blocking, useful for  
avoiding deadlocks.  

```java
import java.util.concurrent.locks.ReentrantLock;

void main() throws InterruptedException {

    var lock = new ReentrantLock();

    Runnable task = () -> {
        if (lock.tryLock()) {
            try {
                println(Thread.currentThread().getName() + " acquired lock");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        } else {
            println(Thread.currentThread().getName() + " could not acquire lock");
        }
    };

    var t1 = new Thread(task, "Thread-1");
    var t2 = new Thread(task, "Thread-2");

    t1.start();
    Thread.sleep(100);
    t2.start();

    t1.join();
    t2.join();
}
```

`tryLock` returns immediately with `true` if the lock was acquired, or `false`  
if not. This enables non-blocking algorithms and timeout-based lock acquisition  
to prevent threads from waiting indefinitely.  


## ReadWriteLock  

`ReadWriteLock` allows multiple readers or a single writer, optimizing for  
read-heavy workloads.  

```java
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Cache {
    private final Map<String, String> data = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    String get(String key) {
        lock.readLock().lock();
        try {
            return data.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    void put(String key, String value) {
        lock.writeLock().lock();
        try {
            data.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }
}

void main() throws InterruptedException {

    var cache = new Cache();
    cache.put("key1", "value1");

    var threads = new ArrayList<Thread>();

    for (int i = 0; i < 5; i++) {
        int num = i;
        var reader = new Thread(() -> {
            println("Reader " + num + ": " + cache.get("key1"));
        });
        threads.add(reader);
        reader.start();
    }

    for (var thread : threads) {
        thread.join();
    }
}
```

Multiple threads can hold the read lock simultaneously as long as no thread  
holds the write lock. This significantly improves throughput for data structures  
that are read more often than written.  


## Condition variables  

`Condition` provides a means for one thread to suspend execution until notified  
by another thread.  

```java
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

void main() throws InterruptedException {

    var lock = new ReentrantLock();
    var condition = lock.newCondition();
    var ready = new boolean[]{false};

    var waiter = new Thread(() -> {
        lock.lock();
        try {
            while (!ready[0]) {
                println("Waiting...");
                condition.await();
            }
            println("Received signal!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    });

    var signaler = new Thread(() -> {
        lock.lock();
        try {
            Thread.sleep(1000);
            ready[0] = true;
            condition.signal();
            println("Signal sent!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    });

    waiter.start();
    Thread.sleep(100);
    signaler.start();

    waiter.join();
    signaler.join();
}
```

`Condition` is associated with a lock and must be used while holding that lock.  
Unlike `Object.wait/notify`, multiple conditions can be associated with a single  
lock, providing finer-grained control.  


## ExecutorService with fixed thread pool  

`ExecutorService` manages a pool of threads, abstracting thread lifecycle  
management.  

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    ExecutorService executor = Executors.newFixedThreadPool(3);

    for (int i = 1; i <= 6; i++) {
        int taskId = i;
        executor.submit(() -> {
            println("Task " + taskId + " running on " + 
                Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);
    println("All tasks completed");
}
```

A fixed thread pool reuses a fixed number of threads, queuing tasks when all  
threads are busy. This prevents resource exhaustion from creating too many  
threads and improves performance through thread reuse.  


## Cached thread pool  

A cached thread pool creates new threads as needed and reuses idle threads.  

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    ExecutorService executor = Executors.newCachedThreadPool();

    for (int i = 1; i <= 10; i++) {
        int taskId = i;
        executor.submit(() -> {
            println("Task " + taskId + " on " + 
                Thread.currentThread().getName());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);
}
```

Cached thread pools are suitable for executing many short-lived tasks. Idle  
threads are terminated after 60 seconds. However, for unbounded task submission,  
this can create too many threads and exhaust system resources.  


## Single thread executor  

A single thread executor ensures tasks execute sequentially in submission order.  

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    for (int i = 1; i <= 5; i++) {
        int taskId = i;
        executor.submit(() -> {
            println("Processing task " + taskId);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);
    println("All tasks processed in order");
}
```

Single thread executors guarantee sequential execution and are useful when tasks  
must not run concurrently, such as writing to a log file or processing messages  
in order.  


## ScheduledExecutorService  

`ScheduledExecutorService` executes tasks after a delay or periodically.  

```java
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    scheduler.schedule(() -> {
        println("Delayed task executed after 2 seconds");
    }, 2, TimeUnit.SECONDS);

    scheduler.scheduleAtFixedRate(() -> {
        println("Fixed rate task: " + System.currentTimeMillis());
    }, 0, 1, TimeUnit.SECONDS);

    Thread.sleep(5000);
    scheduler.shutdown();
}
```

`scheduleAtFixedRate` runs tasks at fixed intervals regardless of task duration.  
`scheduleWithFixedDelay` waits a fixed delay after each task completes before  
starting the next. Both are useful for periodic maintenance tasks.  


## Callable and Future  

`Callable` returns a result and can throw exceptions, unlike `Runnable`.  
`Future` represents the pending result.  

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

void main() throws Exception {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    Callable<Integer> task = () -> {
        Thread.sleep(1000);
        return 42;
    };

    Future<Integer> future = executor.submit(task);
    println("Task submitted, doing other work...");

    Integer result = future.get();
    println("Result: " + result);

    executor.shutdown();
}
```

`Future.get` blocks until the result is available. It throws  
`ExecutionException` if the task threw an exception, or `CancellationException`  
if the task was cancelled. This enables asynchronous computation with results.  


## Future with timeout  

The `get` method can specify a timeout to avoid blocking indefinitely.  

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

void main() throws Exception {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    Future<String> future = executor.submit(() -> {
        Thread.sleep(5000);
        return "Result";
    });

    try {
        String result = future.get(2, TimeUnit.SECONDS);
        println("Result: " + result);
    } catch (TimeoutException e) {
        println("Task timed out!");
        future.cancel(true);
    }

    executor.shutdown();
}
```

When a timeout occurs, the task continues running unless explicitly cancelled.  
Calling `cancel(true)` interrupts the running task. This pattern is essential  
for preventing operations from hanging indefinitely.  


## InvokeAll  

`invokeAll` submits multiple callables and returns when all complete.  

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

void main() throws Exception {

    ExecutorService executor = Executors.newFixedThreadPool(3);

    var tasks = List.<Callable<Integer>>of(
        () -> { Thread.sleep(1000); return 1; },
        () -> { Thread.sleep(2000); return 2; },
        () -> { Thread.sleep(1500); return 3; }
    );

    var futures = executor.invokeAll(tasks);

    int sum = 0;
    for (var future : futures) {
        sum += future.get();
    }

    println("Sum of all results: " + sum);
    executor.shutdown();
}
```

`invokeAll` blocks until all tasks complete or the timeout expires. It's useful  
when you need to wait for multiple independent computations and aggregate their  
results, such as fetching data from multiple sources.  


## InvokeAny  

`invokeAny` returns the result of the fastest completing task, cancelling the  
others.  

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

void main() throws Exception {

    ExecutorService executor = Executors.newFixedThreadPool(3);

    var tasks = List.<Callable<String>>of(
        () -> { Thread.sleep(3000); return "Server A"; },
        () -> { Thread.sleep(1000); return "Server B"; },
        () -> { Thread.sleep(2000); return "Server C"; }
    );

    String fastest = executor.invokeAny(tasks);
    println("Fastest response from: " + fastest);

    executor.shutdown();
}
```

This is useful for redundant operations where you need any successful result  
quickly, such as querying multiple servers. Once one task completes, the others  
are cancelled to conserve resources.  


## CompletableFuture basics  

`CompletableFuture` provides a powerful API for asynchronous programming with  
chaining and combining.  

```java
import java.util.concurrent.CompletableFuture;

void main() throws Exception {

    var future = CompletableFuture.supplyAsync(() -> {
        println("Computing in: " + Thread.currentThread().getName());
        return 42;
    });

    future.thenAccept(result -> {
        println("Result: " + result);
    });

    Thread.sleep(1000);
}
```

`supplyAsync` runs the supplier in the common fork-join pool. `thenAccept`  
registers a callback that executes when the future completes. This enables  
non-blocking asynchronous programming without explicit thread management.  


## CompletableFuture chaining  

Multiple stages can be chained together for complex asynchronous workflows.  

```java
import java.util.concurrent.CompletableFuture;

void main() throws Exception {

    CompletableFuture.supplyAsync(() -> "Hello")
        .thenApply(s -> s + " World")
        .thenApply(String::toUpperCase)
        .thenAccept(result -> println(result))
        .join();
}
```

Each `thenApply` transforms the result from the previous stage. The chain  
executes asynchronously, with each stage starting when the previous completes.  
`join` blocks until the entire chain finishes.  


## CompletableFuture combining  

Multiple futures can be combined to create complex async workflows.  

```java
import java.util.concurrent.CompletableFuture;

void main() throws Exception {

    var future1 = CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        return "Hello";
    });

    var future2 = CompletableFuture.supplyAsync(() -> {
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        return "World";
    });

    future1.thenCombine(future2, (s1, s2) -> s1 + " " + s2)
        .thenAccept(result -> println(result))
        .join();
}
```

`thenCombine` waits for both futures and combines their results. This is useful  
when you need to merge results from independent async operations, like fetching  
user data and preferences simultaneously.  


## CompletableFuture exception handling  

Exceptions in async chains can be handled with `exceptionally` or `handle`.  

```java
import java.util.concurrent.CompletableFuture;

void main() throws Exception {

    CompletableFuture.supplyAsync(() -> {
        if (true) throw new RuntimeException("Something went wrong!");
        return "Result";
    })
    .exceptionally(ex -> {
        println("Exception: " + ex.getMessage());
        return "Default Value";
    })
    .thenAccept(result -> println("Result: " + result))
    .join();
}
```

`exceptionally` catches exceptions from any previous stage and provides a  
fallback value. This enables graceful error handling in async chains without  
breaking the flow or requiring try-catch blocks.  


## CompletableFuture allOf  

`allOf` waits for multiple futures to complete.  

```java
import java.util.concurrent.CompletableFuture;

void main() throws Exception {

    var future1 = CompletableFuture.runAsync(() -> {
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        println("Task 1 done");
    });

    var future2 = CompletableFuture.runAsync(() -> {
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        println("Task 2 done");
    });

    var future3 = CompletableFuture.runAsync(() -> {
        try { Thread.sleep(750); } catch (InterruptedException e) {}
        println("Task 3 done");
    });

    CompletableFuture.allOf(future1, future2, future3).join();
    println("All tasks completed");
}
```

Unlike `invokeAll`, `allOf` returns a `CompletableFuture<Void>`. To get results,  
you must call `get` on individual futures after `allOf` completes. This is  
useful for waiting on multiple async operations.  


## ConcurrentHashMap  

`ConcurrentHashMap` provides thread-safe operations without locking the entire  
map.  

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    var map = new ConcurrentHashMap<String, Integer>();

    ExecutorService executor = Executors.newFixedThreadPool(4);

    for (int i = 0; i < 1000; i++) {
        int num = i;
        executor.submit(() -> {
            map.put("key" + (num % 10), num);
            map.compute("counter", (k, v) -> v == null ? 1 : v + 1);
        });
    }

    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    println("Map size: " + map.size());
    println("Counter: " + map.get("counter"));
}
```

`ConcurrentHashMap` uses fine-grained locking (or lock-free operations in modern  
versions) to allow concurrent reads and writes. Atomic operations like `compute`  
ensure thread-safe updates without external synchronization.  


## CopyOnWriteArrayList  

`CopyOnWriteArrayList` creates a new copy on each modification, ideal for  
read-heavy scenarios.  

```java
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    var list = new CopyOnWriteArrayList<String>();
    list.add("initial");

    ExecutorService executor = Executors.newFixedThreadPool(4);

    for (int i = 0; i < 10; i++) {
        int num = i;
        executor.submit(() -> {
            list.add("item" + num);
        });
    }

    executor.submit(() -> {
        for (String item : list) {
            println("Reading: " + item);
        }
    });

    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    println("Final size: " + list.size());
}
```

Iteration never throws `ConcurrentModificationException` because iterators  
work on a snapshot. This is efficient when reads greatly outnumber writes, as  
writes incur the cost of copying the entire array.  


## BlockingQueue  

`BlockingQueue` provides thread-safe queue operations with blocking methods for  
producer-consumer patterns.  

```java
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

void main() throws InterruptedException {

    BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

    var producer = new Thread(() -> {
        try {
            for (int i = 1; i <= 10; i++) {
                String item = "Item-" + i;
                queue.put(item);
                println("Produced: " + item);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });

    var consumer = new Thread(() -> {
        try {
            for (int i = 0; i < 10; i++) {
                String item = queue.take();
                println("Consumed: " + item);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });

    producer.start();
    consumer.start();

    producer.join();
    consumer.join();
}
```

`put` blocks when the queue is full, and `take` blocks when empty. This  
automatically coordinates producer and consumer threads without explicit  
synchronization, implementing backpressure naturally.  


## CountDownLatch  

`CountDownLatch` allows threads to wait until a count reaches zero.  

```java
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

void main() throws InterruptedException {

    var latch = new CountDownLatch(3);
    ExecutorService executor = Executors.newFixedThreadPool(3);

    for (int i = 1; i <= 3; i++) {
        int taskId = i;
        executor.submit(() -> {
            try {
                Thread.sleep((long) (Math.random() * 2000));
                println("Task " + taskId + " completed");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        });
    }

    println("Waiting for tasks...");
    latch.await();
    println("All tasks completed!");

    executor.shutdown();
}
```

`countDown` decrements the count, and `await` blocks until it reaches zero.  
Unlike `join`, multiple threads can wait on the same latch, and it can count  
multiple completions from the same thread.  


## CyclicBarrier  

`CyclicBarrier` synchronizes a fixed number of threads at a common barrier  
point.  

```java
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

void main() throws InterruptedException {

    var barrier = new CyclicBarrier(3, () -> {
        println("All parties arrived at barrier!");
    });

    ExecutorService executor = Executors.newFixedThreadPool(3);

    for (int i = 1; i <= 3; i++) {
        int partyId = i;
        executor.submit(() -> {
            try {
                println("Party " + partyId + " working...");
                Thread.sleep((long) (Math.random() * 2000));
                println("Party " + partyId + " waiting at barrier");
                barrier.await();
                println("Party " + partyId + " passed barrier");
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    Thread.sleep(5000);
    executor.shutdown();
}
```

Unlike `CountDownLatch`, a barrier can be reused after all threads pass. The  
barrier action runs when all parties arrive. This is useful for iterative  
algorithms where threads must synchronize at each step.  


## Semaphore  

`Semaphore` controls access to a limited number of resources through permits.  

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    var semaphore = new Semaphore(3);
    ExecutorService executor = Executors.newFixedThreadPool(10);

    for (int i = 1; i <= 10; i++) {
        int taskId = i;
        executor.submit(() -> {
            try {
                semaphore.acquire();
                println("Task " + taskId + " acquired permit");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                println("Task " + taskId + " releasing permit");
                semaphore.release();
            }
        });
    }

    executor.shutdown();
    executor.awaitTermination(30, TimeUnit.SECONDS);
}
```

Semaphores limit concurrent access to a resource pool, such as database  
connections or API rate limits. Only three tasks run simultaneously, with others  
waiting until a permit is released.  


## Phaser  

`Phaser` is a flexible synchronization barrier supporting dynamic party  
registration.  

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    var phaser = new Phaser(1);
    ExecutorService executor = Executors.newFixedThreadPool(3);

    for (int i = 1; i <= 3; i++) {
        phaser.register();
        int partyId = i;
        executor.submit(() -> {
            for (int phase = 0; phase < 3; phase++) {
                println("Party " + partyId + " phase " + phase);
                phaser.arriveAndAwaitAdvance();
            }
            phaser.arriveAndDeregister();
        });
    }

    phaser.arriveAndDeregister();
    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);
}
```

Unlike `CyclicBarrier`, parties can dynamically register and deregister.  
`Phaser` supports multiple phases and can be used for complex multi-phase  
computations where the number of participants may vary.  


## Exchanger  

`Exchanger` allows two threads to exchange data at a synchronization point.  

```java
import java.util.concurrent.Exchanger;

void main() throws InterruptedException {

    var exchanger = new Exchanger<String>();

    var thread1 = new Thread(() -> {
        try {
            String data = "Data from Thread 1";
            println("Thread 1 sending: " + data);
            String received = exchanger.exchange(data);
            println("Thread 1 received: " + received);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });

    var thread2 = new Thread(() -> {
        try {
            Thread.sleep(1000);
            String data = "Data from Thread 2";
            println("Thread 2 sending: " + data);
            String received = exchanger.exchange(data);
            println("Thread 2 received: " + received);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();
}
```

Both threads block at `exchange` until the partner arrives. This is useful for  
genetic algorithms, pipeline designs, or any scenario where two threads need  
to swap data synchronously.  


## ForkJoinPool  

`ForkJoinPool` is optimized for divide-and-conquer algorithms using work  
stealing.  

```java
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class SumTask extends RecursiveTask<Long> {
    private final long[] array;
    private final int start, end;
    private static final int THRESHOLD = 1000;

    SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        }
        int mid = (start + end) / 2;
        var left = new SumTask(array, start, mid);
        var right = new SumTask(array, mid, end);
        left.fork();
        return right.compute() + left.join();
    }
}

void main() {

    var array = new long[10000];
    for (int i = 0; i < array.length; i++) {
        array[i] = i + 1;
    }

    var pool = ForkJoinPool.commonPool();
    long sum = pool.invoke(new SumTask(array, 0, array.length));

    println("Sum: " + sum);
}
```

`RecursiveTask` splits work into smaller chunks until reaching a threshold.  
Work stealing allows idle threads to take tasks from busy threads' queues,  
maximizing CPU utilization for parallel algorithms.  


## Virtual threads basics  

Virtual threads (Project Loom) are lightweight threads managed by the JVM rather  
than the OS.  

```java
void main() throws InterruptedException {

    var threads = new ArrayList<Thread>();

    for (int i = 1; i <= 10; i++) {
        int taskId = i;
        var thread = Thread.startVirtualThread(() -> {
            println("Virtual thread " + taskId + " running");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            println("Virtual thread " + taskId + " done");
        });
        threads.add(thread);
    }

    for (var thread : threads) {
        thread.join();
    }
}
```

Virtual threads are cheap to create—millions can exist simultaneously. They  
automatically unmount from carrier threads during blocking operations, enabling  
high-throughput concurrent applications without the complexity of async code.  


## Virtual thread executor  

`Executors.newVirtualThreadPerTaskExecutor` creates a new virtual thread for  
each task.  

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 1; i <= 100; i++) {
            int taskId = i;
            executor.submit(() -> {
                println("Task " + taskId + " on " + Thread.currentThread());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    println("All tasks completed");
}
```

This executor creates virtual threads on demand, suitable for I/O-bound  
workloads with many concurrent tasks. The try-with-resources ensures proper  
shutdown. Virtual threads make thread-per-request architectures practical.  


## Structured concurrency  

Structured concurrency ensures child tasks complete before the parent scope  
exits.  

```java
import java.util.concurrent.StructuredTaskScope;

void main() throws Exception {

    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        var user = scope.fork(() -> fetchUser());
        var order = scope.fork(() -> fetchOrder());

        scope.join();
        scope.throwIfFailed();

        println("User: " + user.get());
        println("Order: " + order.get());
    }
}

String fetchUser() throws InterruptedException {
    Thread.sleep(500);
    return "Alice";
}

String fetchOrder() throws InterruptedException {
    Thread.sleep(300);
    return "Order #12345";
}
```

`StructuredTaskScope` binds the lifetime of forked tasks to the scope.  
`ShutdownOnFailure` cancels remaining tasks if one fails. This simplifies  
error handling and prevents orphaned threads.  


## Thread-local storage  

`ThreadLocal` provides thread-confined variables, each thread having its own  
independent copy.  

```java
void main() throws InterruptedException {

    ThreadLocal<String> userContext = ThreadLocal.withInitial(() -> "unknown");

    var threads = new ArrayList<Thread>();

    for (int i = 1; i <= 3; i++) {
        String userId = "user-" + i;
        var thread = new Thread(() -> {
            userContext.set(userId);
            println(Thread.currentThread().getName() + 
                " context: " + userContext.get());
            processRequest();
            println(Thread.currentThread().getName() + 
                " still: " + userContext.get());
            userContext.remove();
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }
}

void processRequest() {
    try {
        Thread.sleep(100);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}
```

Each thread sees its own value without interference. `ThreadLocal` is useful  
for per-request context in web applications. Always call `remove` to prevent  
memory leaks in thread pools.  


## Deadlock demonstration  

Deadlock occurs when threads wait on each other in a circular dependency.  

```java
void main() throws InterruptedException {

    var lock1 = new Object();
    var lock2 = new Object();

    var thread1 = new Thread(() -> {
        synchronized (lock1) {
            println("Thread 1: Holding lock 1");
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            println("Thread 1: Waiting for lock 2");
            synchronized (lock2) {
                println("Thread 1: Holding both locks");
            }
        }
    });

    var thread2 = new Thread(() -> {
        synchronized (lock2) {
            println("Thread 2: Holding lock 2");
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            println("Thread 2: Waiting for lock 1");
            synchronized (lock1) {
                println("Thread 2: Holding both locks");
            }
        }
    });

    thread1.start();
    thread2.start();

    thread1.join(2000);
    thread2.join(2000);

    if (thread1.isAlive() || thread2.isAlive()) {
        println("Deadlock detected!");
    }
}
```

Thread 1 holds lock1 and waits for lock2, while Thread 2 holds lock2 and waits  
for lock1. Prevent deadlocks by acquiring locks in a consistent order, using  
timeouts, or avoiding nested locks.  


## Parallel streams  

Parallel streams use the fork-join pool to process elements concurrently.  

```java
void main() {

    var numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    long sum = numbers.parallelStream()
        .peek(n -> println("Processing " + n + " on " + 
            Thread.currentThread().getName()))
        .mapToLong(n -> n * n)
        .sum();

    println("Sum of squares: " + sum);
}
```

Parallel streams automatically partition data and process partitions in  
parallel. They're effective for CPU-intensive operations on large datasets but  
have overhead that can hurt performance for small collections.  


## Thread-safe lazy initialization  

Double-checked locking with volatile provides efficient lazy initialization.  

```java
class ExpensiveObject {
    ExpensiveObject() {
        println("Creating expensive object...");
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }
}

class Singleton {
    private static volatile ExpensiveObject instance;

    static ExpensiveObject getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new ExpensiveObject();
                }
            }
        }
        return instance;
    }
}

void main() throws InterruptedException {

    var threads = new ArrayList<Thread>();

    for (int i = 0; i < 5; i++) {
        var thread = new Thread(() -> {
            var obj = Singleton.getInstance();
            println("Got instance: " + obj);
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }
}
```

The outer null check avoids synchronization overhead after initialization. The  
`volatile` keyword ensures proper visibility of the fully constructed object.  
This pattern is mostly unnecessary with the simpler holder class idiom.  
