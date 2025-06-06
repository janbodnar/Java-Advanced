# Gatherers


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
