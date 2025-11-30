# JVM Tuning and Profiling

**JVM tuning** is the process of adjusting Java Virtual Machine parameters to  
optimize application performance, memory usage, and response times. **Profiling**  
involves analyzing runtime behavior to identify bottlenecks, memory leaks, and  
inefficient code paths. Together, these practices form the foundation of Java  
performance engineering.  

Understanding JVM tuning and profiling is essential for building high-performance  
Java applications. Modern applications face demanding requirements: low latency,  
high throughput, efficient resource utilization, and predictable behavior under  
load. Without proper tuning, applications may suffer from excessive garbage  
collection pauses, memory exhaustion, or suboptimal thread utilization.  

Tuning should always be guided by profiling data. Premature optimization based  
on assumptions often leads to wasted effort or even degraded performance. The  
correct approach is to measure first, identify actual bottlenecks, apply targeted  
changes, and validate improvements through repeated measurement.  

## JVM Memory Management Basics  

The JVM manages memory automatically, freeing developers from manual memory  
allocation and deallocation. However, understanding how the JVM organizes and  
manages memory is crucial for effective tuning.  

### Memory Regions Overview  

The JVM divides memory into several distinct regions, each serving a specific  
purpose:  

| Region        | Description                                              |
|---------------|----------------------------------------------------------|
| Heap          | Primary storage for objects; managed by garbage collector|
| Stack         | Per-thread memory for method calls and local variables   |
| Metaspace     | Class metadata, method definitions, constant pools       |
| Code Cache    | JIT-compiled native code                                 |
| Native Memory | Direct buffers, JNI allocations, thread stacks           |

### JVM Memory Architecture Diagram  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           JVM Process Memory                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │                              HEAP                                     │  │
│  │  ┌─────────────────────────┐  ┌─────────────────────────────────────┐ │  │
│  │  │      Young Generation   │  │          Old Generation             │ │  │
│  │  │  ┌───────┐ ┌──────────┐ │  │                                     │ │  │
│  │  │  │ Eden  │ │ Survivor │ │  │    Long-lived objects survive      │ │  │
│  │  │  │       │ │  S0 | S1 │ │  │    multiple GC cycles here         │ │  │
│  │  │  │ New   │ │          │ │  │                                     │ │  │
│  │  │  │objects│ │ Aging    │ │  │    Collected by Major/Full GC      │ │  │
│  │  │  │ here  │ │ objects  │ │  │                                     │ │  │
│  │  │  └───────┘ └──────────┘ │  │                                     │ │  │
│  │  └─────────────────────────┘  └─────────────────────────────────────┘ │  │
│  └───────────────────────────────────────────────────────────────────────┘  │
│                                                                             │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │                           NON-HEAP                                    │  │
│  │  ┌─────────────────┐  ┌─────────────────┐  ┌───────────────────────┐  │  │
│  │  │    Metaspace    │  │   Code Cache    │  │   Direct Buffers      │  │  │
│  │  │                 │  │                 │  │                       │  │  │
│  │  │  Class metadata │  │  JIT compiled   │  │  Off-heap memory      │  │  │
│  │  │  Method data    │  │  native code    │  │  for NIO operations   │  │  │
│  │  │  Constant pool  │  │                 │  │                       │  │  │
│  │  └─────────────────┘  └─────────────────┘  └───────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────┘  │
│                                                                             │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │                        THREAD STACKS                                  │  │
│  │  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐     │  │
│  │  │ Thread1 │  │ Thread2 │  │ Thread3 │  │ Thread4 │  │  ...    │     │  │
│  │  │  Stack  │  │  Stack  │  │  Stack  │  │  Stack  │  │         │     │  │
│  │  └─────────┘  └─────────┘  └─────────┘  └─────────┘  └─────────┘     │  │
│  └───────────────────────────────────────────────────────────────────────┘  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Heap Memory  

The heap is the runtime data area from which memory for all class instances and  
arrays is allocated. It is created when the JVM starts and may increase or  
decrease in size during application runtime.  

**Young Generation**: Where new objects are allocated. Most objects die young  
and are collected quickly through Minor GC. It is further divided into Eden  
space (initial allocation) and Survivor spaces (S0 and S1) for aging objects.  

**Old Generation**: Objects that survive multiple Minor GC cycles are promoted  
here. Collected less frequently through Major GC or Full GC operations.  

### Stack Memory  

Each thread has its own stack, created when the thread is created. The stack  
stores frames containing local variables, partial results, and method invocation  
data. Stack size is fixed per thread and configured via `-Xss`.  

```java
void main() {

    // Each method call creates a new stack frame
    int result = calculateFactorial(5);
    println("Factorial: " + result);
}

int calculateFactorial(int n) {
    // Local variables stored in stack frame
    if (n <= 1) {
        return 1;
    }
    // Recursive call creates new stack frame
    return n * calculateFactorial(n - 1);
}
```

Stack memory is automatically reclaimed when a method returns. Deep recursion  
or excessively large local variables can cause `StackOverflowError`.  

### Metaspace  

Metaspace replaced PermGen in Java 8. It stores class metadata, including:  
- Class definitions and bytecode  
- Method metadata and compiled method information  
- Constant pool data  
- Annotation processing information  

Unlike PermGen, Metaspace uses native memory and can grow dynamically. It is  
still subject to garbage collection when classes are unloaded.  

### Garbage Collection Regions (G1 Collector)  

