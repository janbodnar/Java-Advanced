# HotSpot JVM  

The **HotSpot JVM** (Java Virtual Machine) is the primary execution engine for Java  
applications. Developed by Sun Microsystems and now maintained by Oracle, HotSpot  
is the default JVM included in the Oracle JDK and OpenJDK distributions. It earned  
its name from its ability to identify and optimize "hot spots" in code—sections  
that execute frequently—through Just-In-Time (JIT) compilation.  

HotSpot revolutionized Java performance by introducing adaptive optimization  
techniques that allow Java applications to achieve near-native execution speeds.  
Unlike static compilers that optimize code before execution, HotSpot continuously  
monitors application behavior at runtime and applies targeted optimizations to  
the most performance-critical code paths.  

Understanding HotSpot's internals is essential for Java developers who want to  
write high-performance applications, tune JVM settings for production workloads,  
or diagnose performance issues. This document provides a comprehensive overview  
of HotSpot's architecture, execution model, garbage collection, memory management,  
and performance tuning techniques.  

HotSpot's origins trace back to the mid-1990s at Longview Technologies, which  
was acquired by Sun Microsystems in 1997. The first HotSpot JVM was released with  
Java 1.2, and it became the default JVM starting with Java 1.3. Over the years,  
HotSpot has evolved significantly, incorporating modern garbage collectors,  
tiered compilation, and integration with advanced technologies like GraalVM.  


## HotSpot Architecture  

HotSpot's architecture is designed to balance startup performance with peak  
throughput. The JVM consists of several key components that work together to  
load, verify, compile, and execute Java bytecode efficiently.  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           HotSpot JVM Architecture                          │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │                         Class Loader Subsystem                      │   │
│   │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                  │   │
│   │  │  Bootstrap  │  │  Extension  │  │ Application │                  │   │
│   │  │   Loader    │  │   Loader    │  │   Loader    │                  │   │
│   │  └─────────────┘  └─────────────┘  └─────────────┘                  │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                    │                                        │
│                                    ▼                                        │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │                         Runtime Data Areas                          │   │
│   │  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────────────┐  │   │
│   │  │   Method    │  │    Heap     │  │    Per-Thread Areas         │  │   │
│   │  │    Area     │  │  (Objects)  │  │  ┌───────┐ ┌────────────┐   │  │   │
│   │  │ (Metaspace) │  │             │  │  │ Stack │ │ PC Register│   │  │   │
│   │  └─────────────┘  └─────────────┘  │  └───────┘ └────────────┘   │  │   │
│   │                                    └─────────────────────────────┘  │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                    │                                        │
│                                    ▼                                        │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │                         Execution Engine                            │   │
│   │  ┌─────────────┐  ┌─────────────────────────────┐  ┌─────────────┐  │   │
│   │  │ Interpreter │  │       JIT Compiler          │  │   Garbage   │  │   │
│   │  │             │  │  ┌─────┐      ┌─────────┐   │  │  Collector  │  │   │
│   │  │             │  │  │ C1  │      │   C2    │   │  │             │  │   │
│   │  │             │  │  │Client     │ Server  │   │  │             │  │   │
│   │  └─────────────┘  │  └─────┘      └─────────┘   │  └─────────────┘  │   │
│   │                   └─────────────────────────────┘                   │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Class Loader Subsystem  

The class loader subsystem is responsible for loading, linking, and initializing  
Java classes. HotSpot uses a hierarchical class loading model with three main  
class loaders that work together using the delegation principle.  

| Loader Type     | Responsibility                                             |
|-----------------|------------------------------------------------------------|
| Bootstrap       | Loads core Java classes from rt.jar (or modules in Java 9+)|
| Extension/Platform | Loads classes from extension directories                |
| Application     | Loads classes from the application classpath               |

When loading a class, each loader first delegates to its parent. Only if the  
parent cannot find the class does the child attempt to load it. This ensures  
that core Java classes are always loaded by the bootstrap loader, preventing  
malicious code from replacing critical system classes.  

### Runtime Data Areas  

HotSpot manages several memory regions during program execution:  

| Memory Area   | Shared? | Purpose                                           |
|---------------|---------|---------------------------------------------------|
| Heap          | Yes     | Stores all object instances                       |
| Metaspace     | Yes     | Stores class metadata, replaces PermGen           |
| Code Cache    | Yes     | Stores JIT-compiled native code                   |
| Thread Stacks | No      | Per-thread call stacks and local variables        |
| PC Registers  | No      | Per-thread program counter for bytecode execution |

### Execution Engine Components  

The execution engine transforms bytecode into machine code through multiple stages:  

