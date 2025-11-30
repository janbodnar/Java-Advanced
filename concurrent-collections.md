# Concurrent Collections in Java

Concurrent collections are specialized data structures designed for safe and  
efficient access by multiple threads simultaneously. Unlike traditional  
collections from the Java Collections Framework, concurrent collections provide  
built-in thread safety without requiring external synchronization, while  
offering significantly better performance than using synchronized wrappers.  

In multithreaded applications, data structures are frequently shared across  
threads. Traditional collections like `HashMap`, `ArrayList`, and `LinkedList`  
are not thread-safe—concurrent modifications can lead to data corruption, lost  
updates, or `ConcurrentModificationException`. While wrapping these collections  
with `Collections.synchronizedList` or `Collections.synchronizedMap` provides  
basic thread safety, it creates a performance bottleneck by forcing exclusive  
access during any operation.  

Concurrent collections solve these problems through sophisticated internal  
synchronization mechanisms that allow multiple threads to read and often write  
simultaneously. They use techniques like lock striping, copy-on-write semantics,  
and lock-free algorithms to maximize throughput while maintaining correctness.  

## Why Traditional Collections Fail in Concurrent Environments  

Standard Java collections are designed for single-threaded access. When multiple  
threads access them simultaneously, several problems can occur:  

| Problem | Description |
|---------|-------------|
| Data Corruption | Concurrent modifications can leave the data structure in an inconsistent state |
| Lost Updates | One thread's changes may overwrite another's without either being aware |
| ConcurrentModificationException | Iterating while another thread modifies throws this exception |
| Race Conditions | Non-atomic compound operations lead to unpredictable behavior |
| Visibility Issues | Changes made by one thread may not be visible to others |

```java
void main() throws InterruptedException {

    // UNSAFE: HashMap is not thread-safe
    var map = new HashMap<String, Integer>();
    var threads = new ArrayList<Thread>();

    for (int i = 0; i < 10; i++) {
        int threadNum = i;
        var thread = new Thread(() -> {
            for (int j = 0; j < 1000; j++) {
                String key = "key" + (j % 100);
                // This compound operation is not atomic!
                map.merge(key, 1, Integer::sum);
            }
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    // Expected: 10 * 1000 / 100 * 100 = 10000 per key average
    // Actual: Usually less due to race conditions
    int total = map.values().stream().mapToInt(Integer::intValue).sum();
    println("Total count: " + total + " (expected 10000)");
}
```

This example demonstrates the dangers of using a non-thread-safe collection in  
a concurrent environment. The `merge` operation involves reading, computing,  
and writing—multiple threads can interleave these steps, causing lost updates  
and incorrect final counts.  

## ConcurrentHashMap  

`ConcurrentHashMap` is the most widely used concurrent collection, providing a  
thread-safe hash table implementation with excellent performance. Unlike  
`Hashtable`, which synchronizes every method, `ConcurrentHashMap` uses  
fine-grained locking and lock-free reads to enable high concurrency.  

### Design and Evolution  

The design of `ConcurrentHashMap` has evolved significantly across Java  
versions:  

**Java 1.5-7 (Segment-based locking)**: The map was divided into 16 segments  
(by default), each acting as an independent hash table with its own lock.  
This allowed 16 threads to write simultaneously if they accessed different  
segments.  

**Java 8+ (CAS and bucket-level synchronization)**: The segment design was  
replaced with a more efficient approach. Reads are lock-free using volatile  
reads. Writes use CAS (Compare-And-Swap) for empty buckets and synchronized  
blocks on the first node for occupied buckets. This provides finer granularity  
and better scalability.  

| Feature | HashMap | Hashtable | ConcurrentHashMap |
|---------|---------|-----------|-------------------|
| Thread-safe | No | Yes | Yes |
| Null keys/values | Yes | No | No |
| Locking | None | Method-level | Bucket-level/CAS |
| Iteration | Fail-fast | Fail-fast | Weakly consistent |
| Performance (concurrent) | N/A | Poor | Excellent |

### Basic operations  

`ConcurrentHashMap` supports all standard map operations with full thread  
safety.  