Modern collectors like G1 organize the heap into regions for more efficient  
collection:  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         G1 Heap Region Layout                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┐  │
│  │  E  │  E  │  S  │  O  │  O  │  E  │  H  │  H  │  O  │  E  │  S  │  O  │  │
│  ├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤  │
│  │  O  │  E  │  O  │  O  │  E  │  E  │  H  │  O  │  O  │  E  │  E  │  O  │  │
│  ├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤  │
│  │  O  │  O  │  E  │  S  │  O  │  E  │  O  │  O  │  E  │  O  │  O  │  E  │  │
│  └─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┘  │
│                                                                             │
│  E = Eden Region    S = Survivor Region    O = Old Region    H = Humongous  │
│                                                                             │
│  - Each region is typically 1-32 MB in size                                 │
│  - Regions can change role dynamically                                      │
│  - Humongous regions store objects larger than half a region                │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Tuning with JVM Flags  

JVM flags control memory allocation, garbage collection behavior, and various  
runtime optimizations. Flags are categorized into standard options (stable across  
releases) and non-standard options (may change between JVM versions).  

### Flag Categories  

| Prefix     | Description                                              |
|------------|----------------------------------------------------------|
| `-`        | Standard options (e.g., `-version`, `-classpath`)        |
| `-X`       | Non-standard options, relatively stable                  |
| `-XX:`     | Advanced options, may change without notice              |
| `-XX:+`    | Enable boolean option                                    |
| `-XX:-`    | Disable boolean option                                   |
| `-XX:=`    | Set numeric or string value                              |

### Heap Size Flags  

#### Maximum Heap Size (-Xmx)  

The `-Xmx` flag sets the maximum heap size the JVM can allocate. When heap  
usage approaches this limit, aggressive garbage collection occurs. If memory  
cannot be freed, `OutOfMemoryError` is thrown.  

```bash
# Set maximum heap to 4 gigabytes
java -Xmx4g MyApplication

# Set maximum heap to 2048 megabytes
java -Xmx2048m MyApplication

# Set maximum heap to specific kilobytes
java -Xmx2097152k MyApplication
```

**Best practices for -Xmx:**  
- Set based on available physical memory (typically 50-75% of total RAM)  
- Account for non-heap memory (Metaspace, native memory, thread stacks)  
- Monitor actual usage before increasing; larger heaps mean longer GC pauses  
- Container deployments should consider memory limits carefully  

#### Initial Heap Size (-Xms)  

The `-Xms` flag sets the initial heap size. The JVM starts with this amount  
and grows toward `-Xmx` as needed. Setting `-Xms` equal to `-Xmx` prevents  
heap resizing during runtime.  

```bash
# Start with 2GB, can grow to 4GB
java -Xms2g -Xmx4g MyApplication

# Fixed heap size of 4GB (recommended for production)
java -Xms4g -Xmx4g MyApplication
```

**Best practices for -Xms:**  
- Set equal to `-Xmx` for production systems to avoid resize overhead  
- Lower values acceptable for development to conserve resources  
- Monitor startup time; larger initial heap may slow JVM initialization  

#### Viewing Current Memory Settings  

```java
void main() {

    var runtime = Runtime.getRuntime();

    long maxMemory = runtime.maxMemory();
    long totalMemory = runtime.totalMemory();
    long freeMemory = runtime.freeMemory();
    long usedMemory = totalMemory - freeMemory;

    println("JVM Memory Information:");
    println("─".repeat(50));
    println("Max Memory (Xmx):     " + formatBytes(maxMemory));
    println("Total Memory:         " + formatBytes(totalMemory));
    println("Used Memory:          " + formatBytes(usedMemory));
    println("Free Memory:          " + formatBytes(freeMemory));
    println("Available Processors: " + runtime.availableProcessors());
}

String formatBytes(long bytes) {
    if (bytes < 1024) return bytes + " B";
    if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
    if (bytes < 1024 * 1024 * 1024) return (bytes / (1024 * 1024)) + " MB";
    return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
}
```

Running this program shows current JVM memory configuration. Use it to verify  
that your flags are applied correctly.  

### Garbage Collection Flags  

The JVM includes several garbage collectors, each optimized for different  
workloads. Selecting the appropriate collector significantly impacts performance.  

#### Garbage Collector Comparison  

| Collector      | Best For                    | Pause Times    | Throughput     |
|----------------|-----------------------------|----------------|----------------|
| G1GC           | General purpose, balanced   | Medium (10-200ms) | High        |
| ZGC            | Low latency, large heaps    | Very low (<1-10ms) | High       |
| Shenandoah     | Low latency, any heap size  | Very low (<10ms)   | High       |
| Parallel GC    | Batch processing, throughput| Higher         | Highest        |
| Serial GC      | Small heaps, single CPU     | Variable       | Low            |

#### G1 Garbage Collector  

G1 (Garbage-First) is the default collector since Java 9. It divides the heap  
into regions and prioritizes collecting regions with the most garbage.  

```bash
# Enable G1GC (default in Java 9+)
java -XX:+UseG1GC MyApplication

# Set target pause time (milliseconds)
java -XX:+UseG1GC -XX:MaxGCPauseMillis=200 MyApplication

# Set heap region size (1MB-32MB, must be power of 2)
java -XX:+UseG1GC -XX:G1HeapRegionSize=16m MyApplication

# Tune concurrent GC threads
java -XX:+UseG1GC -XX:ConcGCThreads=4 MyApplication
```

**Key G1 tuning options:**  

| Flag                        | Description                              |
|-----------------------------|------------------------------------------|
| `-XX:MaxGCPauseMillis=200`  | Target maximum GC pause time             |
| `-XX:G1HeapRegionSize=16m`  | Size of G1 heap regions                  |
| `-XX:G1NewSizePercent=5`    | Minimum young generation percentage      |
| `-XX:G1MaxNewSizePercent=60`| Maximum young generation percentage      |
| `-XX:InitiatingHeapOccupancyPercent=45` | Heap occupancy to trigger marking |

#### ZGC (Z Garbage Collector)  

