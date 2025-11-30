# Garbage Collection in Java  

Garbage Collection (GC) is an automatic memory management mechanism in Java that  
identifies and reclaims memory occupied by objects that are no longer in use. This  
frees developers from manual memory management, reducing memory leaks and dangling  
pointer bugs that plague languages like C and C++. Understanding GC algorithms is  
essential for building high-performance Java applications.  

The Java Virtual Machine (JVM) has evolved significantly since its inception,  
introducing increasingly sophisticated garbage collectors to meet the demands of  
modern applications. From the original Serial collector to today's ultra-low-latency  
collectors like ZGC and Shenandoah, each generation has brought improvements in  
throughput, pause times, and scalability.  

This document explores the major garbage collection algorithms available in modern  
Java: G1 (the default), ZGC, Shenandoah, and Epsilon. Each collector is designed  
for specific use cases, and choosing the right one can dramatically impact  
application performance.  


## Overview of Garbage Collection  

Garbage collection automates the process of reclaiming memory from objects that  
are no longer reachable by the application. The JVM divides the heap into regions  
and uses various strategies to identify and collect garbage efficiently.  


### Heap Structure  

The JVM heap is traditionally divided into generations based on object lifetimes:  

| Region       | Description                                                     |
|--------------|-----------------------------------------------------------------|
| Young Gen    | Newly allocated objects; most objects die here (short-lived)   |
| Eden Space   | Initial allocation area within Young Generation                |
| Survivor     | Objects that survived one or more Young GC cycles              |
| Old Gen      | Long-lived objects promoted from Young Generation              |
| Metaspace    | Class metadata and method information (not in heap)            |

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                               JVM Heap                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────────────────────────┐  ┌─────────────────────────────────┐  │
│  │        Young Generation         │  │         Old Generation          │  │
│  │  ┌───────────────────────────┐  │  │                                 │  │
│  │  │        Eden Space         │  │  │   Long-lived objects that       │  │
│  │  │   (New allocations)       │  │  │   survived multiple GC cycles   │  │
│  │  └───────────────────────────┘  │  │                                 │  │
│  │  ┌────────────┐ ┌────────────┐  │  │                                 │  │
│  │  │ Survivor 0 │ │ Survivor 1 │  │  │                                 │  │
│  │  │   (From)   │ │   (To)     │  │  │                                 │  │
│  │  └────────────┘ └────────────┘  │  │                                 │  │
│  └─────────────────────────────────┘  └─────────────────────────────────┘  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

Modern collectors like ZGC and Shenandoah may use different heap layouts,  
organizing memory into uniformly-sized regions rather than generations.  


### Object Lifecycle  

Objects go through a predictable lifecycle from allocation to collection:  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          Object Lifecycle                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   ┌──────────┐     ┌──────────┐     ┌──────────┐     ┌──────────────────┐  │
│   │  Object  │     │  Object  │     │  Object  │     │    Object is     │  │
│   │ Created  │────►│  In Use  │────►│ Becomes  │────►│    Collected     │  │
│   │ in Eden  │     │ (Reach-  │     │ Unreach- │     │  (Memory freed)  │  │
│   │          │     │   able)  │     │   able   │     │                  │  │
│   └──────────┘     └──────────┘     └──────────┘     └──────────────────┘  │
│        │                                                                    │
│        │ If survives GC                                                     │
│        ▼                                                                    │
│   ┌──────────┐     ┌──────────┐                                            │
│   │ Promoted │     │ Promoted │                                            │
│   │    to    │────►│    to    │                                            │
│   │ Survivor │     │ Old Gen  │                                            │
│   └──────────┘     └──────────┘                                            │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```


### GC Phases  

Most garbage collectors perform these fundamental phases:  

| Phase        | Description                                                    |
|--------------|----------------------------------------------------------------|
| Marking      | Identify all reachable (live) objects starting from GC roots  |
| Sweeping     | Identify unreachable objects as garbage                        |
| Compacting   | Move live objects together to eliminate fragmentation          |
| Copying      | Copy live objects to a new region (alternative to compacting)  |

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           GC Phases Overview                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  1. MARKING PHASE                                                           │
│     ┌─────────────────────────────────────────────────────────────────┐    │
│     │  GC Roots ──► Live Object ──► Live Object ──► Live Object       │    │
│     │      │                             │                             │    │
│     │      ▼                             ▼                             │    │
│     │  Live Object                   Live Object     [Dead] [Dead]    │    │
│     └─────────────────────────────────────────────────────────────────┘    │
│                                                                             │
│  2. SWEEPING PHASE                                                          │
│     ┌─────────────────────────────────────────────────────────────────┐    │
│     │  [Live] [    Free    ] [Live] [Live] [     Free      ] [Live]   │    │
│     │         Dead objects reclaimed, memory fragmented               │    │
│     └─────────────────────────────────────────────────────────────────┘    │
│                                                                             │
│  3. COMPACTING PHASE                                                        │
│     ┌─────────────────────────────────────────────────────────────────┐    │
│     │  [Live][Live][Live][Live][          Free Space          ]       │    │
│     │         Objects moved together, eliminating fragmentation       │    │
│     └─────────────────────────────────────────────────────────────────┘    │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```


### Stop-the-World Pauses  

A **stop-the-world (STW)** pause occurs when the GC must pause all application  
threads to safely perform certain operations. Reducing these pauses is a primary  
goal of modern collectors.  

