# Collectors



## Collector.of

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

```java
void main() {
    List<String> words = List.of("cat", "dog", "elephant");

    Collector<String, Map<String, Integer>, Map<String, Integer>> charCountCollector =
        Collector.of(
            HashMap::new,                          // Supplier
            (map, word) -> map.put(word, word.length()), // Accumulator
            (map1, map2) -> {                      // Combiner
                map1.putAll(map2);
                return map1;
            },
            map -> map                             // Finisher (identity)
        );

    Map<String, Integer> result = words.stream().collect(charCountCollector);
    System.out.println(result); // Output: {cat=3, dog=3, elephant=8}
}
```