ZGC is designed for applications requiring extremely low latency. It performs  
most work concurrently, keeping pause times consistently under 10ms regardless  
of heap size.  

```bash
# Enable ZGC
java -XX:+UseZGC MyApplication

# ZGC with generational mode (Java 21+)
java -XX:+UseZGC -XX:+ZGenerational MyApplication

# Set concurrent GC threads
java -XX:+UseZGC -XX:ConcGCThreads=4 MyApplication
```

**ZGC characteristics:**  
- Pause times typically under 1ms  
- Scales to multi-terabyte heaps  
- Slightly lower throughput than G1  
- Best for latency-sensitive applications  

#### Shenandoah GC  

Shenandoah is another low-pause collector, performing concurrent compaction  
to minimize stop-the-world pauses.  

```bash
# Enable Shenandoah
java -XX:+UseShenandoahGC MyApplication

# Set heuristics mode
java -XX:+UseShenandoahGC -XX:ShenandoahGCHeuristics=adaptive MyApplication

# Compact mode for smaller heaps
java -XX:+UseShenandoahGC -XX:ShenandoahGCHeuristics=compact MyApplication
```

#### Parallel GC  

Parallel GC maximizes throughput by using multiple threads for collection.  
It is ideal for batch processing where pause times are acceptable.  

```bash
# Enable Parallel GC
java -XX:+UseParallelGC MyApplication

# Set number of GC threads
java -XX:+UseParallelGC -XX:ParallelGCThreads=8 MyApplication

# Enable adaptive sizing
java -XX:+UseParallelGC -XX:+UseAdaptiveSizePolicy MyApplication
```

### Other Useful Flags  

#### Metaspace Configuration  

```bash
# Set maximum Metaspace size
java -XX:MaxMetaspaceSize=256m MyApplication

# Set initial Metaspace size
java -XX:MetaspaceSize=128m MyApplication

# Limit class metadata allocation
java -XX:CompressedClassSpaceSize=128m MyApplication
```

#### GC Logging Flags  

Modern GC logging (Java 9+) uses the Unified Logging Framework:  

```bash
# Basic GC logging
java -Xlog:gc MyApplication

# Detailed GC logging with timestamps
java -Xlog:gc*:file=gc.log:time,uptime,level,tags MyApplication

# Include heap information
java -Xlog:gc+heap=debug:file=gc.log MyApplication

# Log GC pauses only
java -Xlog:gc+pause=info MyApplication
```

**Legacy GC logging (Java 8):**  

```bash
# Print GC details (Java 8)
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps MyApplication

# Write to file
java -XX:+PrintGCDetails -Xloggc:gc.log MyApplication
```

#### Heap Dump Configuration  

```bash
# Generate heap dump on OutOfMemoryError
java -XX:+HeapDumpOnOutOfMemoryError MyApplication

# Specify heap dump location
java -XX:+HeapDumpOnOutOfMemoryError \
     -XX:HeapDumpPath=/var/dumps/heap.hprof MyApplication

# Run script on OutOfMemoryError
java -XX:+HeapDumpOnOutOfMemoryError \
     -XX:OnOutOfMemoryError="./notify-admin.sh %p" MyApplication
```

#### Thread Stack Size  

```bash
# Set thread stack size to 512KB
java -Xss512k MyApplication

# Larger stacks for deep recursion
java -Xss2m MyApplication
```

### Complete Flag Reference Table  

| Flag                              | Description                          |
|-----------------------------------|--------------------------------------|
| `-Xmx<size>`                      | Maximum heap size                    |
| `-Xms<size>`                      | Initial heap size                    |
| `-Xss<size>`                      | Thread stack size                    |
| `-XX:+UseG1GC`                    | Enable G1 garbage collector          |
| `-XX:+UseZGC`                     | Enable Z garbage collector           |
| `-XX:+UseShenandoahGC`            | Enable Shenandoah collector          |
| `-XX:+UseParallelGC`              | Enable Parallel collector            |
| `-XX:MaxGCPauseMillis=<ms>`       | Target maximum GC pause time         |
| `-XX:MaxMetaspaceSize=<size>`     | Maximum Metaspace size               |
| `-XX:+HeapDumpOnOutOfMemoryError` | Dump heap on OOM                     |
| `-XX:HeapDumpPath=<path>`         | Heap dump file location              |
| `-Xlog:gc*`                       | Enable GC logging                    |
| `-XX:+PrintFlagsFinal`            | Print all flag values                |
| `-XX:+UnlockDiagnosticVMOptions`  | Enable diagnostic options            |
| `-XX:+UnlockExperimentalVMOptions`| Enable experimental options          |

## Profiling Tools  

Profiling tools provide visibility into application behavior, enabling data-  
driven optimization decisions. Java includes several powerful profiling tools  
that integrate directly with the JVM.  

### Java Flight Recorder (JFR)  

Java Flight Recorder is a low-overhead profiling framework built into the JVM.  
It continuously collects diagnostic data with minimal performance impact,  
making it suitable for production environments.  

#### Enabling JFR  

```bash
# Start recording from command line
java -XX:StartFlightRecording=duration=60s,filename=recording.jfr \
     MyApplication

# Start with continuous recording
java -XX:StartFlightRecording=disk=true,maxage=1h,maxsize=500m \
     MyApplication

# Start recording without disk writes
java -XX:StartFlightRecording=memory MyApplication
```

#### Recording Settings  

| Setting            | Description                                       |
|--------------------|---------------------------------------------------|
| `duration`         | Recording length (e.g., `60s`, `5m`, `1h`)        |
| `filename`         | Output file path                                  |
| `disk`             | Enable disk repository (`true`/`false`)           |
| `maxage`           | Maximum age of data to keep                       |
| `maxsize`          | Maximum disk space for recording                  |
| `settings`         | Predefined profile (`default`, `profile`)         |