```java
import java.util.concurrent.ConcurrentHashMap;

void main() {

    var map = new ConcurrentHashMap<String, Integer>();

    // Basic put and get
    map.put("apple", 10);
    map.put("banana", 20);
    map.put("cherry", 30);

    println("Apple count: " + map.get("apple"));
    println("Contains banana: " + map.containsKey("banana"));
    println("Map size: " + map.size());

    // Thread-safe iteration (weakly consistent)
    for (var entry : map.entrySet()) {
        println(entry.getKey() + " -> " + entry.getValue());
    }
}
```

Unlike synchronized collections, iterating over a `ConcurrentHashMap` never  
throws `ConcurrentModificationException`. The iterator reflects the state of  
the map at some point during or after iteration begins. This "weakly  
consistent" behavior is a deliberate trade-off for better concurrency.  

### Atomic compound operations  

`ConcurrentHashMap` provides several methods that perform atomic compound  
operations, eliminating the need for external synchronization.  

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    var wordCount = new ConcurrentHashMap<String, Integer>();
    ExecutorService executor = Executors.newFixedThreadPool(4);

    var words = List.of("apple", "banana", "apple", "cherry", 
        "banana", "apple", "date", "cherry", "apple");

    for (var word : words) {
        executor.submit(() -> {
            // Atomic increment - no external synchronization needed
            wordCount.merge(word, 1, Integer::sum);
        });
    }

    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    println("Word counts:");
    wordCount.forEach((word, count) -> 
        println("  " + word + ": " + count));
}
```

The `merge` method atomically combines the existing value with a new value  
using the provided function. Other atomic operations include `compute`,  
`computeIfAbsent`, and `computeIfPresent`. These eliminate race conditions  
that would occur with separate get-then-put operations.  

### ComputeIfAbsent for lazy initialization  

The `computeIfAbsent` method is particularly useful for implementing  
thread-safe lazy caches.  

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    var cache = new ConcurrentHashMap<String, String>();
    ExecutorService executor = Executors.newFixedThreadPool(10);

    for (int i = 0; i < 20; i++) {
        int requestNum = i;
        executor.submit(() -> {
            // Expensive computation runs only once per key
            String result = cache.computeIfAbsent("user:123", key -> {
                println("Computing value for " + key + 
                    " (request " + requestNum + ")");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                return "UserData{id=123, name='Alice'}";
            });
            println("Request " + requestNum + " got: " + result);
        });
    }

    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);
}
```

Despite 20 concurrent requests, the expensive computation runs only once.  
While one thread computes the value, other threads requesting the same key  
block until the computation completes, then receive the cached result.  

### Bulk operations  

Java 8 added powerful bulk operations that execute in parallel across the map.  

```java
import java.util.concurrent.ConcurrentHashMap;

void main() {

    var scores = new ConcurrentHashMap<String, Integer>();
    scores.put("Alice", 85);
    scores.put("Bob", 92);
    scores.put("Charlie", 78);
    scores.put("Diana", 95);
    scores.put("Eve", 88);

    // forEach with parallelism threshold
    println("All scores (parallel if > 3 elements):");
    scores.forEach(1, (name, score) -> 
        println("  " + name + ": " + score + 
            " [" + Thread.currentThread().getName() + "]"));

    // search - find first matching entry
    String topScorer = scores.search(1, (name, score) -> 
        score > 90 ? name : null);
    println("First scorer above 90: " + topScorer);

    // reduce - aggregate all values
    int total = scores.reduce(1, 
        (name, score) -> score,
        Integer::sum);
    println("Total score: " + total);

    // reduceValues - simpler when you only need values
    int max = scores.reduceValues(1, Integer::max);
    println("Highest score: " + max);
}
```

The first parameter (parallelism threshold) controls when operations run in  
parallel. A threshold of 1 means always parallelize; Long.MAX_VALUE means  
always run sequentially. These operations are efficient because they can  
process different portions of the map concurrently.  

## CopyOnWriteArrayList  

`CopyOnWriteArrayList` is a thread-safe variant of `ArrayList` that creates a  
fresh copy of the underlying array on every modification. This design makes it  
ideal for scenarios where reads vastly outnumber writes.  