**Interpreter**: Provides immediate execution of bytecode without compilation  
overhead. Essential for fast startup and rarely-executed code paths.  

**C1 Compiler (Client)**: A fast compiler that produces moderately optimized  
code quickly. Ideal for methods that need optimization sooner but may not  
benefit from expensive analysis.  

**C2 Compiler (Server)**: A highly optimizing compiler that produces the fastest  
code but takes longer to compile. Applied to "hot" methods that benefit from  
aggressive optimizations like inlining and loop unrolling.  

**Garbage Collector**: Automatically reclaims memory from objects that are no  
longer reachable. HotSpot offers multiple GC algorithms optimized for different  
workload characteristics.  


## Execution Model  

HotSpot uses an adaptive execution model that balances startup performance with  
peak throughput. When a Java application starts, methods are initially executed  
by the interpreter. As methods are called repeatedly, they become candidates for  
JIT compilation.  

### Bytecode Interpretation  

The interpreter executes Java bytecode instruction by instruction. While slower  
than compiled code, interpretation provides several benefits:  

- **Fast startup**: No compilation delay before execution begins  
- **Low memory footprint**: No need to store compiled code  
- **Accurate profiling**: Execution counters guide optimization decisions  
- **Debugging support**: Easier to maintain source-level debugging  

```java
void main() {

    // This method starts in the interpreter
    // Each bytecode instruction is decoded and executed
    int sum = 0;
    for (int i = 0; i < 10; i++) {
        sum += computeValue(i);
    }
    
    IO.println("Sum: " + sum);
}

int computeValue(int x) {
    // If called frequently, this becomes a "hot" method
    // and will be compiled by the JIT compiler
    return x * x + 2 * x + 1;
}
```

During interpretation, HotSpot maintains invocation counters for methods and  
back-edge counters for loops. When these counters exceed configured thresholds,  
the method becomes eligible for JIT compilation.  

### Tiered Compilation  

Modern HotSpot uses tiered compilation, which combines the fast compilation of  
C1 with the high-quality code of C2. Methods progress through compilation tiers  
based on their execution frequency and profiling data.  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        Tiered Compilation Levels                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Level 0: Interpreter                                                       │
│     │                                                                       │
│     │  Method invocation count exceeds threshold                            │
│     ▼                                                                       │
│  Level 1: C1 with Full Optimization (no profiling)                          │
│     │      - Used for trivial methods                                       │
│     │                                                                       │
│  Level 2: C1 with Limited Profiling                                         │
│     │      - Invocation and back-edge counters                              │
│     ▼                                                                       │
│  Level 3: C1 with Full Profiling                                            │
│     │      - Type profiling, branch profiling                               │
│     │      - Used to collect data for C2                                    │
│     ▼                                                                       │
│  Level 4: C2 with Full Optimization                                         │
│            - Maximum optimization based on profiling data                   │
│            - Speculative optimizations with deoptimization guards           │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

| Level | Compiler | Profiling  | Use Case                               |
|-------|----------|------------|----------------------------------------|
| 0     | None     | Basic      | Initial execution, cold code           |
| 1     | C1       | None       | Trivial methods, getters/setters       |
| 2     | C1       | Limited    | Limited profiling for quick warmup     |
| 3     | C1       | Full       | Collecting data for C2 compilation     |
| 4     | C2       | Uses L3    | Hot methods requiring maximum speed    |

### Adaptive Optimization  

HotSpot's adaptive optimization continuously monitors application behavior and  
adjusts compilation strategies accordingly. The JVM collects several types of  
profiling information:  

**Invocation Counts**: How often each method is called  
**Back-Edge Counts**: How many loop iterations execute  
**Type Profiles**: Actual types of objects at call sites  
**Branch Profiles**: Which branches are taken and how often  

```java
void main() {

    // Type profiling example
    List<String> items = new ArrayList<>();
    items.add("one");
    items.add("two");
    items.add("three");
    
    // HotSpot profiles that 'items' is always ArrayList
    // This enables virtual call devirtualization
    int totalLength = 0;
    for (var item : items) {
        totalLength += item.length();
    }
    
    IO.println("Total length: " + totalLength);
}
```

The JIT compiler uses profiling data to make speculative optimizations. If the  
runtime behavior changes and an optimization becomes invalid, HotSpot can  
**deoptimize**—reverting compiled code back to interpretation and recompiling  
with updated profiling information.  


## Just-In-Time (JIT) Compilation  

The JIT compiler is the heart of HotSpot's performance. It translates Java  
bytecode into highly optimized native machine code at runtime, achieving  
performance comparable to statically compiled languages.  

### Hot Method Detection  

HotSpot identifies "hot" methods through execution counters. Methods that exceed  
compilation thresholds are queued for background compilation. The thresholds are  
configurable via JVM flags:  