| Pause Type     | Description                                               |
|----------------|-----------------------------------------------------------|
| Minor GC       | Collection of Young Generation; typically short           |
| Major GC       | Collection of Old Generation; often longer                |
| Full GC        | Collection of entire heap; longest pause                  |
| Concurrent GC  | GC work done while application runs (no pause)            |

Modern collectors like ZGC and Shenandoah perform most work concurrently,  
achieving sub-millisecond pause times regardless of heap size.  


### Concurrent vs Parallel Collection  

Understanding the difference between concurrent and parallel collection is crucial:  

| Term        | Description                                                    |
|-------------|----------------------------------------------------------------|
| Parallel    | Multiple GC threads work together during STW pause             |
| Concurrent  | GC threads work while application threads continue running     |
| Incremental | GC work is divided into smaller chunks across multiple pauses  |


## G1 Garbage Collector  

The Garbage First (G1) collector is the default garbage collector in Java 9+.  
It is designed to provide high throughput with predictable pause times, making  
it suitable for most applications. G1 balances the needs of large heaps with  
the requirement for reasonable latency.  


### Region-Based Heap Management  

G1 divides the heap into equally-sized regions (typically 1-32 MB each) rather  
than contiguous generations. Each region can be Eden, Survivor, Old, or Humongous  
(for large objects).  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         G1 Region-Based Heap                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐          │
│  │  E  │ │  E  │ │  S  │ │  O  │ │  O  │ │  O  │ │  E  │ │  H  │          │
│  └─────┘ └─────┘ └─────┘ └─────┘ └─────┘ └─────┘ └─────┘ └─────┘          │
│  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐          │
│  │  O  │ │  E  │ │  O  │ │  F  │ │  F  │ │  O  │ │  S  │ │  H  │          │
│  └─────┘ └─────┘ └─────┘ └─────┘ └─────┘ └─────┘ └─────┘ └─────┘          │
│  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐          │
│  │  O  │ │  F  │ │  O  │ │  O  │ │  E  │ │  O  │ │  F  │ │  O  │          │
│  └─────┘ └─────┘ └─────┘ └─────┘ └─────┘ └─────┘ └─────┘ └─────┘          │
│                                                                             │
│  Legend: E = Eden, S = Survivor, O = Old, H = Humongous, F = Free          │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```


### How G1 Works  

G1 operates in several phases to manage memory efficiently:  

| Phase                    | Description                                         |
|--------------------------|-----------------------------------------------------|
| Young GC                 | Collects Eden and Survivor regions                  |
| Concurrent Marking       | Marks live objects while application runs           |
| Mixed GC                 | Collects Young regions plus selected Old regions    |
| Full GC                  | Fallback collection of entire heap (avoided if ok)  |

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           G1 GC Cycle                                       │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   ┌─────────────┐     ┌─────────────────────┐     ┌─────────────────────┐  │
│   │  Young GC   │────►│ Concurrent Marking  │────►│      Mixed GC       │  │
│   │   (STW)     │     │   (Concurrent)      │     │       (STW)         │  │
│   └─────────────┘     └─────────────────────┘     └─────────────────────┘  │
│         │                                                   │               │
│         │                                                   │               │
│         └───────────────────────────────────────────────────┘               │
│                           (Cycle repeats)                                   │
│                                                                             │
│   Concurrent Marking Phases:                                                │
│   1. Initial Mark (STW) - Mark objects directly reachable from GC roots    │
│   2. Root Region Scan   - Scan survivor regions for references to old gen  │
│   3. Concurrent Mark    - Mark live objects throughout the heap            │
│   4. Remark (STW)       - Complete marking of remaining live objects       │
│   5. Cleanup (STW/Conc) - Identify completely empty regions to reclaim     │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```


### G1 Advantages  

G1 provides several key benefits:  

- **Predictable pause times**: Configurable pause time targets  
- **Region-based collection**: Only collects regions with most garbage first  
- **Compaction**: Reduces fragmentation during evacuation  
- **Scalability**: Handles heaps from gigabytes to terabytes  
- **Adaptive**: Automatically adjusts to meet pause time goals  


### G1 JVM Tuning Flags  

Common JVM flags for configuring G1:  

```bash
# Enable G1 (default in Java 9+)
-XX:+UseG1GC

# Set maximum pause time target (milliseconds)
-XX:MaxGCPauseMillis=200

# Set heap region size (1-32 MB, must be power of 2)
-XX:G1HeapRegionSize=16m

# Set percentage of heap for Old Generation threshold
-XX:InitiatingHeapOccupancyPercent=45

# Number of parallel GC threads
-XX:ParallelGCThreads=8

# Number of concurrent marking threads
-XX:ConcGCThreads=4

# Reserve memory for promotion failures
-XX:G1ReservePercent=10

# Enable string deduplication
-XX:+UseStringDeduplication
```

Example complete configuration for a web server:  

```bash
java -Xms4g -Xmx4g \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=100 \
     -XX:G1HeapRegionSize=8m \
     -XX:InitiatingHeapOccupancyPercent=35 \
     -XX:+UseStringDeduplication \
     -jar application.jar
```


### Monitoring G1 with simple output  

You can observe G1 behavior by enabling GC logging:  