### Copy-on-write principle  

The copy-on-write strategy works as follows: reads access the current array  
directly without locking, while writes create a complete copy of the array,  
modify the copy, and atomically replace the reference to the underlying array.  

```java
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    var list = new CopyOnWriteArrayList<String>();
    list.addAll(List.of("A", "B", "C"));

    ExecutorService executor = Executors.newFixedThreadPool(4);

    // Writers create new copies
    executor.submit(() -> {
        list.add("D");
        println("Added D");
    });

    // Readers see consistent snapshots
    for (int i = 0; i < 3; i++) {
        int readerNum = i;
        executor.submit(() -> {
            println("Reader " + readerNum + " sees: " + list);
        });
    }

    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    println("Final list: " + list);
}
```

Each read operation sees a consistent snapshot of the list at some point in  
time. Modifications never interfere with ongoing reads because they work on  
a separate copy. The atomic reference swap ensures readers always see a  
complete, consistent state.  

### Safe iteration during modification  

One key advantage is that iterators never throw `ConcurrentModificationException`.  

```java
import java.util.concurrent.CopyOnWriteArrayList;

void main() {

    var listeners = new CopyOnWriteArrayList<String>();
    listeners.addAll(List.of("Listener1", "Listener2", "Listener3"));

    // Safe to iterate while modifying
    for (String listener : listeners) {
        println("Notifying: " + listener);

        if (listener.equals("Listener2")) {
            // This modification creates a new array
            // The iterator continues on the original array
            listeners.add("Listener4");
            listeners.remove("Listener1");
            println("  Modified list during iteration");
        }
    }

    println("After iteration: " + listeners);
}
```

The iterator operates on the snapshot that existed when iteration began.  
Any modifications create a new array, but the iterator continues on the  
original array. This is ideal for event listener patterns where callbacks  
might register or unregister listeners.  

### Performance characteristics  

Understanding the trade-offs is crucial for choosing this collection.  

```java
import java.util.concurrent.CopyOnWriteArrayList;

void main() {

    var list = new CopyOnWriteArrayList<Integer>();

    // Write performance: O(n) - copies entire array
    long writeStart = System.nanoTime();
    for (int i = 0; i < 1000; i++) {
        list.add(i);
    }
    long writeTime = System.nanoTime() - writeStart;

    // Read performance: O(1) for indexed access, no locking
    long readStart = System.nanoTime();
    for (int i = 0; i < 10000; i++) {
        int ignored = list.get(i % list.size());
    }
    long readTime = System.nanoTime() - readStart;

    println("1000 writes: " + writeTime / 1_000_000 + "ms");
    println("10000 reads: " + readTime / 1_000_000 + "ms");
    println("Read/Write ratio for efficiency: reads >> writes");
}
```

Writes are expensive because they copy the entire array. As the list grows,  
write operations become increasingly costly. Reads, however, are extremely  
fast—just an array index access with no synchronization overhead.  

### Use cases and anti-patterns  

`CopyOnWriteArrayList` excels in specific scenarios.  

```java
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

// GOOD: Event listeners (rarely changed, frequently invoked)
class EventBus {
    private final CopyOnWriteArrayList<Consumer<String>> listeners = 
        new CopyOnWriteArrayList<>();

    void subscribe(Consumer<String> listener) {
        listeners.add(listener);
    }

    void unsubscribe(Consumer<String> listener) {
        listeners.remove(listener);
    }

    void publish(String event) {
        // No synchronization needed during iteration
        for (var listener : listeners) {
            listener.accept(event);
        }
    }
}

void main() {

    var bus = new EventBus();

    // Listeners rarely change
    bus.subscribe(e -> println("Handler 1: " + e));
    bus.subscribe(e -> println("Handler 2: " + e));

    // But events are published frequently
    for (int i = 0; i < 5; i++) {
        bus.publish("Event " + i);
    }
}
```

Ideal use cases include event listeners, configuration holders, and caches  
that are read frequently but updated rarely. Avoid using it for lists that  
are modified frequently, as each modification copies the entire array.  

