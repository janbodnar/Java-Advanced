# Java Memory Model

The **Java Memory Model (JMM)** defines how threads interact through memory and  
specifies the rules that govern when changes made by one thread become visible  
to other threads. It is a fundamental part of the Java Language Specification  
that provides the formal semantics of concurrent programming in Java.  

Understanding the JMM is critical for writing correct multithreaded programs.  
Without proper understanding, developers may write code that appears correct  
but fails unpredictably due to subtle concurrency issues like race conditions,  
stale data reads, or instruction reordering. The JMM establishes a contract  
between the programmer and the JVM, defining what guarantees the JVM provides  
and what responsibilities the programmer must fulfill.  

The JMM addresses three fundamental challenges in concurrent programming:  

- **Visibility**: Ensuring that changes made by one thread are visible to  
  other threads when needed  
- **Atomicity**: Guaranteeing that certain operations complete as indivisible  
  units without interference from other threads  
- **Ordering**: Defining the order in which operations appear to execute from  
  the perspective of different threads  

The JMM is defined in terms of **happens-before** relationships, which provide  
a formal framework for reasoning about memory visibility and operation ordering.  
These rules allow developers to write portable concurrent code that behaves  
consistently across different hardware platforms and JVM implementations.  

## Core Concepts  

The Java Memory Model abstracts the complex realities of modern computer  
architectures into a simplified model that developers can reason about. Modern  
CPUs use multiple levels of caching, out-of-order execution, and various  
optimizations that can cause operations to appear in different orders than  
specified in the source code.  

### Main Memory and Working Memory  

In the JMM, there is a conceptual distinction between **main memory** and  
**working memory**:  

| Memory Type     | Description                                              |
|-----------------|----------------------------------------------------------|
| Main Memory     | Shared memory where all variables are stored             |
| Working Memory  | Per-thread cache of variables (CPU caches/registers)     |

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              Main Memory                                │
│   ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐   │
│   │  Variable A │  │  Variable B │  │  Variable C │  │  Variable D │   │
│   └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘   │
└─────────────────────────────────────────────────────────────────────────┘
         ▲                  ▲                  ▲                  ▲
         │ load/store       │ load/store       │ load/store       │
         ▼                  ▼                  ▼                  ▼
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────────────┐
│  Thread 1       │  │  Thread 2       │  │  Thread 3                   │
│  Working Memory │  │  Working Memory │  │  Working Memory             │
│  ┌───────────┐  │  │  ┌───────────┐  │  │  ┌───────────┐ ┌─────────┐  │
│  │ Copy of A │  │  │  │ Copy of B │  │  │  │ Copy of C │ │ Copy of │  │
│  │ Copy of B │  │  │  │ Copy of C │  │  │  │ Copy of D │ │    A    │  │
│  └───────────┘  │  │  └───────────┘  │  │  └───────────┘ └─────────┘  │
└─────────────────┘  └─────────────────┘  └─────────────────────────────┘
```

Each thread has its own working memory, which is an abstract concept  
representing CPU caches, registers, and compiler optimizations. When a thread  
reads a variable, it may read from its working memory rather than main memory.  
When a thread writes a variable, the change may remain in working memory  
temporarily before being flushed to main memory.  

### Visibility  

**Visibility** refers to when changes made by one thread become visible to  
other threads. Without proper synchronization, there is no guarantee that a  
thread will ever see changes made by another thread.  

```java
class VisibilityProblem {
    boolean running = true;
    int counter = 0;
}