| Flag                          | Default | Description                     |
|-------------------------------|---------|----------------------------------|
| `-XX:CompileThreshold`        | 10000   | Invocations before C2 compile   |
| `-XX:Tier3InvocationThreshold`| 200     | Invocations before C1 L3        |
| `-XX:Tier4InvocationThreshold`| 5000    | Invocations before C2 from L3   |

### Key JIT Optimizations  

HotSpot's JIT compilers apply numerous optimizations to improve performance:  

**Inlining**: Replaces method calls with the method body, eliminating call  
overhead and enabling further optimizations.  

```java
void main() {

    long sum = 0;
    
    // Without inlining: method call overhead for each iteration
    // With inlining: getSquare code is inserted directly into loop
    for (int i = 0; i < 1000000; i++) {
        sum += getSquare(i);
    }
    
    IO.println("Sum of squares: " + sum);
}

// This small method is an ideal inlining candidate
int getSquare(int n) {
    return n * n;
}
```

**Escape Analysis**: Determines if objects are accessible outside their  
allocating scope. Non-escaping objects can be stack-allocated or eliminated  
entirely.  

```java
void main() {

    long total = 0;
    
    for (int i = 0; i < 1000000; i++) {
        // The Point object doesn't escape this method
        // HotSpot may eliminate the allocation entirely
        var point = new Point(i, i * 2);
        total += point.distance();
    }
    
    IO.println("Total distance: " + total);
}

record Point(int x, int y) {
    double distance() {
        return Math.sqrt(x * x + y * y);
    }
}
```

**Loop Unrolling**: Replicates loop bodies to reduce loop overhead and enable  
instruction-level parallelism.  

**Dead Code Elimination**: Removes code that has no effect on program output.  

**Constant Folding**: Evaluates constant expressions at compile time.  

**Bounds Check Elimination**: Removes redundant array bounds checks when the  
compiler can prove safety.  

**Null Check Elimination**: Removes null checks when the compiler can prove  
non-nullness.  

### Performance Monitoring  

Monitor JIT compilation activity with these flags:  

```
-XX:+PrintCompilation           # Log compilation events
-XX:+UnlockDiagnosticVMOptions  # Enable diagnostic options
-XX:+PrintInlining              # Show inlining decisions
-XX:+LogCompilation             # Generate detailed XML logs
```

```java
void main() {

    // Run with -XX:+PrintCompilation to see compilation output
    // Methods will show compilation tier and timing
    
    var startTime = System.nanoTime();
    
    long result = 0;
    for (int i = 0; i < 10_000_000; i++) {
        result += fibonacci(20);
    }
    
    var endTime = System.nanoTime();
    var durationMs = (endTime - startTime) / 1_000_000;
    
    IO.println("Result: " + result);
    IO.println("Duration: " + durationMs + " ms");
}

int fibonacci(int n) {
    if (n <= 1) return n;
    return fibonacci(n - 1) + fibonacci(n - 2);
}
```

After repeated invocations, the `fibonacci` method will be compiled by C2,  
resulting in significantly faster execution compared to interpretation.  


## Garbage Collection in HotSpot  

Garbage collection (GC) is the process of automatically reclaiming memory  
occupied by objects that are no longer reachable by the application. HotSpot  
provides multiple garbage collectors, each optimized for different workload  
characteristics.  

### Generational Hypothesis  

HotSpot's garbage collectors are based on the generational hypothesis:  

- Most objects die young (are short-lived)  
- Few references from old objects to young objects exist  

This observation enables efficient collection by focusing on the young  
generation, where most garbage resides.  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          Heap Memory Structure                              │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌───────────────────────────────────┐  ┌───────────────────────────────┐   │
│  │          Young Generation         │  │        Old Generation         │   │
│  │  ┌───────┐ ┌───────┐ ┌───────┐   │  │                               │   │
│  │  │ Eden  │ │  S0   │ │  S1   │   │  │    Tenured/Old Objects        │   │
│  │  │       │ │       │ │       │   │  │                               │   │
│  │  │ New   │ │Survivor│Survivor│   │  │  (Long-lived objects that     │   │
│  │  │Objects│ │ From  │ │  To   │   │  │   survived multiple GCs)      │   │
│  │  └───────┘ └───────┘ └───────┘   │  │                               │   │
│  └───────────────────────────────────┘  └───────────────────────────────┘   │
│                                                                             │
│  Object lifecycle:                                                          │
│  1. New objects allocated in Eden                                           │
│  2. Surviving objects move to Survivor spaces                               │
│  3. After aging, objects promote to Old generation                          │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Available Garbage Collectors  