```java
void main() {

    // Allocate objects to trigger GC
    var list = new ArrayList<byte[]>();
    
    for (int i = 0; i < 100; i++) {
        // Allocate 1 MB blocks
        list.add(new byte[1024 * 1024]);
        
        if (i % 10 == 0) {
            // Release some references to create garbage
            list.subList(0, Math.min(5, list.size())).clear();
        }
    }
    
    // Force garbage collection
    System.gc();
    
    var runtime = Runtime.getRuntime();
    long usedMemory = runtime.totalMemory() - runtime.freeMemory();
    long maxMemory = runtime.maxMemory();
    
    IO.println("Used memory: " + usedMemory / (1024 * 1024) + " MB");
    IO.println("Max memory: " + maxMemory / (1024 * 1024) + " MB");
}
```

Run with GC logging enabled:  

```bash
java --enable-preview --source 25 \
     -XX:+UseG1GC \
     -Xlog:gc*:file=gc.log:time,uptime:filecount=5,filesize=10m \
     GCDemo.java
```

This produces detailed logs showing Young GC, concurrent marking phases, and  
mixed GC events with timing information.  


## ZGC (Z Garbage Collector)  

ZGC is an ultra-low-latency garbage collector designed to keep pause times under  
1 millisecond regardless of heap size. It can handle heaps ranging from megabytes  
to multi-terabytes while maintaining consistent sub-millisecond pauses.  


### ZGC Design Goals  

ZGC was designed with specific goals in mind:  

| Goal                    | Description                                         |
|-------------------------|-----------------------------------------------------|
| Ultra-low latency       | Pause times < 1 ms regardless of heap size          |
| Scalability             | Support heaps from 8 MB to 16 TB                    |
| Concurrent              | Almost all work done concurrently with application  |
| No tuning required      | Minimal configuration needed                        |


### Colored Pointers  

ZGC uses **colored pointers** to store metadata directly in object references.  
This innovative approach allows ZGC to determine object state without accessing  
the object itself.  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        ZGC Colored Pointer (64-bit)                         │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Bit Layout (Linux/x64):                                                    │
│  ┌────────┬────────┬────────┬────────┬──────────────────────────────────┐  │
│  │ Unused │ Final- │ Remap  │ Mark 1 │         Object Address           │  │
│  │(16 bit)│ izable │        │ Mark 0 │           (42 bits)              │  │
│  │        │(1 bit) │(1 bit) │(2 bits)│                                  │  │
│  └────────┴────────┴────────┴────────┴──────────────────────────────────┘  │
│                                                                             │
│  Metadata bits (4 bits total):                                              │
│  - Finalizable: Object has a finalizer                                      │
│  - Remapped:    Object has been relocated                                   │
│  - Marked:      Object is reachable (two mark bits for alternating cycles) │
│                                                                             │
│  The 42-bit address supports up to 4 TB of heap address space              │
│  (16 TB with pointer compression disabled)                                 │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```


### Load Barriers  

ZGC uses **load barriers** instead of write barriers. When an object reference  
is loaded from the heap, the barrier checks if any action is needed (such as  
updating a relocated pointer).  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          ZGC Load Barrier                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   Application Code:        Load Barrier Check:                              │
│   ┌───────────────┐        ┌─────────────────────────────────────────┐     │
│   │ Object ref =  │        │ if (pointer needs remapping) {         │     │
│   │   obj.field   │ ──────►│     update pointer to new location     │     │
│   └───────────────┘        │ }                                       │     │
│                            │ return corrected pointer                │     │
│                            └─────────────────────────────────────────┘     │
│                                                                             │
│   Benefits:                                                                 │
│   - Application always sees up-to-date references                          │
│   - Relocation can happen concurrently                                      │
│   - No stop-the-world pause needed for compaction                          │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```


### ZGC Phases  

ZGC operates in several concurrent phases with minimal STW pauses:  

| Phase                  | Type        | Description                           |
|------------------------|-------------|---------------------------------------|
| Pause Mark Start       | STW (<1ms)  | Start marking, scan thread stacks     |
| Concurrent Mark        | Concurrent  | Traverse object graph, mark live objs |
| Pause Mark End         | STW (<1ms)  | Complete marking                       |
| Concurrent Process     | Concurrent  | Process weak references               |
| Concurrent Reset       | Concurrent  | Reset metadata for next cycle         |
| Concurrent Relocate    | Concurrent  | Move objects, update references       |

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           ZGC Cycle                                         │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  STW (< 1ms)  │         Concurrent Work (Application Running)        │ STW │
│  ┌─────────┐  │  ┌─────────────────────────────────────────────────┐ │┌───┐│
│  │  Mark   │──┼─►│  Concurrent Marking ──► Concurrent Relocation  │─┼┤End││
│  │  Start  │  │  │                                                 │ │└───┘│
│  └─────────┘  │  └─────────────────────────────────────────────────┘ │     │
│               │                                                       │     │
│  Application  │  Application runs with load barriers                 │ App │
│   paused      │  checking/updating references as needed              │ run │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```


### ZGC Advantages  

ZGC provides exceptional benefits for latency-sensitive applications:  

- **Sub-millisecond pauses**: Typically < 0.5 ms, regardless of heap size  
- **Scalable**: Supports heaps from 8 MB to 16 TB  
- **Concurrent compaction**: No fragmentation without long pauses  
- **NUMA-aware**: Optimized for multi-socket systems  
- **Self-tuning**: Minimal configuration required  


### ZGC JVM Tuning Flags  

Common JVM flags for configuring ZGC:  

```bash
# Enable ZGC
-XX:+UseZGC