## BlockingQueue  

`BlockingQueue` is an interface that extends `Queue` with operations that  
block when the queue is empty (for takes) or full (for puts). This makes it  
perfect for producer-consumer patterns, where producers add items and  
consumers remove them, with automatic coordination between the two.  

### Understanding blocking behavior  

Blocking queues provide several types of operations with different behaviors.  

| Operation | Throws Exception | Returns Special Value | Blocks | Times Out |
|-----------|------------------|----------------------|--------|-----------|
| Insert | add(e) | offer(e) | put(e) | offer(e, time, unit) |
| Remove | remove() | poll() | take() | poll(time, unit) |
| Examine | element() | peek() | N/A | N/A |

```java
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

void main() throws InterruptedException {

    BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);

    // offer - returns false if queue is full (non-blocking)
    println("offer A: " + queue.offer("A"));
    println("offer B: " + queue.offer("B"));
    println("offer C: " + queue.offer("C"));
    println("offer D: " + queue.offer("D")); // false, queue full

    // poll - returns null if queue is empty (non-blocking)
    println("poll: " + queue.poll());
    println("poll: " + queue.poll());
    println("poll: " + queue.poll());
    println("poll: " + queue.poll()); // null, queue empty

    // put/take block until operation can proceed
    println("Queue operations demonstrated");
}
```

The blocking methods `put` and `take` are the most commonly used for  
producer-consumer patterns. They automatically handle coordination between  
threads, eliminating the need for explicit wait/notify logic.  

### Producer-consumer pattern  

The classic producer-consumer pattern is simplified with `BlockingQueue`.  

```java
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

void main() throws InterruptedException {

    BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

    // Producer thread
    var producer = new Thread(() -> {
        try {
            for (int i = 1; i <= 10; i++) {
                String item = "Item-" + i;
                queue.put(item); // Blocks if queue is full
                println("Produced: " + item + 
                    " (queue size: " + queue.size() + ")");
                Thread.sleep(100);
            }
            queue.put("DONE"); // Poison pill to signal completion
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });

    // Consumer thread
    var consumer = new Thread(() -> {
        try {
            while (true) {
                String item = queue.take(); // Blocks if queue is empty
                if ("DONE".equals(item)) break;
                println("Consumed: " + item);
                Thread.sleep(200); // Slower consumer
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });

    producer.start();
    consumer.start();

    producer.join();
    consumer.join();

    println("Producer-consumer completed");
}
```

The producer and consumer run at different speeds, but the blocking queue  
automatically coordinates them. When the queue fills up, the producer waits.  
When it empties, the consumer waits. This provides natural backpressure  
without explicit synchronization.  

### ArrayBlockingQueue  

`ArrayBlockingQueue` is a bounded queue backed by a fixed-size array.  

```java
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    // Fixed capacity - must be specified at creation
    BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

    ExecutorService executor = Executors.newFixedThreadPool(4);

    // Multiple producers
    for (int p = 0; p < 2; p++) {
        int producerId = p;
        executor.submit(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    int item = producerId * 100 + i;
                    queue.put(item);
                    println("Producer " + producerId + " put: " + item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    // Multiple consumers
    for (int c = 0; c < 2; c++) {
        int consumerId = c;
        executor.submit(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    Integer item = queue.take();
                    println("Consumer " + consumerId + " got: " + item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    executor.shutdown();
    executor.awaitTermination(30, TimeUnit.SECONDS);
}
```

`ArrayBlockingQueue` provides fair ordering when specified in the constructor.  
Fair mode ensures threads acquire locks in FIFO order but may reduce  
throughput. The bounded capacity prevents memory exhaustion when producers  
outpace consumers.  

### LinkedBlockingQueue  

`LinkedBlockingQueue` is an optionally bounded queue backed by linked nodes.  

