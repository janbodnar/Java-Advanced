# Gatherers


## Type Parameters

The `Gatherer<T, A, R>` interface in Java's Stream API (introduced in JDK 22) has three type parameters:  

1. **`T`** – The type of input elements to the gather operation.  
2. **`A`** – The type of the gatherer's private state object (often mutable),  
   used to track previously seen elements or influence later transformations.  
4. **`R`** – The type of output elements from the gather operation.  

## Core Functions of a Gatherer

A **Gatherer** is defined by four key functions:

1. **Initializer (`initializer()`)** – Creates a new state object (`A`).  
2. **Integrator (`integrator()`)** – Processes each input element (`T`) and optionally emits output (`R`).  
3. **Combiner (`combiner()`)** – Merges two state objects (`A`) for parallel execution.  
4. **Finisher (`finisher()`)** – Performs a final action when the stream ends.  


**What Does `?` Mean in `Gatherer<String, ?, String>`?**

- The wildcard `?` in `Gatherer<String, ?, String>` means **the intermediate state type is unspecified**.
- This allows flexibility in defining the state used during the transformation process.
- The state could be **mutable or immutable**, depending on the specific gatherer implementation.

```java
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class GathererExample {

    public static void main(String[] args) {
        var words = Stream.of("apple", "banana", "cherry", "date", "fig", "grape", "kiwi");

        var gatherer = Gatherer.of(
            () -> null, // No explicit state needed
            Gatherer.Integrator.ofGreedy((state, element, downstream) -> {
                if (element.length() > 4) { // Example filtering logic
                    downstream.accept(element.toUpperCase());
                }
            })
        );

        var result = words.gather(gatherer).toList();
        System.out.println(result);
    }
}
```


## Limit operation

```java

// Limit Gatherer
<T> Gatherer<T, ?, T> limit(long limit) {
    return Gatherer.ofSequential(
            () -> new Object() {
                long counter = 0L;
            },
            (counter, element, downstream) -> {
                if (counter.counter >= limit) {
                    return false;
                } else {
                    counter.counter++;
                    return downstream.push(element);
                }
            });
}

void main() {

    var stream = Stream.of(1, 2, 3, 4, 5);
    var result = stream.gather(limit(3L)).toList();
    System.out.println("result = " + result);
}
```


## Distinct ignore case


```java
// Distinct Ignore Case Gatherer
Gatherer<String, ?, String> distinctIgnoreCase() {

    return Gatherer.ofSequential(
            () -> new HashSet<String>(),
            (set, element, downstream) -> {
                if (set.add(element.toLowerCase())) {
                    return downstream.push(element);
                }
                return true;
            });
}

void main() {
    var stream = Stream.of("one", "One", "ONE", "Two", "two", "tWo");
    var result = stream.gather(distinctIgnoreCase()).toList();
    System.out.println("result = " + result);
}
```





## Running sum

```java
import java.util.List;
import java.util.stream.Stream;


void main() {
    // Example 1: Gatherer to calculate running sum
    List<Integer> runningSums = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .gather(runningSumGatherer())
            .toList();
    System.out.println("Running Sums: " + runningSums);
}

Gatherer<Integer, int[], Integer> runningSumGatherer() {
    return Gatherer.ofSequential(
            () -> new int[] { 0 },
            (state, element, downstream) -> {
                state[0] += element;
                downstream.push(state[0]);
                return true;
            },
            Gatherer.defaultFinisher()
    // (state, downstream) -> {}
    );
}
```

## Counting negs/pos

```java
import java.util.List;
import java.util.stream.Gatherers;

class Count {

    private int positive;
    private int negative;

    public Count() {

    }

    public Count(int pos, int neg) {
        this.positive = pos;
        this.negative = neg;
    }
}

void main() {

    var numbers = List.of(1, -2, 3, -4, 5, -3, 4, -9, 8);

    // Count positive and negative elements
    var count = numbers.stream()
            .gather(Gatherers.scan(Count::new, (c, elem) -> {
                c.positive += elem > 0 ? 1 : 0;
                c.negative += elem < 0 ? 1 : 0;
                return c;
            })).reduce((c1, c2) -> new Count(c1.positive + c2.positive, c1.negative + c2.negative)).get();

    System.out.println("Positive Count: " + count.positive);
    System.out.println("Negative Count: " + count.negative);
}
```

## Creating a custom filter operation

```java
import java.util.stream.Gatherer;

void main() {
    List<Integer> evens = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .gather(evenGatherer())
            .toList();
    System.out.println("Even Numbers: " + evens);
}

Gatherer<Integer, Void, Integer> evenGatherer() {
    return Gatherer.ofSequential(
            // () -> null,
            Gatherer.defaultInitializer(),
            integrator(),
            // (_, element, downstream) -> {
            //     if (element % 2 == 0)
            //         downstream.push(element);
            //     return true;
            // },
            Gatherer.defaultFinisher());
}

Gatherer.Integrator<Void, Integer, Integer> integrator() {
    return Gatherer.Integrator.ofGreedy((_, element, downstream) -> {
        if (element % 2 == 0) {
            downstream.push(element);
        }
        return true; // Continue processing
    });
}
```



## Deduplicate consecutive elements

```java
import java.util.stream.Gatherer;
import java.util.stream.Stream;

class State {
    String previous;
}

void main() {

    Gatherer<String, State, String> deduplicate = Gatherer.ofSequential(
            State::new, // Initial state (no previous element)
            (state, element, downstream) -> {
                if (state.previous == null || !state.previous.equals(element)) {
                    downstream.push(element); // Push if different from previous
                }
                state.previous = element; // Update state to current element
                return true; // Continue processing
            },
            Gatherer.defaultFinisher());

    Stream<String> words = Stream.of("a", "a", "b", "b", "c", "a");
    var result = words.gather(deduplicate).toList();
    System.out.println(result); // Output: [a, b, c, a]
}
```