| Collector    | Young GC | Old GC  | Pause Goal        | Best For            |
|--------------|----------|---------|-------------------|---------------------|
| Serial GC    | Serial   | Serial  | None              | Small apps, testing |
| Parallel GC  | Parallel | Parallel| Throughput        | Batch processing    |
| G1 GC        | Parallel | Concurrent| Predictable     | General purpose     |
| ZGC          | Concurrent| Concurrent| Sub-millisecond| Low latency, large heaps|
| Shenandoah   | Concurrent| Concurrent| Low latency    | Low latency         |

**Serial GC** (`-XX:+UseSerialGC`): Single-threaded collector suitable for  
single-processor machines or applications with small heaps and minimal pause  
time requirements.  

**Parallel GC** (`-XX:+UseParallelGC`): Multi-threaded collector optimized for  
throughput. Performs stop-the-world collections using multiple threads.  

**G1 GC** (`-XX:+UseG1GC`): The default collector since Java 9. Divides the  
heap into regions and collects garbage incrementally to meet pause time targets.  

**ZGC** (`-XX:+UseZGC`): Scalable, low-latency collector with pause times under  
10 milliseconds regardless of heap size. Ideal for large heaps and latency-  
sensitive applications.  

**Shenandoah** (`-XX:+UseShenandoahGC`): Similar to ZGC with concurrent  
compaction. Available in OpenJDK but not Oracle JDK.  

### GC Selection Guidelines  

```java
void main() {

    // Demonstrate memory allocation patterns
    var objects = new ArrayList<byte[]>();
    
    IO.println("Allocating objects...");
    
    for (int i = 0; i < 100; i++) {
        // Allocate 1 MB chunks
        objects.add(new byte[1024 * 1024]);
        
        // Occasionally release some to trigger GC
        if (i % 20 == 0 && i > 0) {
            objects.subList(0, 10).clear();
        }
    }
    
    IO.println("Allocated " + objects.size() + " MB worth of objects");
    
    // Force GC for observation (don't do this in production)
    System.gc();
    
    Runtime runtime = Runtime.getRuntime();
    long usedMemory = runtime.totalMemory() - runtime.freeMemory();
    IO.println("Used memory: " + (usedMemory / 1024 / 1024) + " MB");
}
```

| Requirement                    | Recommended GC       | Key Flags              |
|--------------------------------|----------------------|------------------------|
| Maximum throughput             | Parallel GC          | `-XX:+UseParallelGC`   |
| Predictable pause times < 200ms| G1 GC                | `-XX:MaxGCPauseMillis` |
| Sub-millisecond pauses         | ZGC                  | `-XX:+UseZGC`          |
| Very large heap (TB+)          | ZGC                  | `-XX:+UseZGC`          |
| Single processor               | Serial GC            | `-XX:+UseSerialGC`     |

### GC Tuning Options  

Common flags for tuning garbage collection:  

```
# G1 GC tuning
-XX:MaxGCPauseMillis=200          # Target maximum pause time
-XX:G1HeapRegionSize=16m          # Region size (1-32 MB, power of 2)
-XX:InitiatingHeapOccupancyPercent=45  # When to start concurrent GC

# ZGC tuning
-XX:ZCollectionInterval=5         # Force GC every 5 seconds (for testing)
-XX:SoftMaxHeapSize=4g            # Soft limit for heap size

# General GC logging
-Xlog:gc*:file=gc.log:time,level,tags  # Modern unified logging
```


## Memory Management  

HotSpot manages memory across several distinct regions, each serving a specific  
purpose in the execution of Java applications.  

### Heap Structure  

The heap is where all Java objects live. Its structure depends on the garbage  
collector but typically includes generational divisions.  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      JVM Memory Layout (Non-Heap + Heap)                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                           Heap (-Xmx, -Xms)                         │    │
│  │  ┌─────────────────────────┐  ┌─────────────────────────────────┐   │    │
│  │  │    Young Generation     │  │         Old Generation          │   │    │
│  │  │        (-Xmn)           │  │                                 │   │    │
│  │  │  ┌─────┬───────┬─────┐  │  │    Long-lived objects that     │   │    │
│  │  │  │Eden │  S0   │ S1  │  │  │    survived multiple minor GCs  │   │    │
│  │  │  └─────┴───────┴─────┘  │  │                                 │   │    │
│  │  └─────────────────────────┘  └─────────────────────────────────┘   │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                    Metaspace (native memory)                        │    │
│  │   - Class metadata           - Method bytecode                      │    │
│  │   - Constant pools           - Annotations                          │    │
│  │   - Controlled by: -XX:MaxMetaspaceSize                             │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                         Code Cache                                  │    │
│  │   - JIT compiled code        - Native method stubs                  │    │
│  │   - Controlled by: -XX:ReservedCodeCacheSize                        │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                    Thread Stacks (per thread)                       │    │
│  │   - Local variables          - Method call frames                   │    │
│  │   - Controlled by: -Xss (stack size per thread)                     │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                    Native Memory (Direct Buffers)                   │    │
│  │   - NIO direct buffers       - JNI allocations                      │    │
│  │   - Controlled by: -XX:MaxDirectMemorySize                          │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Memory Configuration Flags  