```java
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;

void main() throws InterruptedException {

    // Optionally bounded (unbounded if capacity not specified)
    BlockingQueue<String> unbounded = new LinkedBlockingQueue<>();
    BlockingQueue<String> bounded = new LinkedBlockingQueue<>(100);

    // Separate locks for head and tail - better concurrency
    var producer = new Thread(() -> {
        try {
            for (int i = 0; i < 1000; i++) {
                bounded.put("Item-" + i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });

    var consumer = new Thread(() -> {
        try {
            for (int i = 0; i < 1000; i++) {
                bounded.take();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });

    long start = System.currentTimeMillis();
    producer.start();
    consumer.start();
    producer.join();
    consumer.join();
    long elapsed = System.currentTimeMillis() - start;

    println("Processed 1000 items in " + elapsed + "ms");
    println("LinkedBlockingQueue uses separate locks for put/take");
}
```

`LinkedBlockingQueue` uses separate locks for puts and takes, providing  
better throughput than `ArrayBlockingQueue` when producers and consumers  
operate at similar rates. However, it has higher memory overhead due to  
node allocation.  

### PriorityBlockingQueue  

`PriorityBlockingQueue` orders elements by priority rather than FIFO.  

```java
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.BlockingQueue;

record Task(String name, int priority) implements Comparable<Task> {
    @Override
    public int compareTo(Task other) {
        return Integer.compare(this.priority, other.priority);
    }
}

void main() throws InterruptedException {

    BlockingQueue<Task> queue = new PriorityBlockingQueue<>();

    // Add tasks with different priorities
    queue.put(new Task("Low priority task", 3));
    queue.put(new Task("High priority task", 1));
    queue.put(new Task("Medium priority task", 2));
    queue.put(new Task("Critical task", 0));

    // Tasks come out in priority order
    println("Processing tasks by priority:");
    while (!queue.isEmpty()) {
        Task task = queue.take();
        println("  " + task.priority() + ": " + task.name());
    }
}
```

Elements must implement `Comparable` or a `Comparator` must be provided.  
Note that `PriorityBlockingQueue` is unbounded—it never blocks on `put`.  
The priority ordering applies only during removal, not insertion.  

### DelayQueue  

`DelayQueue` holds elements that become available only after their delay  
expires.  

```java
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

record DelayedTask(String name, long triggerTime) implements Delayed {
    
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(triggerTime - System.currentTimeMillis(), 
            TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed other) {
        return Long.compare(getDelay(TimeUnit.MILLISECONDS), 
            other.getDelay(TimeUnit.MILLISECONDS));
    }
}

void main() throws InterruptedException {

    var queue = new DelayQueue<DelayedTask>();
    long now = System.currentTimeMillis();

    // Schedule tasks with different delays
    queue.put(new DelayedTask("Task A", now + 3000)); // 3 seconds
    queue.put(new DelayedTask("Task B", now + 1000)); // 1 second
    queue.put(new DelayedTask("Task C", now + 2000)); // 2 seconds

    println("Waiting for tasks...");

    for (int i = 0; i < 3; i++) {
        DelayedTask task = queue.take(); // Blocks until delay expires
        println("Executed: " + task.name() + " at " + 
            (System.currentTimeMillis() - now) + "ms");
    }
}
```

`DelayQueue` is useful for implementing scheduled tasks, cache expiration,  
retry logic with exponential backoff, or rate limiting. Elements are ordered  
by their remaining delay time.  

### SynchronousQueue  

`SynchronousQueue` has no internal capacity—each put must wait for a take.  