# Enable generational ZGC (Java 21+, default in Java 23+)
-XX:+UseZGC -XX:+ZGenerational

# Set maximum heap size (ZGC works best with larger heaps)
-Xmx16g

# Set concurrent GC threads (default is 25% of CPU cores)
-XX:ConcGCThreads=4

# Soft max heap size (ZGC tries to stay below this)
-XX:SoftMaxHeapSize=8g

# Uncommit unused memory (returns memory to OS)
-XX:+ZUncommit
-XX:ZUncommitDelay=300

# Enable large pages for better performance
-XX:+UseLargePages
```

Example configuration for a low-latency trading application:  

```bash
java -Xms32g -Xmx32g \
     -XX:+UseZGC \
     -XX:+ZGenerational \
     -XX:SoftMaxHeapSize=28g \
     -XX:+UseLargePages \
     -XX:+AlwaysPreTouch \
     -jar trading-app.jar
```


### Generational ZGC  

Java 21 introduced Generational ZGC which separates young and old generations  
while maintaining the sub-millisecond pause time guarantee:  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       Generational ZGC (Java 21+)                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Benefits of Generational ZGC:                                              │
│  - Faster collection of short-lived objects                                 │
│  - Reduced memory overhead                                                  │
│  - Better throughput for allocation-heavy workloads                         │
│  - Maintains sub-millisecond pause times                                    │
│                                                                             │
│  ┌─────────────────────────┐  ┌─────────────────────────────────────────┐  │
│  │    Young Generation     │  │           Old Generation                │  │
│  │  (Collected frequently) │  │       (Collected less often)            │  │
│  │                         │  │                                         │  │
│  │  Short-lived objects    │  │  Long-lived objects promoted            │  │
│  │  Most garbage here      │  │  from young generation                  │  │
│  └─────────────────────────┘  └─────────────────────────────────────────┘  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```


## Shenandoah GC  

Shenandoah is a low-pause-time garbage collector that performs concurrent  
compaction. It was developed by Red Hat and is included in OpenJDK. Like ZGC,  
Shenandoah aims for pause times independent of heap size.  


### Shenandoah Design  

Shenandoah's key innovation is **concurrent compaction**—the ability to compact  
the heap while the application continues running. This is achieved through:  

| Feature             | Description                                              |
|---------------------|----------------------------------------------------------|
| Brooks Pointers     | Forwarding pointers in object headers                    |
| Read Barriers       | Check if object has been relocated                       |
| Write Barriers      | Ensure concurrent updates are safe                       |
| Self-healing        | References automatically update during access            |


### Brooks Pointers  

Each object in Shenandoah contains a **forwarding pointer** that points to  
itself initially and is updated to point to the new location during relocation:  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        Brooks Forwarding Pointer                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Before Relocation:                                                         │
│  ┌─────────────────────────────────┐                                       │
│  │  ┌─────────────────────────┐   │                                        │
│  │  │ Forwarding Ptr ──────┐  │   │ Points to itself                       │
│  │  ├─────────────────────│─┘  │   │                                        │
│  │  │ Object Header       │   │   │                                        │
│  │  ├─────────────────────────┤   │                                        │
│  │  │ Object Data             │   │                                        │
│  │  └─────────────────────────┘   │                                        │
│  └─────────────────────────────────┘                                       │
│                                                                             │
│  After Relocation:                                                          │
│  ┌──────────────────────────┐     ┌─────────────────────────┐              │
│  │ Old Location             │     │ New Location            │              │
│  │  ┌────────────────────┐  │     │  ┌────────────────────┐ │              │
│  │  │ Fwd Ptr ──────────────────────►│ Fwd Ptr ──────┐    │ │              │
│  │  ├────────────────────┤  │     │  ├───────────│──┘    │ │              │
│  │  │ (Stale data)       │  │     │  │ Object Header     │ │              │
│  │  └────────────────────┘  │     │  ├────────────────────┤ │              │
│  └──────────────────────────┘     │  │ Object Data        │ │              │
│                                   │  └────────────────────┘ │              │
│                                   └─────────────────────────┘              │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```


### Shenandoah GC Phases  

Shenandoah operates in several phases:  

| Phase                  | Type        | Description                           |
|------------------------|-------------|---------------------------------------|
| Init Mark              | STW (<1ms)  | Scan GC roots, prepare for marking    |
| Concurrent Marking     | Concurrent  | Traverse object graph                 |
| Final Mark             | STW (<1ms)  | Complete marking, prepare for evac    |
| Concurrent Cleanup     | Concurrent  | Reclaim regions with no live objects  |
| Concurrent Evacuation  | Concurrent  | Copy live objects to new regions      |
| Init Update Refs       | STW (<1ms)  | Prepare for reference updating        |
| Concurrent Update Refs | Concurrent  | Update all references to new locations|
| Final Update Refs      | STW (<1ms)  | Complete reference updating           |
| Concurrent Cleanup     | Concurrent  | Reclaim evacuated regions             |

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        Shenandoah GC Cycle                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────┐ Concurrent  ┌─────┐ Concurrent  ┌─────┐ Concurrent  ┌─────┐       │
│  │Init │   Marking   │Final│ Evacuation  │Init │ Update Refs │Final│       │
│  │Mark │────────────►│Mark │────────────►│UR   │────────────►│ UR  │       │
│  │(STW)│             │(STW)│             │(STW)│             │(STW)│       │
│  └─────┘             └─────┘             └─────┘             └─────┘       │
│   <1ms                <1ms                <1ms                <1ms         │
│                                                                             │
│  Concurrent phases run alongside application threads                        │
│  STW pauses are very brief - typically under 1 millisecond                 │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```