#### Starting Recording Programmatically  

```java
import jdk.jfr.Configuration;
import jdk.jfr.Recording;

import java.nio.file.Path;

void main() throws Exception {

    // Load a configuration
    var config = Configuration.getConfiguration("profile");

    // Create and start recording
    var recording = new Recording(config);
    recording.setName("MyRecording");
    recording.setMaxAge(java.time.Duration.ofMinutes(10));
    recording.start();

    println("Recording started: " + recording.getName());
    println("Recording ID: " + recording.getId());

    // Run your application workload here
    simulateWorkload();

    // Stop and save recording
    recording.stop();
    recording.dump(Path.of("programmatic-recording.jfr"));

    println("Recording saved to programmatic-recording.jfr");
    recording.close();
}

void simulateWorkload() throws InterruptedException {
    var list = new ArrayList<String>();
    for (int i = 0; i < 100000; i++) {
        list.add("Item " + i);
        if (i % 10000 == 0) {
            Thread.sleep(100);
            println("Processed " + i + " items");
        }
    }
}
```

This example creates a JFR recording programmatically, which is useful for  
capturing specific application scenarios or integrating profiling into  
automated testing.  

#### Creating Custom JFR Events  

```java
import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Name("com.example.OrderProcessed")
@Label("Order Processed")
@Category("Business Events")
@Description("Fired when an order is successfully processed")
class OrderProcessedEvent extends Event {

    @Label("Order ID")
    String orderId;

    @Label("Customer")
    String customer;

    @Label("Total Amount")
    double amount;

    @Label("Processing Time (ms)")
    long processingTimeMs;
}

void main() {

    // Process several orders
    for (int i = 1; i <= 5; i++) {
        processOrder("ORD-" + i, "Customer-" + i, i * 100.0);
    }

    println("Order processing complete");
}

void processOrder(String orderId, String customer, double amount) {

    var event = new OrderProcessedEvent();
    event.orderId = orderId;
    event.customer = customer;
    event.amount = amount;

    long startTime = System.currentTimeMillis();

    // Simulate order processing
    try {
        Thread.sleep((long) (Math.random() * 100 + 50));
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }

    event.processingTimeMs = System.currentTimeMillis() - startTime;

    // Commit the event if enabled
    event.commit();

    println("Processed order: " + orderId + 
            " in " + event.processingTimeMs + "ms");
}
```

Custom events enable tracking application-specific metrics alongside standard  
JVM events. They are invaluable for correlating business logic with system  
behavior.  

#### Analyzing JFR Recordings  

Use `jfr` command-line tool to extract data from recordings:  

```bash
# Print recording summary
jfr summary recording.jfr

# Print specific event types
jfr print --events jdk.GCPhasePause recording.jfr

# Print CPU sampling data
jfr print --events jdk.ExecutionSample recording.jfr

# Export to JSON format
jfr print --json recording.jfr > recording.json

# List all event types
jfr metadata recording.jfr
```

### VisualVM  

VisualVM is a graphical tool for monitoring, troubleshooting, and profiling  
Java applications. It combines several command-line JDK tools into an intuitive  
interface.  

#### Key Features  

| Feature             | Description                                        |
|---------------------|---------------------------------------------------|
| Monitor             | Real-time CPU, memory, classes, threads display   |
| Threads             | Thread state visualization and deadlock detection |
| Sampler             | CPU and memory sampling with low overhead         |
| Profiler            | Detailed method-level CPU and memory profiling    |
| Heap Dump           | Capture and analyze heap snapshots                |
| Thread Dump         | Capture and view thread stack traces              |

#### Installation and Setup  

VisualVM is available as a standalone download or bundled with GraalVM. For  
remote profiling, start the application with JMX enabled:  

```bash
# Enable JMX for remote connection
java -Dcom.sun.management.jmxremote \
     -Dcom.sun.management.jmxremote.port=9090 \
     -Dcom.sun.management.jmxremote.authenticate=false \
     -Dcom.sun.management.jmxremote.ssl=false \
     MyApplication
```

#### Diagnosing Memory Leaks  

Memory leaks occur when objects are no longer needed but remain reachable,  
preventing garbage collection. VisualVM helps identify leaks through:  

1. **Monitor tab**: Watch heap usage grow over time without stabilizing  
2. **Sampler tab**: Identify classes consuming the most memory  
3. **Heap Dump**: Analyze object references to find retention paths  

```java
import java.util.HashMap;

void main() throws InterruptedException {

    // Simulated memory leak: map grows indefinitely
    var leakyMap = new HashMap<String, byte[]>();

    int counter = 0;
    while (counter < 1000) {
        // Each entry holds 1MB, never removed
        leakyMap.put("key-" + counter, new byte[1024 * 1024]);
        counter++;

        if (counter % 100 == 0) {
            println("Entries: " + counter + 
                    ", Memory: " + formatBytes(
                        Runtime.getRuntime().totalMemory() - 
                        Runtime.getRuntime().freeMemory()));
        }

        Thread.sleep(100);
    }
}

String formatBytes(long bytes) {
    return String.format("%.2f MB", bytes / (1024.0 * 1024));
}
```

When profiling this application with VisualVM:  
1. Connect to the running application  
2. Open the Monitor tab to observe heap growth  
3. Take a heap dump when memory is high  
4. Analyze retained size by class to find the byte[] arrays  
5. Trace references to discover the HashMap holding them  

### JDK Mission Control  

JDK Mission Control (JMC) is an advanced suite of tools for analyzing JFR  
recordings. It provides deep insights into application behavior through  
specialized views and automated analysis.  