```java
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.BlockingQueue;

void main() throws InterruptedException {

    BlockingQueue<String> queue = new SynchronousQueue<>();

    var producer = new Thread(() -> {
        try {
            for (int i = 1; i <= 5; i++) {
                String item = "Item-" + i;
                println("Producer putting: " + item);
                queue.put(item); // Blocks until consumer takes
                println("Producer handed off: " + item);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });

    var consumer = new Thread(() -> {
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(500); // Slow consumer
                String item = queue.take(); // Blocks until producer puts
                println("Consumer received: " + item);
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

Each insertion blocks until another thread removes the element, and vice  
versa. This creates a direct handoff between producer and consumer, useful  
for implementing rendezvous points or when you want immediate processing  
without buffering.  

## Comparison of Concurrent Collections  

| Collection | Thread-Safety Mechanism | Read Performance | Write Performance | Best Use Case |
|------------|------------------------|------------------|-------------------|---------------|
| ConcurrentHashMap | CAS + bucket-level locks | Excellent | Very Good | General-purpose thread-safe map |
| CopyOnWriteArrayList | Copy-on-write | Excellent | Poor | Read-heavy lists, event listeners |
| ArrayBlockingQueue | Single lock | Good | Good | Bounded producer-consumer |
| LinkedBlockingQueue | Dual locks | Good | Good | High-throughput producer-consumer |
| PriorityBlockingQueue | Single lock + heap | Good | Good | Priority-based task scheduling |
| DelayQueue | Single lock | Good | Good | Scheduled tasks, timeouts |
| SynchronousQueue | Lock-free handoff | N/A | N/A | Direct thread-to-thread transfer |

## Best Practices  

### Choosing the right collection  

Select concurrent collections based on access patterns and requirements.  

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;

void main() {

    // 1. For key-value data with concurrent access
    //    Use ConcurrentHashMap
    var cache = new ConcurrentHashMap<String, Object>();
    cache.computeIfAbsent("config", k -> loadConfig());

    // 2. For lists rarely modified but frequently iterated
    //    Use CopyOnWriteArrayList
    var listeners = new CopyOnWriteArrayList<Runnable>();

    // 3. For producer-consumer patterns
    //    Use appropriate BlockingQueue implementation
    BlockingQueue<String> workQueue = new LinkedBlockingQueue<>(1000);

    println("Collections selected based on use case:");
    println("  ConcurrentHashMap for cache/config");
    println("  CopyOnWriteArrayList for listeners");
    println("  LinkedBlockingQueue for work distribution");
}

Object loadConfig() {
    return Map.of("setting", "value");
}
```

The key factors are: read/write ratio, whether you need ordering or  
priority, bounded vs unbounded capacity requirements, and whether blocking  
behavior is desired. Choosing incorrectly can severely impact performance.  

### Avoiding unnecessary synchronization  

Concurrent collections handle synchronization internally—don't add more.  

```java
import java.util.concurrent.ConcurrentHashMap;

void main() {

    var map = new ConcurrentHashMap<String, Integer>();

    // BAD: External synchronization is unnecessary and harmful
    // synchronized (map) {
    //     map.put("key", map.getOrDefault("key", 0) + 1);
    // }

    // GOOD: Use built-in atomic operations
    map.merge("key", 1, Integer::sum);

    // BAD: Check-then-act race condition
    // if (!map.containsKey("key")) {
    //     map.put("key", computeValue());
    // }

    // GOOD: Atomic compute
    map.computeIfAbsent("key", k -> computeValue());

    println("Value: " + map.get("key"));
}

Integer computeValue() {
    return 42;
}
```

Adding external synchronization defeats the purpose of concurrent  
collections, reducing performance without adding safety. Use the atomic  
methods provided by the collection instead of compound check-then-act  
operations.  

### Monitoring performance trade-offs  

Different workloads require different collections.  

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Collections;

void main() throws InterruptedException {

    int iterations = 10000;
    int threads = 4;

    // Test ConcurrentHashMap
    var concurrentMap = new ConcurrentHashMap<Integer, Integer>();
    long mapTime = measureConcurrentOps(concurrentMap, iterations, threads);

    // Test synchronized map for comparison
    var syncMap = Collections.synchronizedMap(new HashMap<Integer, Integer>());
    long syncTime = measureConcurrentOps(syncMap, iterations, threads);

    println("ConcurrentHashMap: " + mapTime + "ms");
    println("Synchronized Map: " + syncTime + "ms");
    println("ConcurrentHashMap is typically faster for concurrent access");
}

long measureConcurrentOps(Map<Integer, Integer> map, int iterations, 
        int threads) throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(threads);
    long start = System.currentTimeMillis();

    for (int t = 0; t < threads; t++) {
        executor.submit(() -> {
            for (int i = 0; i < iterations; i++) {
                map.put(i % 100, i);
                map.get(i % 100);
            }
        });
    }

    executor.shutdown();
    executor.awaitTermination(1, TimeUnit.MINUTES);
    return System.currentTimeMillis() - start;
}
```

Always benchmark with realistic workloads. The best collection depends on  
your specific read/write ratio, contention level, and data size. Profile  
in production-like conditions to make informed decisions.  

## Common Pitfalls  

### Misusing CopyOnWriteArrayList for writes  

`CopyOnWriteArrayList` performs poorly with frequent modifications.  

```java
import java.util.concurrent.CopyOnWriteArrayList;