### Shenandoah Advantages  

Shenandoah provides significant benefits:  

- **Consistent low pauses**: Sub-millisecond pauses regardless of heap size  
- **Concurrent compaction**: Eliminates fragmentation without STW pauses  
- **Wide availability**: Available in most OpenJDK distributions  
- **Proven in production**: Used in many enterprise applications  
- **Memory efficient**: Reasonable memory overhead  


### Shenandoah JVM Tuning Flags  

Common JVM flags for configuring Shenandoah:  

```bash
# Enable Shenandoah GC
-XX:+UseShenandoahGC

# Set heuristics mode (adaptive, static, compact, aggressive)
-XX:ShenandoahGCHeuristics=adaptive

# Percentage of heap to allocate before triggering GC
-XX:ShenandoahAllocationThreshold=10

# Free threshold to trigger concurrent GC
-XX:ShenandoahFreeThreshold=10

# Number of parallel GC threads
-XX:ParallelGCThreads=8

# Number of concurrent GC threads
-XX:ConcGCThreads=4

# Enable uncommit for returning memory to OS
-XX:ShenandoahUncommitDelay=1000
-XX:ShenandoahGuaranteedGCInterval=30000
```

Example configuration for a microservices application:  

```bash
java -Xms2g -Xmx2g \
     -XX:+UseShenandoahGC \
     -XX:ShenandoahGCHeuristics=compact \
     -XX:+AlwaysPreTouch \
     -jar microservice.jar
```


### Shenandoah Heuristics  

Shenandoah supports different heuristics modes for different workloads:  

| Heuristic   | Description                                                  |
|-------------|--------------------------------------------------------------|
| adaptive    | Default; balances throughput and pause times                 |
| static      | Triggers GC at fixed heap occupancy thresholds               |
| compact     | Aggressive compaction for fragmentation-prone workloads      |
| aggressive  | Continuous GC for testing and debugging                      |
| passive     | Never initiates GC cycles (for testing only)                 |


## Epsilon GC  

Epsilon is a **no-op garbage collector** that handles memory allocation but  
never reclaims any garbage. It is designed for specific use cases where GC  
overhead must be eliminated or measured.  


### Epsilon Design  

Epsilon is intentionally minimal:  

| Feature              | Description                                           |
|----------------------|-------------------------------------------------------|
| Memory allocation    | Handles object allocation as normal                   |
| No garbage collection| Never performs any collection                         |
| OutOfMemoryError     | Application crashes when heap is exhausted            |
| Zero GC overhead     | No GC threads, barriers, or pauses                    |


### Epsilon Use Cases  

Epsilon is useful for specific scenarios:  

| Use Case                    | Description                                      |
|-----------------------------|--------------------------------------------------|
| Performance testing         | Measure application without GC interference      |
| Memory pressure testing     | Find how much memory application truly needs     |
| Short-lived applications    | Jobs that complete before filling heap           |
| Latency-sensitive testing   | Establish baseline latency without GC pauses     |
| GC algorithm comparison     | Compare overhead of different collectors         |
| Memory leak detection       | Application quickly fails if leaking memory      |

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        Epsilon GC Behavior                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   Heap Usage Over Time:                                                     │
│                                                                             │
│   Memory │                                         ┌─── OutOfMemoryError   │
│     ▲    │                                    ┌────┘                        │
│     │    │                              ┌─────┘                             │
│     │    │                        ┌─────┘                                   │
│   Max────┼────────────────────────┼─────────────────────────────────────   │
│     │    │                  ┌─────┘                                         │
│     │    │            ┌─────┘                                               │
│     │    │      ┌─────┘                                                     │
│     │    │ ┌────┘                                                           │
│     │    │─┘                                                                │
│     │    └──────────────────────────────────────────────────────────► Time │
│          Start                                              Crash          │
│                                                                             │
│   With Epsilon: Memory only increases, never reclaimed                      │
│   Application must complete before heap exhaustion                          │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```


### Epsilon JVM Tuning Flags  

JVM flags for Epsilon:  

```bash
# Enable Epsilon GC (must explicitly unlock experimental features in older JDKs)
-XX:+UseEpsilonGC

# For older Java versions (before Epsilon became stable)
-XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC

# Set heap size appropriately for your workload
-Xms4g -Xmx4g

# Optional: Exit on OutOfMemoryError for clean shutdown
-XX:+ExitOnOutOfMemoryError

# Optional: Heap dump on OOM for analysis
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/path/to/dumps
```

Example configuration for performance baseline testing:  

```bash
java -Xms8g -Xmx8g \
     -XX:+UseEpsilonGC \
     -XX:+ExitOnOutOfMemoryError \
     -jar benchmark-app.jar
