# Gatherers


## Deduplicate consecutive elements

```java
import java.util.stream.Gatherer;
import java.util.stream.Stream;

class State {
    String previous;
}

void main(String[] args) {

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