#### Key Analysis Views  

| View                | Purpose                                           |
|---------------------|---------------------------------------------------|
| Outline             | High-level recording summary                      |
| Method Profiling    | Hot methods and call stacks                       |
| Memory              | Allocation patterns and object statistics         |
| Lock Instances      | Contention analysis and lock profiling            |
| Garbage Collections | GC events, durations, and pause analysis          |
| Event Browser       | Raw access to all recorded events                 |

#### Automated Analysis  

JMC includes an automated analysis engine that examines recordings and  
provides actionable recommendations:  

- Memory pressure warnings  
- Thread contention hotspots  
- I/O bottleneck identification  
- GC inefficiency detection  
- Code execution anomalies  

#### Starting Recording for JMC  

```bash
# Detailed recording for JMC analysis
java -XX:StartFlightRecording=settings=profile,\
     duration=5m,\
     filename=analysis.jfr \
     MyApplication

# Continuous recording for production
java -XX:StartFlightRecording=settings=default,\
     disk=true,\
     maxage=24h,\
     dumponexit=true,\
     dumponexitpath=exit-recording.jfr \
     MyApplication
```

### Profiling Tools Comparison  

| Tool               | Best For                    | Overhead   | Production-Safe |
|--------------------|-----------------------------|------------|-----------------|
| JFR                | Continuous monitoring       | Very Low   | Yes             |
| VisualVM           | Interactive debugging       | Medium     | Development     |
| JMC                | Deep analysis of JFR files  | N/A        | Analysis tool   |
| async-profiler     | CPU/allocation profiling    | Low        | Yes             |
| jstack             | Quick thread dump           | Minimal    | Yes             |
| jmap               | Heap statistics/dump        | High       | With caution    |

## Performance Tuning Workflow  

Effective performance tuning follows a systematic, iterative process. Avoid  
random flag changes; instead, use data to guide decisions.  

### Step-by-Step Tuning Process  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      Performance Tuning Workflow                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   ┌─────────┐     ┌─────────┐     ┌─────────┐     ┌─────────┐             │
│   │  1.     │     │  2.     │     │  3.     │     │  4.     │             │
│   │ MEASURE │────►│ ANALYZE │────►│  TUNE   │────►│ VERIFY  │────┐        │
│   │         │     │         │     │         │     │         │    │        │
│   └─────────┘     └─────────┘     └─────────┘     └─────────┘    │        │
│        ▲                                                          │        │
│        │                                                          │        │
│        └──────────────────────────────────────────────────────────┘        │
│                              Iterate                                        │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Step 1: Measure (Identify Performance Issues)  

Before tuning, establish baseline metrics:  

```bash
# Enable GC logging
java -Xlog:gc*:file=baseline-gc.log:time,level,tags \
     -XX:+HeapDumpOnOutOfMemoryError \
     MyApplication

# Start JFR recording
java -XX:StartFlightRecording=duration=10m,filename=baseline.jfr \
     MyApplication
```

Key metrics to capture:  
- Response times (p50, p95, p99 percentiles)  
- Throughput (requests/second)  
- GC pause times and frequency  
- Heap usage patterns  
- CPU utilization  
- Thread counts and states  

### Step 2: Analyze (Collect and Interpret Profiling Data)  

Use profiling tools to identify bottlenecks:  

```bash
# Analyze GC patterns
jfr print --events jdk.GCPhasePause,jdk.GarbageCollection baseline.jfr

# Find hot methods
jfr print --events jdk.ExecutionSample baseline.jfr | \
    sort | uniq -c | sort -rn | head -20

# Check memory allocation
jfr print --events jdk.ObjectAllocationInNewTLAB baseline.jfr
```

Common findings and solutions:  

| Finding                        | Likely Cause                    | Solution            |
|--------------------------------|---------------------------------|---------------------|
| Frequent Full GC               | Heap too small or leak          | Increase Xmx, fix leak |
| High GC pause times            | Large old generation            | Tune GC, consider ZGC  |
| Many young GC collections      | High allocation rate            | Reduce object creation |
| Thread contention              | Lock bottlenecks                | Reduce synchronization |
| High CPU in specific methods   | Algorithmic inefficiency        | Optimize code          |

### Step 3: Tune (Adjust JVM Flags)  

Apply targeted changes based on analysis:  

```bash
# If heap is too small
java -Xms4g -Xmx4g MyApplication

# If GC pauses are too long
java -XX:+UseZGC MyApplication

# If throughput is priority
java -XX:+UseParallelGC -XX:ParallelGCThreads=8 MyApplication

# Combined tuning example
java -Xms8g -Xmx8g \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=100 \
     -XX:+HeapDumpOnOutOfMemoryError \
     -Xlog:gc*:file=tuned-gc.log:time,level,tags \
     MyApplication
```

### Step 4: Verify (Validate Improvements)  

Compare tuned metrics against baseline:  

```bash
# Record with same duration as baseline
java -XX:StartFlightRecording=duration=10m,filename=tuned.jfr \
     [tuning flags] \
     MyApplication
```

Verification criteria:  
- Did target metric improve?  
- Did other metrics degrade?  
- Is improvement consistent under load?  
- Does change behave as expected?  

### Sample Workflow: VisualVM and JFR  

This example demonstrates a complete tuning cycle:  

**Scenario**: Application has slow response times during peak load.  

**Step 1: Measure**  
```bash
# Start with JFR recording and GC logging
java -XX:StartFlightRecording=settings=profile,duration=30m,\
     filename=perf-issue.jfr \
     -Xlog:gc*:file=perf-gc.log:time,level,tags \
     -Xms2g -Xmx2g \
     MyApplication
```