void main() throws InterruptedException {

    var shared = new VisibilityProblem();

    var worker = new Thread(() -> {
        while (shared.running) {
            shared.counter++;
        }
        println("Worker stopped, counter = " + shared.counter);
    });

    worker.start();
    Thread.sleep(100);
    shared.running = false;
    println("Main thread set running to false");

    worker.join(2000);
    if (worker.isAlive()) {
        println("Worker thread did not see the change!");
        worker.interrupt();
    }
}
```

In this example, the worker thread may never see the change to `running`  
because it might cache the value in its working memory. The JMM does not  
require the thread to ever refresh its cached value without explicit  
synchronization.  

### Atomicity  

**Atomicity** ensures that an operation completes as an indivisible unit.  
Simple reads and writes of most primitive types are atomic, but compound  
operations like `counter++` are not.  

| Operation Type                | Atomic?                                     |
|-------------------------------|---------------------------------------------|
| Read/write of reference       | Yes                                         |
| Read/write of most primitives | Yes                                         |
| Read/write of long/double     | Not guaranteed (may require volatile)       |
| Increment (`i++`)             | No (read-modify-write)                      |
| Compare-and-set               | Yes (when using atomic classes)             |

```java
void main() throws InterruptedException {

    var counter = new int[]{0};
    var threads = new ArrayList<Thread>();

    for (int i = 0; i < 10; i++) {
        var thread = new Thread(() -> {
            for (int j = 0; j < 1000; j++) {
                counter[0]++;  // NOT atomic: read, increment, write
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

The increment operation `counter[0]++` consists of three separate steps:  
read the current value, add one, and write the new value. Multiple threads  
can interleave these steps, causing lost updates and an incorrect final count.  

### Ordering  

**Ordering** defines the sequence in which operations appear to execute.  
Without synchronization, the compiler, JVM, and CPU are free to reorder  
operations for performance, as long as the reordering is not observable by  
a single thread.  

```java
class ReorderingExample {
    int x = 0;
    int y = 0;
    int a = 0;
    int b = 0;
}

void main() throws InterruptedException {

    for (int iteration = 0; iteration < 100000; iteration++) {

        var shared = new ReorderingExample();

        var thread1 = new Thread(() -> {
            shared.x = 1;
            shared.a = shared.y;
        });

        var thread2 = new Thread(() -> {
            shared.y = 1;
            shared.b = shared.x;
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        if (shared.a == 0 && shared.b == 0) {
            println("Reordering detected at iteration " + iteration);
            println("a = " + shared.a + ", b = " + shared.b);
            break;
        }
    }
}
```

This example may detect reordering where both `a` and `b` are zero, which  
would be impossible under sequential consistency. This happens because the  
CPU or compiler may reorder the write and read operations within each thread.  

### Sequential Consistency vs Real JVM Execution  

**Sequential consistency** is a theoretical model where all operations appear  
to execute in some total order that respects the program order of each thread.  
The JMM does NOT guarantee sequential consistency for unsynchronized programs.  

| Model                  | Description                                      |
|------------------------|--------------------------------------------------|
| Sequential Consistency | All operations appear in a single total order    |
| JMM (unsynchronized)   | Allows reorderings and visibility delays         |
| JMM (synchronized)     | Provides happens-before guarantees               |

The JMM provides sequential consistency only when programs are properly  
synchronized using mechanisms like `synchronized`, `volatile`, or atomic  
operations. This relaxed model allows JVMs and CPUs to apply optimizations  
that would be impossible under strict sequential consistency.  

## Happens-Before Relationship  

The **happens-before** relationship is the cornerstone of the Java Memory  
Model. It defines when the results of one operation are guaranteed to be  
visible to another operation. If operation A happens-before operation B,  
then the memory effects of A are visible to B.  

### Happens-Before Rules  

The JMM defines several rules that establish happens-before relationships:  

| Rule                    | Description                                        |
|-------------------------|----------------------------------------------------|
| Program Order           | Each action in a thread happens-before every       |
|                         | subsequent action in that same thread              |
| Monitor Lock            | An unlock on a monitor happens-before every        |
|                         | subsequent lock on that same monitor               |
| Volatile Variable       | A write to a volatile field happens-before every   |
|                         | subsequent read of that same volatile field        |
| Thread Start            | A call to `start` on a thread happens-before any   |
|                         | action in the started thread                       |
| Thread Join             | All actions in a thread happen-before another      |
|                         | thread successfully returns from `join`            |
| Thread Interruption     | A call to `interrupt` happens-before the           |
|                         | interrupted thread detects the interruption        |
| Object Finalization     | The end of a constructor happens-before the        |
|                         | start of the finalizer for that object             |
| Transitivity            | If A happens-before B and B happens-before C,      |
|                         | then A happens-before C                            |

### Program Order Rule  

Within a single thread, all operations happen-before subsequent operations  
according to the program text. This is the most basic happens-before rule.  

```java
void main() {

    int x = 0;      // (1)
    x = 5;          // (2) - happens-before (3)
    int y = x + 1;  // (3) - sees x = 5

    println("x = " + x + ", y = " + y);
}
```

Operation (2) happens-before operation (3) due to program order, so (3)  
is guaranteed to see the value written by (2). This is intuitive within  
a single thread but becomes crucial when understanding cross-thread visibility.  

### Monitor Lock Rule  

The release of a lock (unlock) happens-before every subsequent acquisition  
of that same lock. This ensures visibility of changes made while holding  
the lock.  

```java
class MonitorExample {
    int sharedData = 0;
}

void main() throws InterruptedException {

    var shared = new MonitorExample();
    var lock = new Object();

    var writer = new Thread(() -> {
        synchronized (lock) {
            shared.sharedData = 42;  // (1)
        }                            // (2) unlock
    });

    var reader = new Thread(() -> {
        try { Thread.sleep(50); } catch (InterruptedException e) {}
        synchronized (lock) {        // (3) lock - happens-after (2)
            println("Read: " + shared.sharedData);  // sees 42
        }
    });

    writer.start();
    reader.start();
    writer.join();
    reader.join();
}
```

The unlock at (2) happens-before the lock at (3). By transitivity, write  
(1) happens-before the read in the reader thread. This guarantees that the  
reader sees the value 42 written by the writer.  

### Volatile Variable Rule  

A write to a volatile variable happens-before every subsequent read of that  
variable. This provides visibility guarantees without mutual exclusion.  

```java
class VolatileExample {
    volatile boolean ready = false;
    int data = 0;
}

void main() throws InterruptedException {

    var shared = new VolatileExample();

    var writer = new Thread(() -> {
        shared.data = 42;         // (1)
        shared.ready = true;      // (2) volatile write
    });

    var reader = new Thread(() -> {
        while (!shared.ready) {}  // (3) volatile read - happens-after (2)
        println("Data: " + shared.data);  // (4) sees 42
    });

    writer.start();
    reader.start();
    writer.join();
    reader.join();
}
```

The volatile write at (2) happens-before the volatile read at (3). Due to  
program order, (1) happens-before (2). By transitivity, (1) happens-before  
(4), ensuring the reader sees `data = 42`.  

### Thread Start Rule  

A call to `Thread.start` happens-before any action in the started thread.  
This ensures that the new thread sees all changes made before the start call.  

```java
void main() throws InterruptedException {

    var data = new int[]{0};

    data[0] = 100;  // (1)

    var thread = new Thread(() -> {
        // (3) sees data[0] = 100
        println("Thread sees: " + data[0]);
    });

    thread.start();  // (2) - all changes before this are visible to thread
    thread.join();
}
```

The assignment at (1) happens-before the start call at (2), which  
happens-before all actions in the new thread. The new thread is guaranteed  
to see `data[0] = 100`.  

### Thread Join Rule  

All actions in a thread happen-before the successful return from `join`  
on that thread. This ensures that results computed by a thread are visible  
after joining.  

```java
void main() throws InterruptedException {

    var result = new int[]{0};

    var worker = new Thread(() -> {
        result[0] = computeValue();  // (1)
    });                              // (2) thread terminates

    worker.start();
    worker.join();  // (3) - happens-after (2)

    // (4) sees result[0] from worker
    println("Result: " + result[0]);
}

int computeValue() {
    int sum = 0;
    for (int i = 1; i <= 100; i++) {
        sum += i;
    }
    return sum;
}
```

All actions in the worker thread, including (1), happen-before the join  
returns at (3). This guarantees that the main thread sees the result  
computed by the worker.  

### Happens-Before Diagram  

```
Thread 1                          Thread 2
─────────                         ─────────
   │                                  │
   ▼                                  │
[x = 1]                               │
   │                                  │
   │ program order                    │
   ▼                                  │
[volatile write: ready = true] ─────────────────► [volatile read: ready]
                               happens-before      │
                                                   │ program order
                                                   ▼
                                                [read x]
                                                (sees x = 1)
```

This diagram illustrates how happens-before relationships chain together  
across threads through a volatile variable, ensuring that Thread 2 sees  
the write to `x` performed by Thread 1.  

## Volatile Variables  

The `volatile` keyword in Java provides two key guarantees:  

1. **Visibility**: Changes to a volatile variable are immediately visible  
   to all threads  
2. **Ordering**: Volatile reads and writes cannot be reordered with other  
   memory operations  

### How Volatile Works  

When a thread writes to a volatile variable, the JMM requires that all  
variables visible to that thread are flushed from working memory to main  
memory. When a thread reads a volatile variable, it refreshes all cached  
variables from main memory.  

```
Before volatile write:           After volatile write:
┌──────────────────────┐         ┌──────────────────────┐
│   Working Memory     │         │   Main Memory        │
│   ┌────────────────┐ │         │   ┌────────────────┐ │
│   │ x = 5          │─┼────────►│   │ x = 5          │ │
│   │ y = 10         │─┼────────►│   │ y = 10         │ │
│   │ ready = true   │─┼────────►│   │ ready = true   │ │
│   └────────────────┘ │ flush   │   └────────────────┘ │
└──────────────────────┘         └──────────────────────┘
```

### Correct Usage of Volatile  

Volatile is suitable for flags and status indicators where only one thread  
writes and multiple threads read, or when the written value does not depend  
on the current value.  

```java
class GracefulShutdown {
    volatile boolean shutdownRequested = false;
}

void main() throws InterruptedException {

    var controller = new GracefulShutdown();

    var workers = new ArrayList<Thread>();
    for (int i = 0; i < 3; i++) {
        int workerId = i;
        var worker = new Thread(() -> {
            int count = 0;
            while (!controller.shutdownRequested) {
                count++;
                // Simulate work
                try { Thread.sleep(10); } catch (InterruptedException e) {
                    break;
                }
            }
            println("Worker " + workerId + " processed " + count + " items");
        });
        workers.add(worker);
        worker.start();
    }

    Thread.sleep(500);
    controller.shutdownRequested = true;  // All workers will see this
    println("Shutdown requested");

    for (var worker : workers) {
        worker.join();
    }
    println("All workers stopped");
}
```

The volatile `shutdownRequested` flag ensures that all worker threads see  
the shutdown signal promptly. Without volatile, workers might cache the  
value and never observe the change.  

### Volatile for Publication  

Volatile can be used to safely publish immutable objects to other threads.  

```java
record Configuration(String host, int port, boolean secure) {}

class ConfigHolder {
    volatile Configuration config;
}

void main() throws InterruptedException {

    var holder = new ConfigHolder();

    var publisher = new Thread(() -> {
        // Create and publish configuration
        holder.config = new Configuration("localhost", 8080, true);
        println("Configuration published");
    });

    var consumer = new Thread(() -> {
        Configuration cfg = null;
        while ((cfg = holder.config) == null) {
            Thread.onSpinWait();
        }
        println("Received: " + cfg.host() + ":" + cfg.port());
    });

    consumer.start();
    Thread.sleep(100);
    publisher.start();

    publisher.join();
    consumer.join();
}
```

The volatile reference ensures that when the consumer reads a non-null  
config, it sees the fully constructed Configuration object with all  
fields properly initialized.  

### Common Pitfalls with Volatile  

Volatile does NOT provide atomicity for compound operations like increment.  

```java
class VolatileCounter {
    volatile int count = 0;
}

void main() throws InterruptedException {

    var counter = new VolatileCounter();
    var threads = new ArrayList<Thread>();

    for (int i = 0; i < 10; i++) {
        var thread = new Thread(() -> {
            for (int j = 0; j < 1000; j++) {
                counter.count++;  // NOT atomic even with volatile!
            }
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    println("Expected: 10000, Actual: " + counter.count);
    println("Volatile does not make ++ atomic!");
}
```

Even though `count` is volatile, the increment operation is still a  
read-modify-write sequence that can be interleaved by other threads.  
Use `AtomicInteger` or `synchronized` for atomic compound operations.  

### When to Use Volatile  

| Use Case                          | Suitable?                           |
|-----------------------------------|-------------------------------------|
| Single writer, multiple readers   | Yes                                 |
| Status flags (running, shutdown)  | Yes                                 |
| Publishing immutable objects      | Yes                                 |
| Counters with increment           | No, use AtomicInteger               |
| Check-then-act patterns           | No, use synchronized                |
| Multiple writers                  | Usually no, use synchronized        |

## Synchronization  

The `synchronized` keyword provides both mutual exclusion and memory  
visibility guarantees. It is the most fundamental synchronization primitive  
in Java.  

### Synchronized Methods  

When a method is declared `synchronized`, only one thread can execute that  
method on a particular object instance at a time.  

```java
class BankAccount {
    private int balance;

    BankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    synchronized void deposit(int amount) {
        balance += amount;
    }

    synchronized void withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
        }
    }

    synchronized int getBalance() {
        return balance;
    }
}

void main() throws InterruptedException {

    var account = new BankAccount(1000);
    var threads = new ArrayList<Thread>();

    // Multiple depositors
    for (int i = 0; i < 5; i++) {
        var thread = new Thread(() -> {
            for (int j = 0; j < 100; j++) {
                account.deposit(10);
            }
        });
        threads.add(thread);
        thread.start();
    }

    // Multiple withdrawers
    for (int i = 0; i < 5; i++) {
        var thread = new Thread(() -> {
            for (int j = 0; j < 100; j++) {
                account.withdraw(5);
            }
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    println("Final balance: " + account.getBalance());
    println("Expected: 1000 + (5*100*10) - (5*100*5) = 3500");
}
```

The `synchronized` keyword ensures that only one thread can execute any  
synchronized method on the same BankAccount instance at a time. This  
prevents race conditions where simultaneous deposits and withdrawals  
could corrupt the balance.  

### Synchronized Blocks  

Synchronized blocks provide more fine-grained control over locking by  
allowing you to specify the lock object and limit the scope of locking.  

```java
class TransferService {
    void transfer(BankAccount from, BankAccount to, int amount) {
        // Lock ordering to prevent deadlock
        Object firstLock = System.identityHashCode(from) < 
                           System.identityHashCode(to) ? from : to;
        Object secondLock = firstLock == from ? to : from;

        synchronized (firstLock) {
            synchronized (secondLock) {
                if (from.getBalance() >= amount) {
                    from.withdraw(amount);
                    to.deposit(amount);
                }
            }
        }
    }
}

class BankAccount {
    private int balance;

    BankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    synchronized void deposit(int amount) {
        balance += amount;
    }

    synchronized void withdraw(int amount) {
        balance -= amount;
    }

    synchronized int getBalance() {
        return balance;
    }
}

void main() throws InterruptedException {

    var account1 = new BankAccount(1000);
    var account2 = new BankAccount(1000);
    var service = new TransferService();

    var threads = new ArrayList<Thread>();

    for (int i = 0; i < 100; i++) {
        threads.add(new Thread(() -> service.transfer(account1, account2, 10)));
        threads.add(new Thread(() -> service.transfer(account2, account1, 10)));
    }

    for (var thread : threads) {
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    println("Account 1: " + account1.getBalance());
    println("Account 2: " + account2.getBalance());
    println("Total: " + (account1.getBalance() + account2.getBalance()));
}
```

The transfer method uses a consistent lock ordering based on object  
identity hash codes to prevent deadlock when multiple threads try to  
transfer between the same accounts in different directions.  

### ReentrantLock  

`ReentrantLock` provides the same mutual exclusion as `synchronized` but  
with additional features like tryLock, timed lock, and interruptible locking.  

```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

class ResourcePool {
    private final ReentrantLock lock = new ReentrantLock();
    private int available = 5;

    boolean tryAcquire(long timeout) throws InterruptedException {
        if (lock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
            try {
                if (available > 0) {
                    available--;
                    return true;
                }
                return false;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    void release() {
        lock.lock();
        try {
            available++;
        } finally {
            lock.unlock();
        }
    }

    int getAvailable() {
        lock.lock();
        try {
            return available;
        } finally {
            lock.unlock();
        }
    }
}

void main() throws InterruptedException {

    var pool = new ResourcePool();
    var threads = new ArrayList<Thread>();

    for (int i = 0; i < 10; i++) {
        int id = i;
        var thread = new Thread(() -> {
            try {
                if (pool.tryAcquire(100)) {
                    println("Thread " + id + " acquired resource");
                    Thread.sleep(200);
                    pool.release();
                    println("Thread " + id + " released resource");
                } else {
                    println("Thread " + id + " timed out");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    println("Final available: " + pool.getAvailable());
}
```

ReentrantLock's tryLock with timeout prevents threads from waiting  
indefinitely, which is useful for avoiding deadlocks and implementing  
responsive applications that can give up on acquiring a resource.  

### Comparison: synchronized vs ReentrantLock  

| Feature                   | synchronized        | ReentrantLock             |
|---------------------------|---------------------|---------------------------|
| Lock acquisition          | Implicit            | Explicit lock/unlock      |
| Interruptible locking     | No                  | Yes (lockInterruptibly)   |
| Timed locking             | No                  | Yes (tryLock with timeout)|
| Non-blocking tryLock      | No                  | Yes                       |
| Multiple conditions       | No                  | Yes (newCondition)        |
| Fairness policy           | No                  | Yes (optional)            |
| Must release in finally   | Automatic           | Yes, manual               |
| Performance               | Slightly faster     | Slightly slower           |

```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

class BoundedBuffer {
    private final String[] buffer;
    private int count, putIndex, takeIndex;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    BoundedBuffer(int capacity) {
        buffer = new String[capacity];
    }

    void put(String item) throws InterruptedException {
        lock.lock();
        try {
            while (count == buffer.length) {
                notFull.await();
            }
            buffer[putIndex] = item;
            putIndex = (putIndex + 1) % buffer.length;
            count++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    String take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            String item = buffer[takeIndex];
            takeIndex = (takeIndex + 1) % buffer.length;
            count--;
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }
}

void main() throws InterruptedException {

    var buffer = new BoundedBuffer(5);

    var producer = new Thread(() -> {
        try {
            for (int i = 0; i < 10; i++) {
                buffer.put("Item-" + i);
                println("Produced: Item-" + i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });

    var consumer = new Thread(() -> {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(200);
                String item = buffer.take();
                println("Consumed: " + item);
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

This bounded buffer example demonstrates using multiple Condition objects  
with ReentrantLock. Separate conditions for "not full" and "not empty"  
allow precise signaling, waking only the appropriate waiting threads.  

## Atomic Operations  

The `java.util.concurrent.atomic` package provides classes that support  
lock-free, thread-safe operations on single variables. These classes use  
Compare-And-Swap (CAS) instructions provided by modern CPUs.  

### Compare-And-Swap (CAS)  

CAS is an atomic instruction that compares the current value of a memory  
location with an expected value and, only if they match, updates the  
location to a new value.  

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    Compare-And-Swap (CAS) Operation                     │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│   CAS(memory_location, expected_value, new_value)                       │
│                                                                         │
│   1. Read current value at memory_location                              │
│   2. Compare with expected_value                                        │
│   3. If equal: write new_value (success)                                │
│      If not equal: do nothing (failure)                                 │
│   4. Return whether update was successful                               │
│                                                                         │
│   All steps happen atomically!                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

### AtomicInteger  

`AtomicInteger` provides atomic operations on int values without locking.  

```java
import java.util.concurrent.atomic.AtomicInteger;

void main() throws InterruptedException {

    var counter = new AtomicInteger(0);
    var threads = new ArrayList<Thread>();

    for (int i = 0; i < 10; i++) {
        var thread = new Thread(() -> {
            for (int j = 0; j < 1000; j++) {
                counter.incrementAndGet();  // Atomic increment
            }
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    println("Final count: " + counter.get());
    println("Expected: 10000");
}
```

The `incrementAndGet` method uses CAS internally. If the CAS fails because  
another thread modified the value, it retries with the new current value  
until success. This provides atomicity without blocking.  

### Atomic Operations Methods  

| Method                    | Description                                  |
|---------------------------|----------------------------------------------|
| `get()`                   | Returns the current value                    |
| `set(value)`              | Sets to the given value                      |
| `getAndSet(value)`        | Sets and returns the old value               |
| `compareAndSet(exp, upd)` | Sets if current equals expected              |
| `incrementAndGet()`       | Increments and returns new value             |
| `getAndIncrement()`       | Returns current value, then increments       |
| `decrementAndGet()`       | Decrements and returns new value             |
| `addAndGet(delta)`        | Adds delta and returns new value             |
| `getAndAdd(delta)`        | Returns current value, then adds delta       |
| `updateAndGet(function)`  | Updates using function, returns new value    |
| `accumulateAndGet(x, op)` | Applies operator with x, returns new value   |

```java
import java.util.concurrent.atomic.AtomicInteger;

void main() {

    var value = new AtomicInteger(10);

    println("Initial: " + value.get());
    println("getAndIncrement: " + value.getAndIncrement() + 
            " -> now: " + value.get());
    println("incrementAndGet: " + value.incrementAndGet());
    println("addAndGet(5): " + value.addAndGet(5));
    println("compareAndSet(17, 20): " + value.compareAndSet(17, 20) + 
            " -> value: " + value.get());
    println("updateAndGet(x -> x * 2): " + value.updateAndGet(x -> x * 2));
    println("accumulateAndGet(3, (x,y) -> x+y): " + 
            value.accumulateAndGet(3, (x, y) -> x + y));
}
```

The `updateAndGet` and `accumulateAndGet` methods accept functions,  
enabling complex atomic updates. The function may be called multiple  
times if CAS fails, so it should be side-effect free.  

### AtomicReference  

`AtomicReference` provides atomic operations on object references.  

```java
import java.util.concurrent.atomic.AtomicReference;

record ImmutablePoint(int x, int y) {}

void main() throws InterruptedException {

    var pointRef = new AtomicReference<>(new ImmutablePoint(0, 0));

    var threads = new ArrayList<Thread>();
    for (int i = 0; i < 10; i++) {
        int delta = i;
        var thread = new Thread(() -> {
            for (int j = 0; j < 100; j++) {
                pointRef.updateAndGet(p -> 
                    new ImmutablePoint(p.x() + 1, p.y() + delta));
            }
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    var finalPoint = pointRef.get();
    println("Final point: (" + finalPoint.x() + ", " + finalPoint.y() + ")");
    println("Expected x: 1000");
}
```

AtomicReference is useful for atomically updating immutable objects or  
implementing lock-free data structures. The `updateAndGet` method  
atomically replaces the reference with a new value computed from the  
current value.  

### AtomicStampedReference  

`AtomicStampedReference` solves the ABA problem by associating an integer  
stamp with the reference.  

```java
import java.util.concurrent.atomic.AtomicStampedReference;

void main() {

    var ref = new AtomicStampedReference<>("A", 0);

    println("Initial: " + ref.getReference() + ", stamp: " + ref.getStamp());

    // Thread 1 reads value and stamp
    String expected = ref.getReference();
    int expectedStamp = ref.getStamp();

    // Simulate ABA: another thread changes A -> B -> A
    ref.compareAndSet("A", "B", 0, 1);
    println("After A->B: " + ref.getReference() + ", stamp: " + ref.getStamp());

    ref.compareAndSet("B", "A", 1, 2);
    println("After B->A: " + ref.getReference() + ", stamp: " + ref.getStamp());

    // Thread 1 tries to update - fails because stamp changed
    boolean success = ref.compareAndSet(expected, "C", expectedStamp, 3);
    println("Update with old stamp: " + success);

    // Update with correct stamp succeeds
    success = ref.compareAndSet("A", "C", 2, 3);
    println("Update with correct stamp: " + success);
    println("Final: " + ref.getReference() + ", stamp: " + ref.getStamp());
}
```

The ABA problem occurs when a value changes from A to B and back to A,  
making a simple CAS appear to succeed even though the value was modified.  
The stamp tracks modifications, preventing this issue.  

## Memory Reordering  

Modern CPUs and compilers reorder instructions for performance. These  
reorderings are invisible to single-threaded programs but can cause issues  
in multithreaded code without proper synchronization.  

### Types of Reordering  

| Type               | Description                                        |
|--------------------|----------------------------------------------------|
| Compiler Reorder   | Compiler rearranges instructions for optimization  |
| CPU Reorder        | CPU executes instructions out of program order     |
| Store Buffer       | Writes are buffered before reaching main memory    |
| Cache Coherence    | Different CPUs may see writes in different orders  |

### Reordering Example  

```java
class ReorderDemo {
    int a = 0;
    int b = 0;
    int x = 0;
    int y = 0;
}

void main() throws InterruptedException {

    int bothZero = 0;
    int iterations = 1000000;

    for (int i = 0; i < iterations; i++) {
        var demo = new ReorderDemo();

        var t1 = new Thread(() -> {
            demo.a = 1;
            demo.x = demo.b;
        });

        var t2 = new Thread(() -> {
            demo.b = 1;
            demo.y = demo.a;
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        if (demo.x == 0 && demo.y == 0) {
            bothZero++;
        }
    }

    println("Both zero occurred: " + bothZero + " times out of " + iterations);
    if (bothZero > 0) {
        println("Reordering detected! This proves memory reordering occurs.");
    }
}
```

Under sequential consistency, x=0 and y=0 simultaneously would be  
impossible. If this result occurs, it proves that either the compiler  
or CPU reordered the writes and reads within each thread.  

### How JMM Prevents Unsafe Reorderings  

The JMM uses **memory barriers** (also called fences) to prevent certain  
reorderings. Synchronization operations insert these barriers:  

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        Memory Barrier Types                             │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  LoadLoad Barrier:   Load1; LoadLoad; Load2                             │
│                      Ensures Load1 completes before Load2               │
│                                                                         │
│  StoreStore Barrier: Store1; StoreStore; Store2                         │
│                      Ensures Store1 is visible before Store2            │
│                                                                         │
│  LoadStore Barrier:  Load1; LoadStore; Store2                           │
│                      Ensures Load1 completes before Store2              │
│                                                                         │
│  StoreLoad Barrier:  Store1; StoreLoad; Load2                           │
│                      Ensures Store1 is visible before Load2             │
│                      (Most expensive, provides full fence)              │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

### Volatile and Memory Barriers  

Volatile accesses insert memory barriers that prevent reordering:  

| Operation          | Barriers                                          |
|--------------------|---------------------------------------------------|
| Volatile Read      | LoadLoad + LoadStore after the read              |
| Volatile Write     | StoreStore before + StoreLoad after the write    |

```java
class VolatileBarrierDemo {
    int a = 0;
    int b = 0;
    volatile boolean flag = false;
}

void main() throws InterruptedException {

    var demo = new VolatileBarrierDemo();

    var writer = new Thread(() -> {
        demo.a = 1;                    // (1)
        demo.b = 2;                    // (2)
        // StoreStore barrier here
        demo.flag = true;              // (3) volatile write
        // StoreLoad barrier here
    });

    var reader = new Thread(() -> {
        while (!demo.flag) {}          // (4) volatile read
        // LoadLoad + LoadStore barrier here
        println("a = " + demo.a);      // (5) guaranteed to see 1
        println("b = " + demo.b);      // (6) guaranteed to see 2
    });

    writer.start();
    reader.start();
    writer.join();
    reader.join();
}
```

The StoreStore barrier before the volatile write ensures (1) and (2)  
are visible before (3). The LoadLoad barrier after the volatile read  
ensures (5) and (6) see the values written by the writer thread.  

## Practical Examples  

This section demonstrates common concurrency issues and how to solve them  
using JMM principles.  

### Race Condition  

A race condition occurs when the correctness of a program depends on the  
relative timing of threads.  

```java
class RaceConditionDemo {
    int counter = 0;
}

void main() throws InterruptedException {

    var demo = new RaceConditionDemo();

    println("=== Without Synchronization (Race Condition) ===");

    var threads1 = new ArrayList<Thread>();
    for (int i = 0; i < 10; i++) {
        var thread = new Thread(() -> {
            for (int j = 0; j < 10000; j++) {
                demo.counter++;
            }
        });
        threads1.add(thread);
        thread.start();
    }

    for (var thread : threads1) {
        thread.join();
    }

    println("Expected: 100000, Actual: " + demo.counter);
    println("Lost updates: " + (100000 - demo.counter));
}
```

The increment operation reads, modifies, and writes the counter in three  
steps. Multiple threads can interleave these steps, causing some increments  
to be lost.  

### Fixing Race Condition with Synchronization  

```java
class SynchronizedCounter {
    private int counter = 0;

    synchronized void increment() {
        counter++;
    }

    synchronized int get() {
        return counter;
    }
}

void main() throws InterruptedException {

    var counter = new SynchronizedCounter();

    println("=== With Synchronization (No Race Condition) ===");

    var threads = new ArrayList<Thread>();
    for (int i = 0; i < 10; i++) {
        var thread = new Thread(() -> {
            for (int j = 0; j < 10000; j++) {
                counter.increment();
            }
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    println("Expected: 100000, Actual: " + counter.get());
}
```

The `synchronized` keyword ensures that only one thread can execute  
the increment method at a time, eliminating the race condition.  

### Stale Data  

Stale data occurs when a thread reads a cached value instead of the  
latest value in main memory.  

```java
class StaleDataDemo {
    boolean ready = false;
    int value = 0;
}

void main() throws InterruptedException {

    println("=== Stale Data Demo ===");

    var demo = new StaleDataDemo();

    var reader = new Thread(() -> {
        int spins = 0;
        while (!demo.ready) {
            spins++;
            if (spins > 1000000000) {
                println("Reader gave up after " + spins + " spins");
                return;
            }
        }
        println("Reader saw ready=true after " + spins + " spins");
        println("Value: " + demo.value);
    });

    var writer = new Thread(() -> {
        demo.value = 42;
        demo.ready = true;
        println("Writer set ready=true and value=42");
    });

    reader.start();
    Thread.sleep(100);
    writer.start();

    writer.join();
    reader.join(2000);

    if (reader.isAlive()) {
        println("Reader thread stuck - never saw update!");
        reader.interrupt();
    }
}
```

Without volatile, the reader thread may cache the value of `ready` and  
never see the update. This is a visibility problem that can cause the  
program to hang indefinitely.  

### Fixing Stale Data with Volatile  

```java
class VolatileFixDemo {
    volatile boolean ready = false;
    int value = 0;  // piggybacks on volatile synchronization
}

void main() throws InterruptedException {

    println("=== Volatile Fix Demo ===");

    var demo = new VolatileFixDemo();

    var reader = new Thread(() -> {
        int spins = 0;
        while (!demo.ready) {
            spins++;
        }
        println("Reader saw ready=true after " + spins + " spins");
        println("Value: " + demo.value);  // guaranteed to see 42
    });

    var writer = new Thread(() -> {
        demo.value = 42;
        demo.ready = true;
        println("Writer set ready=true and value=42");
    });

    reader.start();
    Thread.sleep(100);
    writer.start();

    writer.join();
    reader.join();
}
```

Making `ready` volatile ensures the reader sees the update. Additionally,  
due to happens-before rules, when the reader sees `ready=true`, it is  
guaranteed to see `value=42` as well.  

### Double-Checked Locking  

Double-checked locking is a pattern for lazy initialization that requires  
careful use of volatile to work correctly.  

```java
class Singleton {
    private static volatile Singleton instance;

    private Singleton() {
        // Expensive initialization
        println("Singleton instance created");
    }

    static Singleton getInstance() {
        if (instance == null) {                   // First check (no locking)
            synchronized (Singleton.class) {
                if (instance == null) {           // Second check (with lock)
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

void main() throws InterruptedException {

    println("=== Double-Checked Locking ===");

    var threads = new ArrayList<Thread>();
    for (int i = 0; i < 10; i++) {
        var thread = new Thread(() -> {
            Singleton s = Singleton.getInstance();
            println(Thread.currentThread().getName() + " got: " + s);
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }
}
```

The volatile keyword is essential here. Without it, threads might see  
a partially constructed object reference due to instruction reordering  
during object creation.  

### Initialization Safety  

```java
class ImmutableValue {
    private final int value;

    ImmutableValue(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }
}

class UnsafePublication {
    ImmutableValue holder;  // NOT safe without volatile or synchronization
}

class SafePublication {
    volatile ImmutableValue holder;  // Safe due to volatile
}

void main() throws InterruptedException {

    println("=== Safe Publication Demo ===");

    var safe = new SafePublication();

    var publisher = new Thread(() -> {
        safe.holder = new ImmutableValue(42);
        println("Published value");
    });

    var reader = new Thread(() -> {
        ImmutableValue v = null;
        while ((v = safe.holder) == null) {
            Thread.onSpinWait();
        }
        println("Read value: " + v.getValue());
    });

    reader.start();
    Thread.sleep(100);
    publisher.start();

    publisher.join();
    reader.join();
}
```

Even though ImmutableValue has a final field, unsafe publication (without  
volatile) could allow a reader to see a partially constructed object.  
Volatile publication ensures the reader sees the fully constructed object.  

## Best Practices  

Following these guidelines helps write correct and maintainable concurrent  
code.  

### Prefer Higher-Level Concurrency Utilities  

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

void main() throws InterruptedException {

    println("=== Using Higher-Level Utilities ===");

    // Use ConcurrentHashMap instead of synchronized HashMap
    var cache = new ConcurrentHashMap<String, String>();

    // Use AtomicLong instead of synchronized counter
    var requestCount = new AtomicLong(0);

    // Use ExecutorService instead of raw threads
    ExecutorService executor = Executors.newFixedThreadPool(4);

    for (int i = 0; i < 100; i++) {
        int id = i;
        executor.submit(() -> {
            cache.put("key" + id, "value" + id);
            requestCount.incrementAndGet();
        });
    }

    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    println("Cache size: " + cache.size());
    println("Request count: " + requestCount.get());
}
```

Higher-level utilities like ConcurrentHashMap, AtomicLong, and  
ExecutorService are thoroughly tested, well-optimized, and less  
error-prone than hand-rolled synchronization.  

### Minimize Scope of Synchronization  

```java
class DataProcessor {
    private final Object lock = new Object();
    private int processedCount = 0;
    private List<String> results = new ArrayList<>();

    void process(String data) {
        // Do expensive computation outside synchronized block
        String processed = expensiveComputation(data);

        // Only synchronize the critical section
        synchronized (lock) {
            results.add(processed);
            processedCount++;
        }
    }

    private String expensiveComputation(String data) {
        // Simulate expensive work
        try { Thread.sleep(10); } catch (InterruptedException e) {}
        return data.toUpperCase();
    }

    synchronized int getProcessedCount() {
        return processedCount;
    }
}

void main() throws InterruptedException {

    println("=== Minimizing Synchronization Scope ===");

    var processor = new DataProcessor();
    var threads = new ArrayList<Thread>();

    long start = System.currentTimeMillis();

    for (int i = 0; i < 20; i++) {
        int id = i;
        var thread = new Thread(() -> {
            processor.process("item-" + id);
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    long duration = System.currentTimeMillis() - start;
    println("Processed: " + processor.getProcessedCount());
    println("Time: " + duration + "ms");
}
```

By keeping the synchronized block small, threads can perform expensive  
computations in parallel. Only the shared state modification needs  
synchronization.  

### Use Immutable Objects  

```java
record ImmutableRequest(String id, long timestamp, String payload) {}

class RequestHandler {
    private volatile ImmutableRequest lastRequest;

    void handle(ImmutableRequest request) {
        // Safe to share immutable object without synchronization
        lastRequest = request;
        process(request);
    }

    private void process(ImmutableRequest request) {
        println("Processing: " + request.id() + " at " + request.timestamp());
    }

    ImmutableRequest getLastRequest() {
        return lastRequest;  // Safe to return immutable object
    }
}

void main() throws InterruptedException {

    println("=== Using Immutable Objects ===");

    var handler = new RequestHandler();
    var threads = new ArrayList<Thread>();

    for (int i = 0; i < 10; i++) {
        int id = i;
        var thread = new Thread(() -> {
            var request = new ImmutableRequest(
                "req-" + id, 
                System.currentTimeMillis(), 
                "data-" + id
            );
            handler.handle(request);
        });
        threads.add(thread);
        thread.start();
    }

    for (var thread : threads) {
        thread.join();
    }

    println("Last request: " + handler.getLastRequest());
}
```

Immutable objects (like records) are inherently thread-safe. They can  
be shared freely between threads without synchronization, simplifying  
concurrent code and reducing the risk of bugs.  

### Document Thread Safety  

```java
/**
 * Thread-safe counter using atomic operations.
 * <p>
 * This class is safe for use by multiple threads. All operations
 * are atomic and visibility is guaranteed.
 * </p>
 */
import java.util.concurrent.atomic.AtomicInteger;

class ThreadSafeCounter {
    private final AtomicInteger count = new AtomicInteger(0);

    /**
     * Increments the counter atomically.
     * Thread-safe: uses CAS operations internally.
     */
    public void increment() {
        count.incrementAndGet();
    }

    /**
     * Returns the current count.
     * Thread-safe: atomic read of volatile field.
     */
    public int get() {
        return count.get();
    }
}

void main() {

    println("=== Documenting Thread Safety ===");

    var counter = new ThreadSafeCounter();
    counter.increment();
    println("Count: " + counter.get());
}
```

Clear documentation about thread safety helps other developers use your  
code correctly. State whether a class is thread-safe, conditionally  
thread-safe, or not thread-safe.  

### Decision Guide for Synchronization  

| Scenario                           | Recommended Approach               |
|------------------------------------|-----------------------------------|
| Simple flag (one writer)           | volatile                          |
| Counter with increment             | AtomicInteger/AtomicLong          |
| Multiple operations must be atomic | synchronized or ReentrantLock     |
| Read-heavy, rare writes            | ReadWriteLock                     |
| Producer-consumer                  | BlockingQueue                     |
| Thread-safe map                    | ConcurrentHashMap                 |
| Thread-safe list (read-heavy)      | CopyOnWriteArrayList              |
| Complex state machine              | synchronized with careful design  |
| High-contention counters           | LongAdder                         |

### Testing Concurrent Code  

```java
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

void main() throws InterruptedException {

    println("=== Testing Concurrent Code ===");

    var counter = new AtomicInteger(0);
    int threadCount = 100;
    int iterationsPerThread = 1000;

    var startLatch = new CountDownLatch(1);
    var doneLatch = new CountDownLatch(threadCount);

    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    for (int i = 0; i < threadCount; i++) {
        executor.submit(() -> {
            try {
                startLatch.await();  // Wait for all threads to be ready
                for (int j = 0; j < iterationsPerThread; j++) {
                    counter.incrementAndGet();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                doneLatch.countDown();
            }
        });
    }

    // Start all threads simultaneously
    startLatch.countDown();

    // Wait for completion
    doneLatch.await(30, TimeUnit.SECONDS);
    executor.shutdown();

    int expected = threadCount * iterationsPerThread;
    int actual = counter.get();

    println("Expected: " + expected);
    println("Actual: " + actual);
    println("Test " + (expected == actual ? "PASSED" : "FAILED"));
}
```

Use CountDownLatch to synchronize thread starts for maximum concurrency  
during testing. Run tests many times to increase the chance of detecting  
race conditions.  

## Conclusion  

The Java Memory Model is a critical foundation for writing correct  
concurrent programs. Understanding the JMM helps developers:  

- **Avoid subtle bugs**: Race conditions, visibility issues, and ordering  
  problems can cause intermittent failures that are extremely difficult  
  to debug. Knowledge of the JMM helps identify and prevent these issues.  

- **Write portable code**: The JMM provides guarantees that hold across  
  all JVM implementations and hardware platforms. Code that correctly  
  uses synchronization will work reliably everywhere.  

- **Choose appropriate tools**: Understanding the JMM helps developers  
  select the right synchronization mechanism for each situation, whether  
  it's volatile, synchronized, atomic classes, or concurrent collections.  

- **Reason about program behavior**: The happens-before relationship  
  provides a formal framework for understanding when changes made by  
  one thread will be visible to another.  

### Key Takeaways  

| Concept                 | Summary                                        |
|-------------------------|------------------------------------------------|
| Visibility              | Use volatile or synchronization to ensure      |
|                         | changes are visible across threads             |
| Atomicity               | Use synchronized or atomic classes for         |
|                         | compound operations                            |
| Happens-Before          | Synchronization creates happens-before         |
|                         | relationships that guarantee ordering          |
| Volatile                | Provides visibility and ordering, not          |
|                         | atomicity for compound operations              |
| Synchronized            | Provides mutual exclusion, visibility, and     |
|                         | ordering guarantees                            |
| Atomic Classes          | Lock-free thread-safe operations using CAS     |
| Memory Barriers         | Prevent reordering of memory operations        |
| Immutability            | Eliminates need for synchronization            |

The JMM may seem complex, but its rules exist to enable efficient  
execution while providing guarantees that programmers can rely on.  
By following the best practices outlined in this document and using  
higher-level concurrency utilities when possible, developers can write  
thread-safe code that performs well and behaves correctly across all  
platforms.  
