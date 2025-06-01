# Collectors



## Collector.of

## Concatenating list

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

### Counting chars

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

### Uppercasing letters

```java
void main() {

    List<String> words = List.of("hello", "there", "java");

    Collector<String, ArrayList<String>, List<String>> uppercaseCollector =
        Collector.of(
            ArrayList::new,                        // Supplier
            (list, word) -> list.add(word.toUpperCase()), // Accumulator
            (list1, list2) -> {                    // Combiner
                list1.addAll(list2);
                return list1;
            },
            list -> list                           // Finisher (identity)
        );

    List<String> result = words.stream().collect(uppercaseCollector);
    System.out.println(result); // Output: [HELLO, WORLD, JAVA]
}
```

### Counting chars

```java

void main() {
    List<String> words = List.of("one", "two", "three");

    Collector<String, int[], Integer> lengthSumCollector =
        Collector.of(
            () -> new int[]{0},                    // Supplier
            (arr, word) -> arr[0] += word.length(), // Accumulator
            (arr1, arr2) -> {                      // Combiner
                arr1[0] += arr2[0];
                return arr1;
            },
            arr -> arr[0]                          // Finisher
        );

    Integer result = words.stream().collect(lengthSumCollector);
    System.out.println(result); // Output: 11 (3 + 3 + 5)
}
```
