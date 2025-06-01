# Collectors



## Collect.of

```java
import java.util.List;
import java.util.stream.Collector;

void main() {
    List<String> words = List.of("Apple", "Banana", "Cherry");

    Collector<String, StringBuilder, String> delimitedCollector =
        Collector.of(
            StringBuilder::new,                    // Supplier
            (sb, word) -> sb.append(word).append(", "), // Accumulator
            StringBuilder::append,                 // Combiner
            sb -> sb.length() > 0 
                ? sb.substring(0, sb.length() - 2) // Remove trailing ", "
                : sb.toString()                    // Finisher
        );

    String result = words.stream().collect(delimitedCollector);
    System.out.println(result); // Output: Apple, Banana, Cherry
}
```