**Step 2: Analyze in JMC**  
Open `perf-issue.jfr` in JDK Mission Control:  
1. Check Automated Analysis for warnings  
2. Review GC tab: many Full GCs taking >500ms  
3. Memory tab: heap consistently near 100%  
4. Conclusion: Heap is too small  

**Step 3: Tune**  
```bash
# Increase heap and set initial = max
java -Xms4g -Xmx4g \
     -XX:StartFlightRecording=settings=profile,duration=30m,\
     filename=after-tune.jfr \
     -Xlog:gc*:file=after-tune-gc.log:time,level,tags \
     MyApplication
```

**Step 4: Verify**  
Compare recordings in JMC:  
- Full GC count: 47 → 3  
- Average GC pause: 423ms → 45ms  
- p99 response time: 2.3s → 0.8s  

The change achieved the goal. Document the configuration and monitor  
production metrics.  

## Best Practices  

Following these guidelines leads to more effective and maintainable  
performance tuning.  

### Start with Defaults  

Modern JVMs are well-tuned for general workloads. The HotSpot JVM uses  
ergonomics to automatically configure many settings based on available  
resources.  

```bash
# Let JVM choose GC and heap size
java MyApplication

# View auto-selected options
java -XX:+PrintFlagsFinal -version 2>&1 | grep -E "UseG1GC|MaxHeapSize"
```

Only override defaults when profiling reveals specific issues.  

### Test Under Realistic Workloads  

Tuning in isolation often produces misleading results. Performance  
characteristics change significantly between test and production:  

| Factor              | Test Environment        | Production              |
|---------------------|------------------------|-------------------------|
| Data volume         | Sample data            | Full dataset            |
| Concurrent users    | 1-10                   | 100-10000+              |
| Request patterns    | Uniform                | Spiky, varied           |
| Duration            | Minutes                | Days/weeks              |
| External services   | Mocked                 | Real latency            |

Use load testing tools (JMeter, Gatling, k6) to simulate production  
conditions during tuning.  

### Monitor GC Logs and Heap Usage  

Establish continuous monitoring in production:  

```bash
# Production GC logging configuration
java -Xlog:gc*:file=/var/log/app/gc.log:time,uptime,level,tags:filecount=10,filesize=100m \
     -XX:+HeapDumpOnOutOfMemoryError \
     -XX:HeapDumpPath=/var/dumps/ \
     MyApplication
```

Key monitoring metrics:  
- GC pause time distribution (especially p99)  
- GC frequency and type (Minor vs Full)  
- Heap occupancy over time  
- Promotion rate (objects moving to old generation)  
- Allocation rate  

### Profile Before Changing Flags  

Never tune blindly. Each change should address a specific, measured issue:  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    Flag Change Decision Process                             │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   Question                           │  Action                              │
│   ─────────────────────────────────  │  ──────────────────────────────────  │
│   Is there a performance problem?    │  No  → Don't tune                   │
│   Have you profiled?                 │  No  → Profile first                │
│   Did profiling identify root cause? │  No  → Investigate more             │
│   Will this flag address the cause?  │  No  → Find correct solution        │
│   Have you tested the change?        │  No  → Test in staging              │
│   Does it improve target metric?     │  No  → Revert and try alternative   │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Document Configuration Decisions  

Maintain records of tuning decisions:  

```bash
# production-jvm-config.sh
# JVM Configuration for Order Processing Service
# Last updated: 2024-01-15
# Owner: Performance Team

# Heap Configuration
# Reason: Production load analysis showed peak usage of 3.2GB
HEAP_OPTS="-Xms4g -Xmx4g"

# GC Configuration  
# Reason: p99 latency requirement of <100ms
GC_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=80"

# Monitoring
# Reason: Required for production troubleshooting
MONITOR_OPTS="-Xlog:gc*:file=/var/log/gc.log:time,level,tags"
MONITOR_OPTS="$MONITOR_OPTS -XX:+HeapDumpOnOutOfMemoryError"
MONITOR_OPTS="$MONITOR_OPTS -XX:HeapDumpPath=/var/dumps"

java $HEAP_OPTS $GC_OPTS $MONITOR_OPTS -jar app.jar
```

## Common Pitfalls  

Avoid these frequent mistakes when tuning JVM performance.  

### Setting Heap Sizes Incorrectly  

**Too Small**: Causes frequent garbage collection, OutOfMemoryError, and  
degraded performance. The JVM spends more time collecting than executing  
application code.  

**Too Large**: Results in longer GC pauses, slower startup, and wasted  
resources. Very large heaps may exceed physical memory, causing swapping.  

```bash
# Incorrect: Heap larger than physical RAM
java -Xmx32g MyApplication  # On machine with 16GB RAM

# Incorrect: Heap too small for workload
java -Xmx256m MyApplication  # With 2GB working set

# Correct: Sized based on analysis, accounting for non-heap
java -Xmx12g MyApplication  # On 16GB machine (leaves room for OS, Metaspace)
```

### Misinterpreting GC Logs  

Common log interpretation errors:  

| Misinterpretation           | Reality                                  |
|-----------------------------|------------------------------------------|
| All GC is bad               | Minor GC is normal and efficient         |
| Longer pause = bigger heap  | Pause depends on live objects, not heap  |
| More GC threads always helps| Too many causes contention               |
| G1 needs extensive tuning   | Defaults work well for most workloads    |

**Example log entry:**  
```
[2024-01-15T10:30:00.123+0000] GC(42) Pause Young (Normal) 
    (G1 Evacuation Pause) 2048M->1024M(4096M) 45.123ms
```