| Flag                        | Purpose                                     |
|-----------------------------|---------------------------------------------|
| `-Xms<size>`                | Initial heap size                           |
| `-Xmx<size>`                | Maximum heap size                           |
| `-Xmn<size>`                | Young generation size                       |
| `-Xss<size>`                | Thread stack size                           |
| `-XX:MaxMetaspaceSize`      | Maximum metaspace size                      |
| `-XX:ReservedCodeCacheSize` | Maximum code cache size                     |
| `-XX:MaxDirectMemorySize`   | Maximum direct buffer memory                |

### Monitoring Memory Usage  

```java
void main() {

    Runtime runtime = Runtime.getRuntime();
    
    // Heap memory
    long maxMemory = runtime.maxMemory();
    long totalMemory = runtime.totalMemory();
    long freeMemory = runtime.freeMemory();
    long usedMemory = totalMemory - freeMemory;
    
    IO.println("=== Heap Memory ===");
    IO.println("Max:   " + formatBytes(maxMemory));
    IO.println("Total: " + formatBytes(totalMemory));
    IO.println("Used:  " + formatBytes(usedMemory));
    IO.println("Free:  " + formatBytes(freeMemory));
    
    // Processors
    IO.println("\n=== System ===");
    IO.println("Available processors: " + runtime.availableProcessors());
}

String formatBytes(long bytes) {
    if (bytes < 1024) return bytes + " B";
    if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
    if (bytes < 1024 * 1024 * 1024) return (bytes / 1024 / 1024) + " MB";
    return (bytes / 1024 / 1024 / 1024) + " GB";
}
```

For detailed memory analysis, use the MemoryMXBean:  

```java
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

void main() {

    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    
    MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
    MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();
    
    IO.println("=== Heap Memory ===");
    printMemoryUsage(heapUsage);
    
    IO.println("\n=== Non-Heap Memory ===");
    printMemoryUsage(nonHeapUsage);
}

void printMemoryUsage(MemoryUsage usage) {
    IO.println("Init:      " + formatBytes(usage.getInit()));
    IO.println("Used:      " + formatBytes(usage.getUsed()));
    IO.println("Committed: " + formatBytes(usage.getCommitted()));
    IO.println("Max:       " + formatBytes(usage.getMax()));
}

String formatBytes(long bytes) {
    if (bytes < 0) return "undefined";
    if (bytes < 1024) return bytes + " B";
    if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
    return (bytes / 1024 / 1024) + " MB";
}
```


## Performance Tuning  

Performance tuning in HotSpot involves configuring JVM flags, monitoring  
runtime behavior, and iteratively adjusting settings based on observations.  
Effective tuning requires understanding the application's workload  
characteristics and performance goals.  

### Essential JVM Flags  

**Heap Sizing**:  
```
-Xms4g                           # Initial heap size
-Xmx4g                           # Maximum heap size (set equal to -Xms for production)
-Xmn1g                           # Young generation size
```

**GC Selection and Tuning**:  
```
-XX:+UseG1GC                     # Use G1 garbage collector
-XX:MaxGCPauseMillis=200         # Target maximum pause time
-XX:G1HeapRegionSize=16m         # G1 region size
```

**JIT Compilation**:  
```
-XX:+TieredCompilation           # Enable tiered compilation (default)
-XX:TieredStopAtLevel=1          # Stop at C1 for faster startup
-XX:CompileThreshold=5000        # Lower threshold for faster warmup
```

**Diagnostic Flags**:  
```
-XX:+HeapDumpOnOutOfMemoryError  # Generate heap dump on OOM
-XX:HeapDumpPath=/path/dump.hprof # Heap dump location
-XX:+PrintFlagsFinal             # Print all JVM flags
```

### Profiling Tools  

HotSpot includes several tools for performance analysis:  

**Java Flight Recorder (JFR)**: A low-overhead profiling tool built into  
the JVM. Collects detailed runtime information with minimal performance impact.  

```
# Enable JFR from command line
-XX:StartFlightRecording=duration=60s,filename=recording.jfr

# Enable JFR programmatically
jcmd <pid> JFR.start duration=60s filename=recording.jfr
```

