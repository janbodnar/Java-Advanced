# Collectors


## Creating list of Integers with streams 

```java
void main() {

    List<Integer> nums1 = IntStream.range(1, 4).boxed().toList();
    List<Integer> nums2 = IntStream.range(1, 4)
            .collect(ArrayList::new, List::add, List::addAll);
    List<Integer> nums3 = Stream.iterate(1, x -> x + 1).limit(3).toList();
    List<Integer> nums4 = Stream.of(1, 2, 3).toList();


    System.out.println(nums1);
    System.out.println(nums2);
    System.out.println(nums3);
    System.out.println(nums4);
}
```














The `Collector.of` method in Java's `java.util.stream.Collector` class is a factory method used to create a custom `Collector` for use in stream operations. It allows you to define how a stream's elements are collected into a final result by specifying the key components of the collection process.

### Definition
The `Collector.of` method has the following signatures in the Java Stream API:

1. **Basic Form**:
   ```java
   public static <T, A, R> Collector<T, A, R> of(
       Supplier<A> supplier,
       BiConsumer<A, T> accumulator,
       BinaryOperator<A> combiner,
       Function<A, R> finisher,
       Collector.Characteristics... characteristics)
   ```
   - **`<T>`**: The type of input elements in the stream.
   - **`<A>`**: The mutable accumulation type (intermediate container).
   - **`<R>`**: The final result type.
   - **Parameters**:
     - `supplier`: A function that creates a new mutable container (e.g., `StringBuilder::new`).
     - `accumulator`: A function that incorporates a stream element into the mutable container (e.g., `StringBuilder::append`).
     - `combiner`: A function that merges two containers, used in parallel streams (e.g., `(sb1, sb2) -> sb1.append(sb2)`).
     - `finisher`: A function that transforms the intermediate container into the final result (e.g., `StringBuilder::toString`).
     - `characteristics`: Optional `Collector.Characteristics` (e.g., `CONCURRENT`, `UNORDERED`, `IDENTITY_FINISH`) that describe the collector's behavior.
   - **Returns**: A `Collector` instance that can be used with `Stream.collect()`.

2. **Simplified Form (Identity Finisher)**:
   ```java
   public static <T, R> Collector<T, R, R> of(
       Supplier<R> supplier,
       BiConsumer<R, T> accumulator,
       BinaryOperator<R> combiner,
       Collector.Characteristics... characteristics)
   ```
   - This version is used when the intermediate container is the same as the final result type (i.e., no finisher transformation is needed).
   - The `finisher` is implicitly the identity function (`x -> x`).
   - **Parameters** and **returns** are similar to the first form, but `<A>` and `<R>` are the same type.

### Purpose
`Collector.of` is used to create custom collectors when the built-in collectors (e.g., `Collectors.toList()`, `Collectors.joining()`) are insufficient for specific use cases. It provides fine-grained control over the collection process, allowing you to define how elements are accumulated, combined, and finalized.

### Example Usage
From your previous example:
```java
Collector<String, StringBuilder, String> stringCollector =
    Collector.of(
        StringBuilder::new,             // Supplier: Creates a new StringBuilder
        StringBuilder::append,          // Accumulator: Appends each string
        StringBuilder::append,          // Combiner: Merges two StringBuilders
        StringBuilder::toString         // Finisher: Converts StringBuilder to String
    );
```

### Characteristics
You can specify `Collector.Characteristics` to optimize the collector:
- `CONCURRENT`: Indicates the collector can handle concurrent accumulation (e.g., for parallel streams).
- `UNORDERED`: Indicates the result is not dependent on the order of elements.
- `IDENTITY_FINISH`: Indicates the finisher is the identity function (no transformation needed).

If no characteristics apply, pass an empty array (`new Collector.Characteristics[]{}`).

### Notes
- The collector created by `Collector.of` is used with `Stream.collect()` to process stream elements.
- It is particularly useful for custom aggregations, such as building specialized data structures or performing complex reductions.
- Ensure the supplier, accumulator, combiner, and finisher are thread-safe if `CONCURRENT` is specified or if used in parallel streams.

Let me know if you need further clarification or additional examples!

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