```


### Epsilon demonstration  

This example shows how Epsilon handles memory:  

```java
void main() {

    var runtime = Runtime.getRuntime();
    var list = new ArrayList<byte[]>();
    
    IO.println("Starting Epsilon GC demonstration");
    IO.println("Max heap: " + runtime.maxMemory() / (1024 * 1024) + " MB");
    
    try {
        for (int i = 0; i < 1000; i++) {
            // Allocate 1 MB blocks
            list.add(new byte[1024 * 1024]);
            
            if (i % 50 == 0) {
                long used = runtime.totalMemory() - runtime.freeMemory();
                IO.println("Allocated " + (i + 1) + " MB, " +
                          "Used: " + used / (1024 * 1024) + " MB");
            }
        }
    } catch (OutOfMemoryError e) {
        IO.println("OutOfMemoryError: Heap exhausted as expected with Epsilon");
    }
}
```

Run with Epsilon enabled:  

```bash
java --enable-preview --source 25 \
     -Xms256m -Xmx256m \
     -XX:+UseEpsilonGC \
     EpsilonDemo.java
```

The application will crash with `OutOfMemoryError` when the heap is exhausted,  
demonstrating that Epsilon never reclaims memory.  


## Comparison of GC Algorithms  

This section compares the four garbage collectors across various dimensions:  


### Feature Comparison  

| Feature              | G1 GC          | ZGC            | Shenandoah     | Epsilon        |
|----------------------|----------------|----------------|----------------|----------------|
| Default (Java 9+)    | Yes            | No             | No             | No             |
| Typical pause times  | 10-200 ms      | < 1 ms         | < 10 ms        | None           |
| Max pause times      | 500+ ms        | < 1 ms         | < 10 ms        | None           |
| Heap size support    | GB to TB       | MB to 16 TB    | MB to TB       | Any            |
| Throughput           | High           | High           | High           | Maximum        |
| Memory overhead      | Low-Medium     | Medium         | Medium         | None           |
| Concurrent compact   | Partial        | Yes            | Yes            | N/A            |
| Generational         | Yes            | Yes (Java 21+) | No             | N/A            |
| JDK availability     | All            | OpenJDK 11+    | OpenJDK 12+    | OpenJDK 11+    |


### Pause Time Comparison  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    Typical Pause Time Comparison                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Pause Time (ms)                                                            │
│                                                                             │
│     500 ─┤                                                                  │
│          │  ░░░░░ G1 Full GC (worst case)                                  │
│          │  ░░░░░                                                          │
│     200 ─┤  ░░░░░                                                          │
│          │  ░░░░░                                                          │
│          │  ░░░░░                                                          │
│     100 ─┤  ░░░░░ G1 Mixed GC                                              │
│          │  ░░░░░                                                          │
│          │  ░░░░░                                                          │
│      50 ─┤  ░░░░░                                                          │
│          │  ░░░░░                                                          │
│          │  ░░░░░   ▓▓▓ G1 Young GC                                        │
│      10 ─┤  ░░░░░   ▓▓▓                   ▒▒▒ Shenandoah                   │
│          │  ░░░░░   ▓▓▓                   ▒▒▒                              │
│       1 ─┤  ░░░░░   ▓▓▓   ███ ZGC         ▒▒▒                              │
│          │  ░░░░░   ▓▓▓   ███             ▒▒▒                              │
│       0 ─┼──────────────────────────────────────────► Epsilon (no pauses) │
│          │    G1        ZGC      Shenandoah    Epsilon                     │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```


### Use Case Recommendations  

| Application Type           | Recommended GC | Reason                          |
|----------------------------|----------------|---------------------------------|
| General purpose            | G1             | Good balance, well-tested       |
| Web services (typical)     | G1             | Predictable performance         |
| Low-latency trading        | ZGC            | Sub-ms pauses critical          |
| Real-time systems          | ZGC/Shenandoah | Consistent low latency          |
| Large heap (> 32 GB)       | ZGC            | Scales well with heap size      |
| Microservices              | G1/Shenandoah  | Good startup, reasonable pause  |
| Batch processing           | G1             | High throughput, pauses okay    |
| Performance testing        | Epsilon        | Zero GC interference            |
| Short-lived processes      | Epsilon        | No GC needed                    |


### Memory Overhead Comparison  

| Collector   | Overhead Source                                              |
|-------------|--------------------------------------------------------------|
| G1          | Remembered sets, region metadata (~5-10% overhead)           |
| ZGC         | Colored pointers, multi-mapping (~10-15% overhead)           |
| Shenandoah  | Brooks pointers, barriers (~10-15% overhead)                 |
| Epsilon     | None (but never frees memory)                                |


## Choosing the Right GC  

Selecting the appropriate garbage collector depends on your application's  
requirements. Use this decision framework to guide your choice.  