**JDK Mission Control**: GUI application for analyzing JFR recordings.  
Visualizes CPU usage, memory allocation, thread activity, and more.  

**VisualVM**: Standalone profiler that can connect to running JVMs.  
Provides CPU and memory profiling, thread analysis, and heap dumps.  

**Async Profiler**: Third-party profiler with low overhead flame graph  
generation. Excellent for production profiling.  

```java
import java.lang.management.ManagementFactory;
import java.lang.management.GarbageCollectorMXBean;

void main() {

    IO.println("=== GC Statistics ===");
    
    for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
        IO.println("Collector: " + gc.getName());
        IO.println("  Collection count: " + gc.getCollectionCount());
        IO.println("  Collection time:  " + gc.getCollectionTime() + " ms");
        IO.println("  Memory pools:     " + String.join(", ", gc.getMemoryPoolNames()));
        IO.println();
    }
}
```

### Tuning Workflow  

1. **Establish Baseline**: Measure current performance with realistic workload  
2. **Identify Bottleneck**: Use profiling tools to find the constraint  
3. **Make One Change**: Modify a single setting at a time  
4. **Measure Impact**: Compare against baseline with same workload  
5. **Iterate**: Repeat until performance goals are met  

| Symptom                    | Likely Cause           | Tuning Action               |
|----------------------------|------------------------|------------------------------|
| Long GC pauses             | Heap too small/large   | Adjust -Xmx, switch GC       |
| Frequent minor GCs         | Young gen too small    | Increase -Xmn                |
| Full GCs                   | Old gen fragmentation  | Use G1 or ZGC                |
| Slow startup               | JIT warmup             | Lower CompileThreshold       |
| High CPU in GC             | Inefficient collection | Use Parallel or G1 GC        |


## Advanced Features  

HotSpot continues to evolve with new features that enhance performance,  
scalability, and developer productivity.  

### Tiered Compilation Deep Dive  

Tiered compilation was introduced in Java 7 and became the default in Java 8.  
It combines the benefits of fast C1 compilation with the optimizing power of  
C2, providing both quick warmup and peak performance.  

The compilation policy considers:  
- Method invocation frequency  
- Loop iteration counts  
- Compilation queue length  
- Available compiler threads  

```java
void main() {

    IO.println("=== Compilation Tier Demo ===");
    
    // Cold method - interpreted initially
    int result = calculate(5);
    IO.println("First call (interpreted): " + result);
    
    // Warming up - triggers C1 compilation
    for (int i = 0; i < 10000; i++) {
        result = calculate(i % 100);
    }
    IO.println("After warmup (C1 compiled): " + result);
    
    // Hot path - triggers C2 compilation
    long total = 0;
    for (int i = 0; i < 1000000; i++) {
        total += calculate(i % 100);
    }
    IO.println("After hot loop (C2 compiled): total = " + total);
}

int calculate(int n) {
    // Simple method that benefits from inlining and optimization
    int sum = 0;
    for (int i = 0; i <= n; i++) {
        sum += i;
    }
    return sum;
}
```

### GraalVM Integration  

GraalVM is an advanced polyglot virtual machine that includes the Graal  
compiler—a modern, highly optimizing JIT compiler written in Java. Graal can  
be used as a drop-in replacement for C2 in HotSpot.  

```
# Use Graal as the JIT compiler (requires GraalVM or JDK with JVMCI)
-XX:+UnlockExperimentalVMOptions
-XX:+EnableJVMCI
-XX:+UseJVMCICompiler
```

Graal provides several advantages:  
- Written in Java for easier maintenance and extension  
- Advanced optimizations for dynamic languages  
- Native image compilation (ahead-of-time)  
- Better support for partial escape analysis  

### Project Loom (Virtual Threads)  

Project Loom introduces virtual threads—lightweight threads managed by the  
JVM rather than the operating system. Virtual threads enable scalable  
concurrent applications with the simple thread-per-request model.  

```java
void main() throws InterruptedException {

    IO.println("=== Virtual Threads Demo ===");
    
    var startTime = System.currentTimeMillis();
    
    // Create 100,000 virtual threads
    var threads = new ArrayList<Thread>();
    for (int i = 0; i < 100_000; i++) {
        int taskId = i;
        Thread vt = Thread.ofVirtual().start(() -> {
            // Simulate blocking I/O
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        threads.add(vt);
    }
    
    // Wait for all virtual threads to complete
    for (Thread t : threads) {
        t.join();
    }
    
    var duration = System.currentTimeMillis() - startTime;
    IO.println("Completed 100,000 virtual threads in " + duration + " ms");
}
```