Interpretation:  
- Minor GC (#42), copying live objects from young generation  
- Heap reduced from 2GB to 1GB (4GB max)  
- Pause of 45ms - acceptable for G1  

### Ignoring Application-Level Bottlenecks  

JVM tuning cannot fix algorithmic problems:  

```java
void main() {

    // No amount of JVM tuning helps this inefficient code
    var list = new ArrayList<String>();
    for (int i = 0; i < 100000; i++) {
        list.add("item-" + i);
    }

    // O(n) lookup instead of O(1) with Set
    for (int i = 0; i < 100000; i++) {
        boolean found = list.contains("item-" + i);  // Slow!
    }
}
```

Profiling with JFR or VisualVM would show hot spots in `ArrayList.contains`.  
The solution is changing data structure, not GC tuning.  

### Over-Reliance on Tuning Without Profiling  

The "cargo cult" approach of copying configurations without understanding:  

```bash
# Copied from blog post without context
java -Xms32g -Xmx32g \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=50 \
     -XX:G1HeapRegionSize=32m \
     -XX:G1NewSizePercent=30 \
     -XX:G1MaxNewSizePercent=50 \
     -XX:InitiatingHeapOccupancyPercent=35 \
     -XX:G1MixedGCLiveThresholdPercent=85 \
     -XX:ConcGCThreads=8 \
     -XX:ParallelGCThreads=16 \
     MyApplication
```

Problems with this approach:  
- Heap size may be wrong for your hardware  
- Pause time target may be unrealistic  
- Thread counts may not match CPU count  
- Some flags may conflict or be deprecated  

### Tuning in Wrong Environment  

Development and production environments differ significantly:  

| Aspect                | Development           | Production            |
|-----------------------|-----------------------|-----------------------|
| Heap size             | 512MB                 | 4GB+                  |
| CPU cores             | 4                     | 16+                   |
| OS memory             | Shared                | Dedicated             |
| JIT optimization      | Minimal               | Fully warmed          |
| Class loading         | Dynamic               | Stable                |

Always validate tuning changes in an environment that mirrors production.  

## Examples  

These examples provide starting configurations for common deployment scenarios.  
Adjust based on your specific requirements and profiling results.  

### Web Application Configuration  

Web applications typically prioritize low latency and consistent response times.  

```bash
#!/bin/bash
# Web Application JVM Configuration
# Characteristics: Many concurrent requests, session state, moderate heap

# Heap: 4GB for medium traffic site
HEAP="-Xms4g -Xmx4g"

# G1GC with low pause target
GC="-XX:+UseG1GC"
GC="$GC -XX:MaxGCPauseMillis=100"
GC="$GC -XX:+ParallelRefProcEnabled"

# Large code cache for compiled JSPs/servlets
CODE_CACHE="-XX:ReservedCodeCacheSize=256m"

# Thread stack (default is usually fine)
STACK="-Xss512k"

# Metaspace for web frameworks with many classes
META="-XX:MaxMetaspaceSize=256m"

# Production monitoring
MONITOR="-Xlog:gc*:file=/var/log/app/gc.log:time,uptime:filecount=5,filesize=50m"
MONITOR="$MONITOR -XX:+HeapDumpOnOutOfMemoryError"
MONITOR="$MONITOR -XX:HeapDumpPath=/var/dumps/"

# JFR for production profiling
JFR="-XX:StartFlightRecording=disk=true,maxage=12h,maxsize=500m"
JFR="$JFR -XX:FlightRecorderOptions=repository=/var/jfr/"

java $HEAP $GC $CODE_CACHE $STACK $META $MONITOR $JFR \
     -jar web-application.jar
```

**Rationale:**  
- Fixed heap prevents resize pauses during traffic spikes  
- G1GC provides predictable pause times  
- Generous Metaspace for framework class loading  
- Continuous JFR for diagnosing production issues  

### Microservice Configuration  

Microservices need fast startup, small footprint, and container awareness.  

```bash
#!/bin/bash
# Microservice JVM Configuration
# Characteristics: Small heap, fast startup, container deployment

# Smaller heap for microservice (adjust based on service)
HEAP="-Xms512m -Xmx512m"

# ZGC for consistent low latency (or G1 for smaller heaps)
GC="-XX:+UseZGC"

# Enable container support (default in modern JVMs)
CONTAINER="-XX:+UseContainerSupport"
CONTAINER="$CONTAINER -XX:MaxRAMPercentage=75.0"

# Optimize for startup time
STARTUP="-XX:TieredStopAtLevel=1"
STARTUP="$STARTUP -XX:+UseSerialGC"  # Use for very small services

# Alternative: CDS for faster startup
# Generate: java -Xshare:dump
CDS="-Xshare:on"

# Minimal monitoring in container (use sidecar for full logging)
MONITOR="-Xlog:gc:stderr:time"
MONITOR="$MONITOR -XX:+HeapDumpOnOutOfMemoryError"
MONITOR="$MONITOR -XX:HeapDumpPath=/tmp/"

# For production microservice with moderate load
java $HEAP $GC $CONTAINER $MONITOR \
     -jar microservice.jar

# For startup-optimized (dev/test)
# java $HEAP $STARTUP $CDS $MONITOR -jar microservice.jar
```

**Rationale:**  
- Container awareness respects cgroup limits  
- MaxRAMPercentage for automatic sizing in containers  
- ZGC provides low latency without tuning  
- Startup options available for faster cold starts  

### High-Throughput Batch Job Configuration  

Batch jobs prioritize throughput over latency, processing maximum data per  
unit time.  

```bash
#!/bin/bash
# Batch Processing JVM Configuration
# Characteristics: Large heap, throughput priority, long-running

# Large heap for batch data processing
HEAP="-Xms16g -Xmx16g"

# Parallel GC maximizes throughput
GC="-XX:+UseParallelGC"
GC="$GC -XX:ParallelGCThreads=12"
GC="$GC -XX:+UseNUMA"

# Alternative: G1 if pauses become too long
# GC="-XX:+UseG1GC -XX:MaxGCPauseMillis=500"

# Large young generation for allocation-heavy workloads
YOUNG="-XX:NewRatio=2"

# Aggressive inlining for hot paths
JIT="-XX:MaxInlineSize=100"
JIT="$JIT -XX:FreqInlineSize=200"

# Monitoring for batch analysis
MONITOR="-Xlog:gc*:file=/var/log/batch/gc.log:time,uptime"
MONITOR="$MONITOR -XX:+HeapDumpOnOutOfMemoryError"

# JFR for post-run analysis
JFR="-XX:StartFlightRecording=settings=profile,dumponexit=true"
JFR="$JFR -XX:FlightRecorderOptions=repository=/var/jfr/"

java $HEAP $GC $YOUNG $JIT $MONITOR $JFR \
     -jar batch-processor.jar
```

**Rationale:**  
- Parallel GC provides highest throughput  
- Large heap accommodates batch data sets  
- NUMA support for multi-socket systems  
- Aggressive JIT settings for long-running hot paths  

### Configuration Summary Table  

| Scenario           | Heap    | GC           | Key Priorities            |
|--------------------|---------|--------------|---------------------------|
| Web Application    | 4-8GB   | G1GC         | Low latency, consistent   |
| Microservice       | 256M-1GB| ZGC/G1GC     | Fast startup, small footprint |
| Batch Processing   | 8-32GB  | Parallel GC  | Throughput, large dataset |
| Low-Latency Trading| 4-16GB  | ZGC          | Sub-ms pauses             |
| Desktop/GUI        | 256M-2GB| G1GC         | Responsive, modest heap   |

### Complete Example Application  

```java
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

void main() {

    println("=== JVM Configuration Analysis ===");
    println();

    // Runtime info
    var runtime = Runtime.getRuntime();
    println("Available Processors: " + runtime.availableProcessors());
    println();

    // Memory configuration
    println("Memory Configuration:");
    println("  Max Memory (-Xmx):     " + formatBytes(runtime.maxMemory()));
    println("  Total Memory (current):" + formatBytes(runtime.totalMemory()));
    println("  Free Memory:           " + formatBytes(runtime.freeMemory()));
    println("  Used Memory:           " + 
            formatBytes(runtime.totalMemory() - runtime.freeMemory()));
    println();

    // Garbage collector info
    println("Garbage Collectors:");
    for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
        println("  " + gc.getName());
        println("    Collection Count: " + gc.getCollectionCount());
        println("    Collection Time:  " + gc.getCollectionTime() + "ms");
    }
    println();

    // Memory pools
    MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
    println("Heap Memory Usage:");
    var heapUsage = memory.getHeapMemoryUsage();
    println("  Init:      " + formatBytes(heapUsage.getInit()));
    println("  Used:      " + formatBytes(heapUsage.getUsed()));
    println("  Committed: " + formatBytes(heapUsage.getCommitted()));
    println("  Max:       " + formatBytes(heapUsage.getMax()));
    println();

    println("Non-Heap Memory Usage:");
    var nonHeapUsage = memory.getNonHeapMemoryUsage();
    println("  Init:      " + formatBytes(nonHeapUsage.getInit()));
    println("  Used:      " + formatBytes(nonHeapUsage.getUsed()));
    println("  Committed: " + formatBytes(nonHeapUsage.getCommitted()));
    println();

    // JVM arguments
    println("JVM Arguments:");
    var runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    for (String arg : runtimeMXBean.getInputArguments()) {
        println("  " + arg);
    }
}

String formatBytes(long bytes) {
    if (bytes < 0) return "N/A";
    if (bytes < 1024) return bytes + " B";
    if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
    if (bytes < 1024 * 1024 * 1024) return (bytes / (1024 * 1024)) + " MB";
    return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
}
```

This utility displays current JVM configuration. Run it with different flags  
to verify settings are applied correctly.  

## Conclusion  

JVM tuning and profiling are essential skills for building high-performance  
Java applications. Effective performance engineering requires a disciplined,  
data-driven approach rather than ad-hoc flag adjustments.  

### Key Takeaways  

| Principle                   | Guidance                                     |
|-----------------------------|----------------------------------------------|
| Measure First               | Always profile before tuning                 |
| Start Simple                | Use JVM defaults until proven insufficient   |
| Make One Change at a Time   | Isolate variables to understand impact       |
| Validate Improvements       | Confirm changes achieve desired outcomes     |
| Test Realistically          | Use production-like conditions               |
| Document Decisions          | Record rationale for future reference        |
| Monitor Continuously        | Track metrics to detect regressions          |

### Balancing Performance, Stability, and Maintainability  

The goal of tuning is not maximum performance at any cost. Instead, aim for  
the optimal balance:  

**Performance**: Meet response time and throughput requirements with  
appropriate resource utilization. Avoid over-optimization that yields  
diminishing returns.  

**Stability**: Ensure consistent behavior under varying loads. A well-tuned  
application handles traffic spikes gracefully without crashes or severe  
degradation.  

**Maintainability**: Keep configurations understandable and documented. Complex  
tuning that nobody understands becomes a liability during incidents.  

### Continued Learning  

JVM performance engineering evolves with each Java release. Stay current by:  
- Reading JVM release notes for new GC features and deprecations  
- Following OpenJDK mailing lists and JEPs  
- Experimenting with new collectors (ZGC, Shenandoah) as they mature  
- Practicing with profiling tools on sample applications  
- Reviewing production metrics to understand real-world behavior  

With the foundation provided in this document and continued practice, you can  
effectively diagnose performance issues, apply targeted improvements, and  
build Java applications that meet demanding performance requirements.  