### Decision Framework  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      GC Selection Decision Tree                             │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│                    What are your primary requirements?                      │
│                                                                             │
│                              ┌────────────┐                                 │
│                              │   Start    │                                 │
│                              └─────┬──────┘                                 │
│                                    │                                        │
│                    ┌───────────────┼───────────────┐                        │
│                    ▼               ▼               ▼                        │
│             ┌────────────┐  ┌────────────┐  ┌────────────┐                  │
│             │ Ultra-low  │  │  General   │  │Performance │                  │
│             │  latency   │  │  purpose   │  │  testing   │                  │
│             │  (< 1 ms)  │  │            │  │            │                  │
│             └─────┬──────┘  └─────┬──────┘  └─────┬──────┘                  │
│                   │               │               │                         │
│             ┌─────┴─────┐        │               │                         │
│             ▼           ▼        ▼               ▼                         │
│        ┌────────┐ ┌────────┐ ┌────────┐    ┌────────────┐                  │
│        │  ZGC   │ │Shenan- │ │   G1   │    │  Epsilon   │                  │
│        │        │ │ doah   │ │ (def)  │    │            │                  │
│        └────────┘ └────────┘ └────────┘    └────────────┘                  │
│                                                                             │
│  Further considerations:                                                    │
│  - ZGC: Best for heaps > 4GB, Java 17+                                     │
│  - Shenandoah: Good alternative to ZGC, widely available                   │
│  - G1: Best general choice, extensive tuning options                       │
│  - Epsilon: Only for testing or short-lived processes                      │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```


### Latency Requirements  

| Latency Requirement        | Recommended GC    | Configuration                  |
|----------------------------|-------------------|--------------------------------|
| Any pause acceptable       | G1                | Default settings               |
| < 200 ms 99th percentile   | G1                | Tuned MaxGCPauseMillis         |
| < 50 ms 99th percentile    | G1/Shenandoah     | Careful tuning                 |
| < 10 ms 99th percentile    | ZGC/Shenandoah    | Default is usually fine        |
| < 1 ms 99th percentile     | ZGC               | Default settings               |


### Throughput vs Latency Trade-offs  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    Throughput vs Latency Trade-off                          │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Throughput ▲                                                               │
│     (high)  │                                                               │
│             │  ┌─────────┐                                                  │
│             │  │ Epsilon │ (no GC overhead, but OOM risk)                   │
│             │  └─────────┘                                                  │
│             │         ┌─────────┐                                           │
│             │         │   G1    │ (high throughput, moderate pauses)        │
│             │         └─────────┘                                           │
│             │              ┌────────────┐                                   │
│             │              │ Shenandoah │ (good throughput, low pauses)     │
│             │              └────────────┘                                   │
│             │                   ┌─────────┐                                 │
│             │                   │   ZGC   │ (good throughput, < 1ms pause) │
│             │                   └─────────┘                                 │
│      (low)  │                                                               │
│             └──────────────────────────────────────────────────────────────►│
│                   Low                                              High     │
│                                 Latency (pause times)                       │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```


### Heap Size Considerations  

| Heap Size        | Recommended GC     | Notes                              |
|------------------|--------------------|------------------------------------|
| < 256 MB         | G1 or Serial       | G1 overhead may be significant     |
| 256 MB - 4 GB    | G1                 | Default, well-tuned                |
| 4 GB - 32 GB     | G1 or ZGC          | Both work well                     |
| 32 GB - 256 GB   | ZGC or Shenandoah  | Low-latency collectors preferred   |
| > 256 GB         | ZGC                | Designed for very large heaps      |


## Best Practices  

Following these best practices helps ensure optimal GC performance and  
application stability.  


### Monitoring GC Performance  

Use these tools to monitor GC behavior:  

| Tool                   | Description                                         |
|------------------------|-----------------------------------------------------|
| JDK Flight Recorder    | Low-overhead profiling built into JVM               |
| VisualVM               | GUI for monitoring GC, heap, threads                |
| GC Logs                | Detailed GC event information                       |
| jstat                  | Command-line GC statistics                          |
| jcmd                   | Diagnostic commands for running JVM                 |


### Enable GC Logging  

Always enable GC logging in production:  

```bash
# Modern unified logging (Java 9+)
-Xlog:gc*:file=gc.log:time,uptime,level,tags:filecount=10,filesize=100m

# More detailed logging
-Xlog:gc+heap=debug:file=gc.log:time,uptime:filecount=5,filesize=50m

# Log to both file and console
-Xlog:gc*:file=gc.log:time -Xlog:gc:stdout:time
```


### GC Log Analysis  

Key metrics to monitor in GC logs:  

| Metric                    | Healthy Range         | Warning Signs            |
|---------------------------|-----------------------|--------------------------|
| GC pause time             | < target              | Exceeds MaxGCPauseMillis |
| GC frequency              | Stable                | Increasing over time     |
| Heap after GC             | < 70% of max          | Consistently > 80%       |
| Allocation rate           | Stable                | Sudden spikes            |
| Promotion rate            | Low relative to alloc | High promotion rate      |
| Full GC frequency         | Rare or never         | Frequent Full GCs        |


### JVM Tuning Guidelines  

General tuning recommendations:  

```bash
# Start with reasonable heap sizing
-Xms4g -Xmx4g  # Equal min/max avoids resize pauses

# Pre-touch memory for consistent performance
-XX:+AlwaysPreTouch

# Use large pages if available (improves TLB efficiency)
-XX:+UseLargePages

# Set appropriate thread counts
-XX:ParallelGCThreads=8   # Usually = CPU cores
-XX:ConcGCThreads=2       # Usually = ParallelGCThreads/4

# Enable string deduplication for string-heavy apps
-XX:+UseStringDeduplication  # G1 only
```


### Common Pitfalls to Avoid  