Virtual threads are designed for high-throughput I/O-bound workloads. They  
efficiently handle blocking operations by unmounting from platform threads,  
allowing a small number of platform threads to execute millions of virtual  
threads.  

### Project Valhalla (Value Objects)  

Project Valhalla introduces value objects—objects that are defined by their  
data rather than their identity. Value objects can be flattened into arrays  
and stored inline, eliminating object header overhead and improving cache  
locality.  

Value objects will eventually provide:  
- Primitive-like performance for user-defined types  
- Specialized generics (no more boxing for primitives)  
- Better memory layout for data-intensive applications  

### Compact Object Headers  

Recent JDK versions include experimental support for compact object headers,  
reducing the per-object memory overhead from 12-16 bytes to 8 bytes.  

```
-XX:+UnlockExperimentalVMOptions
-XX:+UseCompactObjectHeaders
```


## Common Pitfalls  

Understanding common mistakes helps developers avoid performance problems and  
configuration issues.  

### Over-Tuning JVM Flags  

One of the most common mistakes is excessive tuning based on assumptions rather  
than measurements.  

**Problem**: Setting too many JVM flags without understanding their interactions  
can lead to worse performance or unexpected behavior.  

**Solution**: Start with defaults, measure performance, and tune only based on  
observed bottlenecks. Modern HotSpot versions have excellent ergonomics that  
automatically adjust many settings.  

```java
void main() {

    // Bad: Assuming more threads is always better
    // -XX:ParallelGCThreads=64 (on an 8-core machine)
    
    // Good: Let HotSpot choose based on available processors
    int processors = Runtime.getRuntime().availableProcessors();
    IO.println("Available processors: " + processors);
    IO.println("HotSpot will configure GC threads appropriately");
    
    // Only override defaults when measurements show improvement
}
```

### Ignoring Application-Level Bottlenecks  

**Problem**: Focusing on JVM tuning when the real issue is in application code,  
database queries, or external service calls.  

**Solution**: Profile the application first. Often, optimizing algorithms or  
reducing I/O has far greater impact than JVM tuning.  

```java
void main() {

    // Example: Inefficient string concatenation in a loop
    // No amount of JVM tuning will fix this O(n²) algorithm
    
    String result = "";
    for (int i = 0; i < 10000; i++) {
        result += i + ",";  // Creates new String each iteration
    }
    
    // Better: Use StringBuilder (O(n))
    var sb = new StringBuilder();
    for (int i = 0; i < 10000; i++) {
        sb.append(i).append(",");
    }
    result = sb.toString();
    
    IO.println("Length: " + result.length());
}
```

### Misunderstanding GC Logs  

**Problem**: Misinterpreting garbage collection logs leads to incorrect  
tuning decisions.  

**Solution**: Learn to read GC logs properly. Understand the difference between  
minor GC, major GC, and full GC. Know what metrics indicate problems.  

```
# Enable comprehensive GC logging
-Xlog:gc*:file=gc.log:time,level,tags:filecount=5,filesize=10m
```

Key GC log metrics to monitor:  

| Metric                | Healthy Range    | Problem Indicator              |
|-----------------------|------------------|--------------------------------|
| Minor GC pause        | < 50ms           | > 200ms regularly              |
| Full GC frequency     | Rare             | Multiple per minute            |
| GC throughput         | > 95%            | < 90%                          |
| Heap after GC         | < 70% of max     | > 90% consistently             |

### Setting -Xms Different from -Xmx  

**Problem**: Setting different values for initial and maximum heap causes  
the JVM to resize the heap during operation, triggering garbage collections.  

**Solution**: In production, set `-Xms` equal to `-Xmx` to avoid heap resizing.  

```
# Production configuration - avoid heap resizing
-Xms4g -Xmx4g

# Development configuration - OK to let heap grow
-Xms256m -Xmx2g
```


## Best Practices  

Following these guidelines helps achieve optimal HotSpot performance while  
maintaining system stability.  

### Monitoring Guidelines  

1. **Establish Baseline Metrics**: Before any tuning, capture normal operation  
   metrics including throughput, latency, GC frequency, and memory usage.  

2. **Use Proper Tools**: JFR for detailed profiling, GC logs for collection  
   analysis, and metrics systems for long-term trending.  

3. **Monitor in Production**: Performance characteristics differ between  
   development and production. Monitor continuously.  

```java
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

void main() {

    ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
    
    IO.println("=== Thread Monitoring ===");
    IO.println("Thread count:        " + threadBean.getThreadCount());
    IO.println("Peak thread count:   " + threadBean.getPeakThreadCount());
    IO.println("Daemon thread count: " + threadBean.getDaemonThreadCount());
    
    // Check for deadlocks
    long[] deadlockedThreads = threadBean.findDeadlockedThreads();
    if (deadlockedThreads == null) {
        IO.println("No deadlocks detected");
    } else {
        IO.println("DEADLOCK DETECTED: " + deadlockedThreads.length + " threads");
    }
}
```