void main() {

    // BAD: Using CopyOnWriteArrayList for write-heavy workload
    var cowList = new CopyOnWriteArrayList<Integer>();

    long cowStart = System.currentTimeMillis();
    for (int i = 0; i < 10000; i++) {
        cowList.add(i); // Each add copies the entire array!
    }
    long cowTime = System.currentTimeMillis() - cowStart;

    // GOOD: Use ConcurrentLinkedQueue or synchronized list for writes
    var arrayList = new ArrayList<Integer>();

    long alStart = System.currentTimeMillis();
    for (int i = 0; i < 10000; i++) {
        synchronized (arrayList) {
            arrayList.add(i);
        }
    }
    long alTime = System.currentTimeMillis() - alStart;

    println("CopyOnWriteArrayList: " + cowTime + "ms");
    println("Synchronized ArrayList: " + alTime + "ms");
    println("Use CopyOnWriteArrayList only for read-heavy workloads");
}
```

Each modification copies the entire array, making write performance O(n).  
For lists with frequent modifications, use `ConcurrentLinkedQueue` or  
a properly synchronized collection.  

### Assuming compound operations are atomic  

Individual operations are atomic, but sequences of operations are not.  

```java
import java.util.concurrent.ConcurrentHashMap;

void main() throws InterruptedException {

    var map = new ConcurrentHashMap<String, Integer>();
    map.put("balance", 1000);

    // BAD: Non-atomic compound operation
    // var balance = map.get("balance");
    // if (balance >= 100) {
    //     map.put("balance", balance - 100); // Race condition!
    // }

    // GOOD: Use atomic operations
    var threads = new ArrayList<Thread>();
    for (int i = 0; i < 20; i++) {
        var thread = new Thread(() -> {
            map.compute("balance", (key, balance) -> {
                if (balance != null && balance >= 100) {
                    return balance - 100;
                }
                return balance;
            });
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    println("Final balance: " + map.get("balance"));
    println("Use compute() for atomic read-modify-write operations");
}
```

The check-then-act pattern is not thread-safe even with concurrent  
collections. Another thread can modify the value between your check and  
your action. Always use atomic methods like `compute`, `merge`, or  
`computeIfAbsent` for compound operations.  

### Ignoring BlockingQueue capacity limits  

Unbounded queues can lead to memory exhaustion.  

```java
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

void main() throws InterruptedException {

    // RISKY: Unbounded queue can grow indefinitely
    BlockingQueue<String> unbounded = new LinkedBlockingQueue<>();
    // If producer is faster than consumer, memory exhaustion!

    // BETTER: Bounded queue with capacity limit
    BlockingQueue<String> bounded = new ArrayBlockingQueue<>(1000);

    // Producer that respects backpressure
    var producer = new Thread(() -> {
        try {
            for (int i = 0; i < 2000; i++) {
                // put() blocks when queue is full - natural backpressure
                bounded.put("Item-" + i);
                if (i % 500 == 0) {
                    println("Produced " + i + " items, queue size: " + 
                        bounded.size());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });

    var consumer = new Thread(() -> {
        try {
            for (int i = 0; i < 2000; i++) {
                bounded.take();
                Thread.sleep(1); // Slow consumer
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });

    producer.start();
    consumer.start();

    producer.join();
    consumer.join();

    println("Bounded queues prevent memory exhaustion through backpressure");
}
```

Always consider capacity limits when using blocking queues. Bounded queues  
provide natural backpressure—when the queue fills up, producers block  
until space is available, preventing memory exhaustion.  

## Advanced Topics  

### Integration with Executors  

Concurrent collections work seamlessly with thread pools.  

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    var results = new ConcurrentHashMap<Integer, String>();
    var workQueue = new LinkedBlockingQueue<Integer>(100);
    ExecutorService executor = Executors.newFixedThreadPool(4);

    // Submit work items
    for (int i = 0; i < 50; i++) {
        workQueue.put(i);
    }

    // Workers process items and store results
    for (int w = 0; w < 4; w++) {
        executor.submit(() -> {
            while (!workQueue.isEmpty()) {
                Integer item = workQueue.poll();
                if (item != null) {
                    results.put(item, "Result-" + item);
                }
            }
        });
    }

    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    println("Processed " + results.size() + " items");
    println("Sample: " + results.get(25));
}
```

Thread pools distribute work across threads, while concurrent collections  
provide safe shared state. This combination is the foundation of most  
concurrent Java applications.  

### Using with parallel streams  

Concurrent collections can safely collect parallel stream results.  

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

void main() {

    var frequencyMap = new ConcurrentHashMap<Integer, Long>();

    // Process in parallel, collect to concurrent map
    IntStream.range(0, 10000)
        .parallel()
        .forEach(i -> {
            int key = i % 10;
            frequencyMap.merge(key, 1L, Long::sum);
        });

    println("Frequency counts:");
    frequencyMap.forEach((key, count) -> 
        println("  " + key + ": " + count));

    long total = frequencyMap.values().stream().mapToLong(Long::longValue).sum();
    println("Total: " + total);
}
```

When collecting results from parallel streams into shared data structures,  
concurrent collections eliminate the need for external synchronization.  
The `merge` operation safely handles concurrent updates from multiple  
stream threads.  

### Combining with synchronization primitives  

For complex coordination, combine concurrent collections with other primitives.  

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {

    var sharedState = new ConcurrentHashMap<String, Integer>();
    var startLatch = new CountDownLatch(1);
    var completionLatch = new CountDownLatch(5);

    ExecutorService executor = Executors.newFixedThreadPool(5);

    for (int i = 0; i < 5; i++) {
        int workerId = i;
        executor.submit(() -> {
            try {
                startLatch.await(); // Wait for signal to start
                for (int j = 0; j < 1000; j++) {
                    sharedState.merge("counter", 1, Integer::sum);
                }
                println("Worker " + workerId + " completed");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                completionLatch.countDown();
            }
        });
    }

    println("All workers ready, starting...");
    startLatch.countDown(); // Signal all workers to start

    completionLatch.await(); // Wait for all to complete
    println("Final counter: " + sharedState.get("counter"));

    executor.shutdown();
}
```

`CountDownLatch` ensures all workers start simultaneously and the main  
thread waits for completion. The concurrent map safely aggregates results.  
This pattern is useful for benchmarking and coordinated parallel operations.  

## Conclusion  

Concurrent collections are essential building blocks for modern multithreaded  
Java applications. They provide thread-safe data structures with significantly  
better performance than synchronized wrappers, using sophisticated internal  
mechanisms like CAS operations, lock striping, and copy-on-write semantics.  

**ConcurrentHashMap** is the workhorse for shared key-value data, offering  
excellent concurrency through fine-grained locking and atomic compound  
operations. **CopyOnWriteArrayList** excels in read-heavy scenarios like  
event listener management, trading write performance for lock-free reads.  
**BlockingQueue** implementations simplify producer-consumer patterns with  
built-in blocking behavior that automatically coordinates threads.  

Key principles for effective use:  

1. **Choose based on access patterns**: Match the collection to your read/write  
   ratio and concurrency requirements  

2. **Use atomic operations**: Leverage built-in methods like `compute`,  
   `merge`, and `computeIfAbsent` for compound operations  

3. **Avoid external synchronization**: The collections handle thread safety  
   internally—adding more locks hurts performance  

4. **Respect capacity limits**: Bounded queues prevent memory exhaustion  
   through natural backpressure  

5. **Understand iteration semantics**: Weakly consistent iterators may not  
   reflect the absolute latest state but never throw exceptions  

By understanding the strengths and trade-offs of each concurrent collection,  
developers can build applications that are both correct and performant under  
concurrent access, without the complexity of manual synchronization.  