| Pitfall                      | Problem                      | Solution                    |
|------------------------------|------------------------------|------------------------------|
| Over-tuning                  | Breaks self-tuning           | Start with defaults          |
| Ignoring memory leaks        | GC cannot fix leaks          | Profile and fix app code     |
| Undersized heap              | Excessive GC overhead        | Increase heap size           |
| Oversized heap               | Long GC pauses (G1)          | Right-size or use ZGC        |
| Explicit System.gc()         | Triggers unexpected Full GC  | Remove or disable            |
| Finalizers                   | Delays object reclamation    | Use try-with-resources       |
| Ignoring GC logs             | Problems go unnoticed        | Monitor in production        |


### Memory Leak Detection  

When GC cannot keep up, suspect a memory leak:  

```java
void main() {

    // Common memory leak patterns to avoid:
    
    // 1. Static collections that grow unboundedly
    // BAD: static List<Object> cache = new ArrayList<>();
    
    // 2. Listeners not removed
    // BAD: eventSource.addListener(listener); // never removed
    
    // 3. Unclosed resources
    // BAD: InputStream is = new FileInputStream(file); // never closed
    
    // GOOD: Use try-with-resources
    // try (var is = new FileInputStream(file)) { ... }
    
    // 4. Custom caches without eviction
    // BAD: Map<Key, Value> cache = new HashMap<>(); // grows forever
    
    // GOOD: Use bounded cache with eviction
    // Map<Key, Value> cache = Collections.synchronizedMap(
    //     new LinkedHashMap<>(100, 0.75f, true) {
    //         protected boolean removeEldestEntry(Map.Entry e) {
    //             return size() > 100;
    //         }
    //     }
    // );
    
    IO.println("Memory leak patterns demonstrated (commented code)");
}
```


### Profiling with JFR  

Use Java Flight Recorder for detailed GC analysis:  

```bash
# Start recording with GC events
java -XX:StartFlightRecording=duration=60s,filename=recording.jfr \
     -jar application.jar

# Or attach to running process
jcmd <pid> JFR.start duration=60s filename=recording.jfr

# Analyze with JDK Mission Control or programmatically
```


### Application-Level Optimizations  

Reduce GC pressure through application design:  

| Optimization                 | Description                                     |
|------------------------------|------------------------------------------------|
| Object pooling               | Reuse objects for frequently allocated types    |
| Primitive arrays             | Use primitives instead of boxed types           |
| StringBuilder                | Avoid string concatenation in loops             |
| Lazy initialization          | Don't create objects until needed               |
| Escape analysis              | Let JVM optimize short-lived local objects      |
| Off-heap storage             | Use ByteBuffer for large data sets              |


### Example: Reducing Allocation  

This example demonstrates reducing object allocation:  

```java
void main() {

    int iterations = 1_000_000;
    
    // Measure high-allocation approach
    long start1 = System.nanoTime();
    long sum1 = 0;
    for (int i = 0; i < iterations; i++) {
        // Creates new String each iteration
        String s = "Value: " + i;
        sum1 += s.length();
    }
    long time1 = System.nanoTime() - start1;
    
    // Measure low-allocation approach
    long start2 = System.nanoTime();
    long sum2 = 0;
    var sb = new StringBuilder();
    for (int i = 0; i < iterations; i++) {
        sb.setLength(0);  // Reuse StringBuilder
        sb.append("Value: ").append(i);
        sum2 += sb.length();
    }
    long time2 = System.nanoTime() - start2;
    
    IO.println("High allocation: " + time1 / 1_000_000 + " ms");
    IO.println("Low allocation:  " + time2 / 1_000_000 + " ms");
    IO.println("Speedup: " + String.format("%.2fx", (double) time1 / time2));
}
```

By reusing the StringBuilder, we eliminate millions of intermediate String  
allocations, reducing GC pressure and improving performance.  


## Conclusion  

Garbage collection is fundamental to Java's memory management, and understanding  
the available collectors is essential for building high-performance applications.  
Modern Java provides a spectrum of collectors suited to different requirements:  

**G1 GC** remains the default and best choice for most applications. It provides  
a good balance between throughput and latency, with extensive tuning options for  
specific workloads. Its predictable pause times and automatic optimization make  
it suitable for everything from web services to batch processing.  

**ZGC** represents the cutting edge of low-latency garbage collection. With  
sub-millisecond pause times regardless of heap size, it is ideal for applications  
where consistent response times are critical, such as financial trading systems,  
real-time analytics, and large-scale data processing.  

**Shenandoah** offers similar low-latency benefits to ZGC with a different  
implementation approach. Its concurrent compaction and wide availability make  
it a strong choice for applications requiring consistent performance without  
the uncertainty of GC pauses.  

**Epsilon** serves a unique niche for testing and benchmarking. By eliminating  
GC entirely, it helps developers understand application behavior without GC  
interference and is useful for short-lived processes that complete before  
exhausting memory.  

### Key Takeaways  

| Principle                         | Description                               |
|-----------------------------------|-------------------------------------------|
| Start with defaults               | Modern collectors are well-tuned          |
| Monitor before tuning             | Understand actual behavior first          |
| Match GC to requirements          | Choose based on latency/throughput needs  |
| Enable GC logging                 | Essential for production debugging        |
| Fix application issues first      | GC cannot solve memory leaks              |
| Test thoroughly                   | GC behavior varies with workload          |

The evolution from early collectors to today's G1, ZGC, and Shenandoah  
demonstrates the JVM's continued advancement in handling the demands of  
modern applications. By understanding these collectors and following best  
practices, developers can build Java applications that are both performant  
and reliable.  