### When to Tune vs Accept Defaults  

| Scenario                         | Recommendation                           |
|----------------------------------|------------------------------------------|
| Standard web application         | Accept G1 GC defaults                    |
| Sub-millisecond latency required | Switch to ZGC, tune carefully            |
| Batch processing                 | Consider Parallel GC for throughput      |
| Memory constrained environment   | Tune heap sizes, consider Serial GC      |
| Microservices with fast startup  | Consider GraalVM native image            |

### Production Deployment Checklist  

1. **Memory Configuration**  
   ```
   -Xms4g -Xmx4g                     # Equal heap bounds
   -XX:MaxMetaspaceSize=256m         # Bound metaspace
   -XX:+HeapDumpOnOutOfMemoryError   # Capture OOM heap dump
   ```

2. **GC Configuration**  
   ```
   -XX:+UseG1GC                      # Default collector
   -XX:MaxGCPauseMillis=200          # Pause time target
   -Xlog:gc*:file=gc.log:time        # Enable GC logging
   ```

3. **Diagnostic Preparation**  
   ```
   -XX:+UnlockDiagnosticVMOptions    # Enable diagnostics
   -XX:+DebugNonSafepoints           # Better profiling accuracy
   -XX:ErrorFile=/path/hs_err.log    # Error log location
   ```

### Testing Under Realistic Workloads  

Performance tuning is only valid when tested under production-like conditions:  

- Use production data volumes  
- Simulate realistic request patterns  
- Include warmup period before measurements  
- Run tests for extended periods  
- Test failure scenarios (high memory, high CPU)  

```java
void main() throws InterruptedException {

    IO.println("=== Warmup and Measurement Demo ===");
    
    // Warmup phase - trigger JIT compilation
    IO.println("Warming up...");
    for (int i = 0; i < 100_000; i++) {
        doWork();
    }
    
    // Measurement phase
    IO.println("Measuring...");
    long startTime = System.nanoTime();
    int iterations = 1_000_000;
    
    for (int i = 0; i < iterations; i++) {
        doWork();
    }
    
    long endTime = System.nanoTime();
    double avgNanos = (endTime - startTime) / (double) iterations;
    
    IO.println("Average time per operation: " + String.format("%.2f", avgNanos) + " ns");
    IO.println("Operations per second: " + String.format("%.0f", 1_000_000_000.0 / avgNanos));
}

int doWork() {
    // Simulated work
    int result = 0;
    for (int i = 0; i < 100; i++) {
        result += i * i;
    }
    return result;
}
```


## Conclusion  

The HotSpot JVM is a sophisticated runtime environment that has enabled Java  
to achieve excellent performance across a wide range of applications. Its  
adaptive optimization approach, combining interpretation with tiered JIT  
compilation, provides both fast startup and peak throughput.  

### Key Takeaways  

| Aspect                | Summary                                              |
|-----------------------|------------------------------------------------------|
| Architecture          | Class loading, runtime data areas, execution engine  |
| Execution Model       | Interpreter + tiered JIT compilation (C1, C2)        |
| JIT Compilation       | Hot method detection, inlining, escape analysis      |
| Garbage Collection    | Generational collectors: G1 (default), ZGC, etc.     |
| Memory Management     | Heap, metaspace, code cache, thread stacks           |
| Performance Tuning    | Profile first, tune based on measurements            |
| Advanced Features     | Virtual threads, Graal integration, compact headers  |

Understanding HotSpot internals enables developers to:  

- **Write Efficient Code**: Understanding JIT optimizations helps write code  
  that compiles well, avoiding patterns that prevent optimization.  

- **Configure for Production**: Knowledge of GC algorithms and memory layout  
  enables appropriate configuration for specific workload requirements.  

- **Diagnose Performance Issues**: Familiarity with profiling tools and  
  runtime behavior helps identify and resolve performance bottlenecks.  

- **Plan for Scalability**: Understanding resource usage patterns helps design  
  systems that scale efficiently with load.  

HotSpot continues to evolve with each JDK release, incorporating new features  
and optimizations. Staying current with developments—such as virtual threads,  
value objects, and improved garbage collectors—ensures that applications can  
take advantage of the latest performance improvements.  

The best approach to HotSpot performance is to start with defaults, measure  
under realistic conditions, and tune only when measurements indicate specific  
bottlenecks. Modern HotSpot ergonomics handle most situations well, and  
over-tuning often causes more problems than it solves.  
