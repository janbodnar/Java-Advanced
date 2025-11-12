# Covariance & Contravariance

Covariance and contravariance are fundamental concepts in Java's type system  
that describe how subtyping relationships between types relate to subtyping  
relationships between more complex types constructed from them. Understanding  
these concepts is essential for working effectively with Java generics,  
especially when using wildcards.  

**Covariance** refers to the ability to use a more derived type than originally  
specified. In Java generics, this is achieved using upper-bounded wildcards  
(`? extends T`). When a generic type is covariant, it preserves the ordering of  
types: if `Dog` is a subtype of `Animal`, then `List<? extends Dog>` is a  
"subtype" of `List<? extends Animal>`. Covariant types are producers - you can  
read from them but generally cannot write to them (except null).  

**Contravariance** refers to the ability to use a more generic (less derived)  
type than originally specified. In Java generics, this is achieved using  
lower-bounded wildcards (`? super T`). When a generic type is contravariant, it  
reverses the ordering of types: if `Dog` is a subtype of `Animal`, then  
`List<? super Dog>` is a "subtype" of `List<? super Animal>` in the opposite  
direction. Contravariant types are consumers - you can write to them but can  
only safely read Objects from them.  

**Invariance** is the default behavior in Java generics. `List<Dog>` is not  
considered a subtype of `List<Animal>`, even though `Dog` is a subtype of  
`Animal`. This prevents type safety violations. Without wildcards, generic  
types are invariant.  

The **PECS principle** (Producer Extends, Consumer Super) is a helpful mnemonic:  
use `? extends T` when you only read from a structure (it produces T instances),  
and use `? super T` when you only write to a structure (it consumes T  
instances). This principle guides the correct use of bounded wildcards to  
achieve maximum flexibility while maintaining type safety.  


## Covariance & Contravariance table

| Feature | Covariance (Upper Bound) | Contravariance (Lower Bound) |
| :--- | :--- | :--- |
| **Common Name** | "Extends Wildcard" | "Super Wildcard" |
| **Syntax** | `? extends T` | `? super T` |
| **Example** | `List<? extends Number>` | `List<? super Integer>` |
| **What It Means** | A list of *some unknown type* that is `T` or a **subtype** of `T`. | A list of *some unknown type* that is `T` or a **supertype** of `T`. |
| **Type of Bound** | **Upper Bound** (`T` is the *highest* class in the hierarchy you can guarantee). | **Lower Bound** (`T` is the *lowest* class in the hierarchy you can guarantee). |
| **Valid List Types** | `List<Number>`, `List<Integer>`, `List<Double>` | `List<Integer>`, `List<Number>`, `List<Object>` |
| **Can you READ? (Get)** | **Yes.** You are guaranteed to get an object that is at least of type `T`. | **No** (safely). You can only be sure you're getting an `Object`, as you don't know how high up the superclass chain the list's type goes. |
| **Can you WRITE? (Add)** | **No** (except `null`). You don't know the *specific* subtype. A `List<? extends Number>` could be a `List<Integer>`, so you can't add a `Double`. | **Yes.** You can safely add any object of type `T` (or its subtypes). A `List<Integer>`, `List<Number>`, or `List<Object>` can all safely accept an `Integer`. |
| **PECS Mnemonic** | **P**roducer **E**xtends | **C**onsumer **S**uper |
| **Primary Use Case** | When you are **reading** items *from* a generic structure (it *produces* items for you). | When you are **writing** items *to* a generic structure (it *consumes* items from you). |



## Wildcards with upper bounds

This example shows using wildcard types with upper bounds to accept lists of  
subclasses.  

```java
abstract class Shape {

  abstract void draw();
}

class Rectangle extends Shape {

  @Override
  void draw() {
    IO.println("Drawing rectangle");
  }
}

class Circle extends Shape {

  @Override
  void draw() {
    IO.println("Drawing circle");
  }
}

// Generic wildcard example
void main() {

  List<Rectangle> shapes1 = new ArrayList<>();
  shapes1.add(new Rectangle());

  List<Circle> shapes2 = new ArrayList<>();
  shapes2.add(new Circle());
  shapes2.add(new Circle());

  drawShapes(shapes1);
  drawShapes(shapes2);
}

void drawShapes(List<? extends Shape> lists) {

  for (Shape s : lists) {
    s.draw();
  }
}
```

The `drawShapes` method uses a bounded wildcard `? extends Shape` to accept  
lists of any type that extends `Shape`. This demonstrates covariance - the  
method can accept `List<Rectangle>`, `List<Circle>`, or `List<Shape>`. The  
wildcard makes the method more flexible than `List<Shape>` alone, which would  
not accept lists of subtypes. This is useful for read-only operations where  
you need to process elements polymorphically without adding new elements.  


## Reading with covariance

This example demonstrates reading values from a covariant collection using  
`? extends Number`.  

```java
void main() {

  List<Integer> integers = List.of(1, 2, 3, 4, 5);
  List<Double> doubles = List.of(1.1, 2.2, 3.3);

  printNumbers(integers);
  printNumbers(doubles);
}

void printNumbers(List<? extends Number> numbers) {

  for (Number n : numbers) {
    IO.println(n);
  }
}
```

The `printNumbers` method accepts any list containing Number or its subtypes.  
We can safely read from the list because we know every element is at least a  
Number. This demonstrates the producer aspect of covariance - the list produces  
Number instances that we can read and use polymorphically.  


## Sum calculation with covariance

This example shows how covariance enables processing different numeric types  
with a single method.  

```java
void main() {

  List<Integer> scores = List.of(85, 92, 78, 95, 88);
  List<Double> prices = List.of(19.99, 45.50, 12.75);

  IO.println("Total scores: " + sum(scores));
  IO.println("Total prices: " + sum(prices));
}

double sum(List<? extends Number> numbers) {

  double total = 0;
  for (Number n : numbers) {
    total += n.doubleValue();
  }
  return total;
}
```

The sum method works with any list of numbers by reading values and converting  
them to double. This shows how covariance allows writing flexible utility  
methods that operate on collections of different numeric types. The wildcard  
enables code reuse across Integer, Double, Float, and other Number subtypes.  


## Finding maximum with covariance

This example uses covariance with Comparable to find the maximum element in  
collections of different types.  

```java
void main() {

  List<Integer> numbers = List.of(5, 2, 9, 1, 7);
  List<String> words = List.of("apple", "zebra", "banana");

  IO.println("Max number: " + findMax(numbers));
  IO.println("Max word: " + findMax(words));
}

<T extends Comparable<T>> T findMax(List<? extends T> list) {

  T max = list.getFirst();
  for (T element : list) {
    if (element.compareTo(max) > 0) {
      max = element;
    }
  }
  return max;
}
```

The generic method uses both a type parameter bound and a covariant wildcard.  
The bound ensures T is Comparable, while the wildcard allows accepting lists  
of T or its subtypes. This combination provides type safety while maintaining  
flexibility for reading and comparing elements.  


## Writing with contravariance

This example demonstrates writing values to a contravariant collection using  
`? super Integer`.  

```java
void main() {

  List<Integer> integers = new ArrayList<>();
  List<Number> numbers = new ArrayList<>();
  List<Object> objects = new ArrayList<>();

  addIntegers(integers);
  addIntegers(numbers);
  addIntegers(objects);

  IO.println("Integers: " + integers);
  IO.println("Numbers: " + numbers);
  IO.println("Objects: " + objects);
}

void addIntegers(List<? super Integer> list) {

  list.add(1);
  list.add(2);
  list.add(3);
}
```

The `addIntegers` method accepts any list that can hold Integer or its  
supertypes. We can safely write Integer values because any supertype  
(Number, Object) can hold an Integer. This demonstrates the consumer aspect  
of contravariance - the list consumes Integer instances that we provide.  


## Comparator with contravariance

This example shows using contravariant wildcards with Comparator to sort  
different types.  

```java
import java.util.Comparator;

void main() {

  List<Integer> numbers = new ArrayList<>(List.of(5, 2, 9, 1));
  Comparator<Object> objectComparator = (a, b) ->
      a.toString().compareTo(b.toString());

  sortList(numbers, objectComparator);
  IO.println("Sorted: " + numbers);
}

<T> void sortList(List<T> list, Comparator<? super T> comparator) {

  list.sort(comparator);
}
```

The sortList method accepts a Comparator for T or any supertype of T. This  
allows using a more general comparator (like one for Object) to sort more  
specific types (like Integer). Contravariance here enables flexible comparison  
strategies while maintaining type safety.  


## PECS principle demonstration

This example demonstrates the PECS principle by copying elements from a  
producer to a consumer.  

```java
void main() {

  List<Integer> source = List.of(1, 2, 3, 4, 5);
  List<Number> destination = new ArrayList<>();

  copy(source, destination);
  IO.println("Destination: " + destination);
}

<T> void copy(List<? extends T> src, List<? super T> dest) {

  for (T item : src) {
    dest.add(item);
  }
}
```

The copy method demonstrates PECS: the source uses `? extends T` (producer)  
because we read from it, and the destination uses `? super T` (consumer)  
because we write to it. This allows copying from List<Integer> to List<Number>,  
which wouldn't be possible without wildcards due to invariance.  


## Array covariance

This example shows that arrays in Java are covariant, unlike generic  
collections.  

```java
void main() {

  Integer[] integers = {1, 2, 3};
  Number[] numbers = integers;

  IO.println("First number: " + numbers[0]);

  try {
    numbers[0] = 3.14;
  } catch (ArrayStoreException e) {
    IO.println("Cannot store Double in Integer array: " + e.getMessage());
  }
}
```

Arrays are covariant, meaning Integer[] is a subtype of Number[]. However,  
this leads to runtime errors when trying to store incompatible types. The JVM  
performs runtime checks to prevent storing the wrong type, throwing  
ArrayStoreException. Generics avoid this with compile-time checking.  


## Generic collections are invariant

This example demonstrates that generic collections are invariant by default,  
requiring wildcards for flexibility.  

```java
void main() {

  List<Integer> integers = List.of(1, 2, 3);

  // This would not compile without wildcards:
  // processNumbers(integers);  // Error!

  processNumbersWithWildcard(integers);
}

void processNumbersWithWildcard(List<? extends Number> numbers) {

  for (Number n : numbers) {
    IO.println(n.doubleValue());
  }
}
```

Unlike arrays, List<Integer> is not a subtype of List<Number> due to  
invariance. This prevents unsafe operations at compile time. Using wildcards  
(`? extends Number`) explicitly enables covariance when needed for read  
operations, providing type safety without runtime checks.  


## Bounded wildcards in return types

This example shows using bounded wildcards in method return types to provide  
flexibility to callers.  

```java
void main() {

  List<? extends Number> evenNumbers = getEvenNumbers();
  List<? extends Number> primeNumbers = getPrimeNumbers();

  IO.println("Even numbers: " + evenNumbers);
  IO.println("Prime numbers: " + primeNumbers);
}

List<? extends Number> getEvenNumbers() {

  return List.of(2, 4, 6, 8, 10);
}

List<? extends Number> getPrimeNumbers() {

  return List.of(2, 3, 5, 7, 11);
}
```

Methods can return bounded wildcards to allow flexibility in implementation  
while constraining the type for clients. The caller knows they'll get some  
kind of Number list and can read from it safely. This pattern is useful when  
the exact type returned might vary or be implementation-specific.  


## Multiple type parameters with wildcards

This example demonstrates using wildcards with multiple type parameters in a  
generic class.  

```java
record Pair<K, V>(K key, V value) {}

void main() {

  Pair<Integer, String> pair1 = new Pair<>(1, "one");
  Pair<Integer, Double> pair2 = new Pair<>(2, 2.0);

  printPair(pair1);
  printPair(pair2);
}

void printPair(Pair<? extends Number, ?> pair) {

  IO.println("Key: " + pair.key() + ", Value: " + pair.value());
}
```

The printPair method uses wildcards on multiple type parameters independently.  
The key is bounded to Number subtypes while the value is unbounded (any type).  
This shows how wildcards can be applied selectively to different type  
parameters based on what operations you need to perform.  


## Contravariance with collections addAll

This example shows how contravariant wildcards enable flexible collection  
operations like addAll.  

```java
void main() {

  List<Integer> integers = new ArrayList<>(List.of(1, 2, 3));
  List<Number> moreNumbers = List.of(4.5, 5L, 6);

  addAll(integers, List.of(4, 5, 6));
  IO.println("Integers: " + integers);
}

<T> void addAll(List<? super T> dest, List<? extends T> src) {

  for (T item : src) {
    dest.add(item);
  }
}
```

This mimics the signature of Collection.addAll, using both covariance and  
contravariance. The source is covariant (producer) and destination is  
contravariant (consumer), following PECS. This allows adding from a more  
specific list to a more general list while maintaining type safety.  


## Unbounded wildcards

This example demonstrates using unbounded wildcards when you don't need to  
know the specific type.  

```java
void main() {

  List<Integer> integers = List.of(1, 2, 3);
  List<String> strings = List.of("a", "b", "c");
  List<Double> doubles = List.of(1.1, 2.2);

  IO.println("Size: " + getSize(integers));
  IO.println("Size: " + getSize(strings));
  IO.println("Size: " + getSize(doubles));
}

int getSize(List<?> list) {

  return list.size();
}
```

The unbounded wildcard `?` means "list of unknown type". This is useful when  
you only need operations that don't depend on the element type, like getting  
size or checking emptiness. It's more flexible than Object and represents  
true ignorance of the type parameter.  


## Wildcard capture

This example shows the wildcard capture helper method pattern for working  
with wildcards.  

```java
void main() {

  List<Integer> integers = new ArrayList<>();
  integers.add(1);
  integers.add(2);
  integers.add(3);

  reverse(integers);
  
  IO.println("Reversed: " + integers);
}

void reverse(List<?> list) {

  reverseHelper(list);
}

<T> void reverseHelper(List<T> list) {

  int size = list.size();
  for (int i = 0; i < size / 2; i++) {
    T temp = list.get(i);
    list.set(i, list.get(size - 1 - i));
    list.set(size - 1 - i, temp);
  }
}
```

The wildcard capture pattern uses a helper method with a type parameter to  
"capture" the unknown type from a wildcard. This allows operations that  
require knowing the type is consistent (like swapping elements) while still  
accepting any list type. The helper captures the wildcard into a concrete type.  


## Generic method with bounded wildcards

This example shows using bounded wildcards as method parameters in a generic  
method.  

```java
void main() {

  List<Integer> integers = List.of(10, 20, 30);
  List<Double> doubles = List.of(1.5, 2.5, 3.5);

  IO.println("Contains 20: " + contains(integers, 20));
  IO.println("Contains 2.5: " + contains(doubles, 2.5));
}

<T> boolean contains(List<? extends T> list, T element) {

  for (T item : list) {
    if (item.equals(element)) {
      return true;
    }
  }
  return false;
}
```

The contains method combines a type parameter with a covariant wildcard. The  
type parameter T is inferred from the element being searched, and the list  
can be of T or any subtype. This provides flexibility while ensuring the  
search element is compatible with the list contents.  


## Contravariance with functional interfaces

This example demonstrates contravariance with Consumer functional interface.  

```java
import java.util.function.Consumer;

void main() {

  Consumer<Object> objectConsumer = obj -> IO.println("Object: " + obj);
  Consumer<Number> numberConsumer = num -> IO.println("Number: " + num);

  processList(List.of(1, 2, 3), objectConsumer);
  processList(List.of(1, 2, 3), numberConsumer);
}

<T> void processList(List<T> list, Consumer<? super T> consumer) {

  for (T item : list) {
    consumer.accept(item);
  }
}
```

The processList method accepts a Consumer for T or any supertype. This allows  
using more general consumers (like Consumer<Object>) with more specific types  
(like Integer). Contravariance in functional interfaces enables flexible  
callback mechanisms while maintaining type safety.  


## Covariance with Stream operations

This example shows how covariance works naturally with Stream API operations.  

```java
import java.util.stream.Stream;

void main() {

  List<Integer> integers = List.of(1, 2, 3, 4, 5);

  double average = calculateAverage(integers);
  IO.println("Average: " + average);
}

double calculateAverage(List<? extends Number> numbers) {

  return numbers.stream()
      .mapToDouble(Number::doubleValue)
      .average()
      .orElse(0.0);
}
```

The calculateAverage method uses covariance to accept any list of numbers and  
processes them using streams. The covariant wildcard allows the method to work  
with Integer, Double, Long, etc. This demonstrates how wildcards integrate  
seamlessly with modern Java APIs like streams.  


## Multiple bounds with extends

This example demonstrates using multiple bounds in a generic method to  
constrain type parameters.  

```java
interface Printable {
  String toPrintFormat();
}

record Document(String title, int pages) implements Comparable<Document>,
    Printable {

  @Override
  public int compareTo(Document other) {
    return Integer.compare(this.pages, other.pages);
  }

  @Override
  public String toPrintFormat() {
    return title + " (" + pages + " pages)";
  }
}

void main() {

  List<Document> docs = List.of(
      new Document("Manual", 50),
      new Document("Guide", 30)
  );

  processItems(docs);
}

<T extends Comparable<T> & Printable> void processItems(List<T> items) {

  var sorted = items.stream().sorted().toList();
  for (T item : sorted) {
    IO.println(item.toPrintFormat());
  }
}
```

The processItems method requires T to implement both Comparable and Printable  
using the `&` syntax for multiple bounds. This ensures compile-time safety for  
operations requiring both interfaces. Multiple bounds enable writing methods  
that depend on multiple capabilities of the type parameter.  


## Nested wildcards

This example shows using wildcards within wildcards for nested generic  
structures.  

```java
void main() {

  List<List<Integer>> intLists = List.of(
      List.of(1, 2, 3),
      List.of(4, 5, 6)
  );

  List<List<Double>> doubleLists = List.of(
      List.of(1.1, 2.2),
      List.of(3.3, 4.4)
  );

  printNested(intLists);
  printNested(doubleLists);
}

void printNested(List<? extends List<? extends Number>> lists) {

  for (List<? extends Number> list : lists) {
    for (Number num : list) {
      IO.print(num + " ");
    }
    IO.println();
  }
}
```

Nested wildcards allow working with complex nested generic structures. The  
outer wildcard allows lists of different List types, while the inner wildcard  
allows lists containing different Number types. This pattern is useful for  
processing hierarchical or nested data structures flexibly.  


## Generic builder pattern with wildcards

This example demonstrates using wildcards in a builder pattern for flexible  
object construction.  

```java
class Builder<T> {

  private List<T> items = new ArrayList<>();

  Builder<T> addAll(List<? extends T> newItems) {
    items.addAll(newItems);
    return this;
  }

  List<T> build() {
    return new ArrayList<>(items);
  }
}

void main() {

  List<Integer> integers = List.of(1, 2, 3);
  List<Number> numbers = new Builder<Number>()
      .addAll(integers)
      .addAll(List.of(4.5, 5.5))
      .build();

  IO.println("Built list: " + numbers);
}
```

The builder's addAll method uses covariance to accept lists of T or subtypes,  
enabling flexible construction. This allows adding Integer lists to a  
Number builder, or Double lists, making the builder more versatile. Wildcards  
in builders enable fluent APIs that work with type hierarchies.  


## Type inference with wildcards

This example shows how Java's type inference works with bounded wildcards in  
method calls.  

```java
void main() {

  var result1 = merge(List.of(1, 2, 3), List.of(4, 5));
  var result2 = merge(List.of("a", "b"), List.of("c", "d"));

  IO.println("Merged integers: " + result1);
  IO.println("Merged strings: " + result2);
}

<T> List<T> merge(List<? extends T> list1, List<? extends T> list2) {

  List<T> result = new ArrayList<>();
  result.addAll(list1);
  result.addAll(list2);
  return result;
}
```

The merge method uses covariant wildcards on both parameters, and the compiler  
infers T from the common supertype. This allows merging lists of the same type  
or compatible types. Type inference with wildcards reduces verbosity while  
maintaining type safety.  


## Contravariance with Predicate

This example demonstrates using contravariant wildcards with Predicate  
functional interface.  

```java
import java.util.function.Predicate;

void main() {

  Predicate<Object> notNull = obj -> obj != null;
  List<String> strings = List.of("hello", "world");

  var filtered = filter(strings, notNull);
  IO.println("Filtered: " + filtered);
}

<T> List<T> filter(List<T> list, Predicate<? super T> predicate) {

  return list.stream()
      .filter(predicate)
      .toList();
}
```

The filter method accepts a Predicate for T or any supertype, following  
contravariance for consumers. This allows using more general predicates  
(like checking for null on Object) with more specific types (like String).  
Contravariant functional interfaces enable code reuse across type hierarchies.  


## Covariance with Optional

This example shows using covariant wildcards with Optional to handle different  
numeric types.  

```java
import java.util.Optional;

void main() {

  Optional<Integer> intOpt = Optional.of(42);
  Optional<Double> doubleOpt = Optional.of(3.14);

  IO.println("Integer value: " + getValue(intOpt));
  IO.println("Double value: " + getValue(doubleOpt));
}

double getValue(Optional<? extends Number> optional) {

  return optional
      .map(Number::doubleValue)
      .orElse(0.0);
}
```

The getValue method uses covariance to accept Optional of any Number subtype.  
This demonstrates how wildcards work with wrapper types like Optional,  
allowing flexible handling of different numeric types. Covariance with  
Optional enables generic utility methods for wrapped values.  


## Combining covariance and contravariance

This example shows using both covariance and contravariance in a single  
method for maximum flexibility.  

```java
import java.util.function.Function;

void main() {

  List<Integer> integers = List.of(1, 2, 3);
  Function<Number, String> formatter = n -> "Value: " + n;

  var result = transform(integers, formatter);
  IO.println("Transformed: " + result);
}

<T, R> List<R> transform(
    List<? extends T> source,
    Function<? super T, ? extends R> mapper) {

  List<R> result = new ArrayList<>();
  for (T item : source) {
    result.add(mapper.apply(item));
  }
  return result;
}
```

The transform method demonstrates PECS on both parameters: source is covariant  
(producer), and mapper's input is contravariant (consumer) while its output is  
covariant (producer). This combination provides maximum flexibility, allowing  
the most general possible types for both input and transformation.  


## Wildcards with Set operations

This example demonstrates using wildcards for set operations like union and  
intersection.  

```java
void main() {

  Set<Integer> set1 = Set.of(1, 2, 3, 4);
  Set<Integer> set2 = Set.of(3, 4, 5, 6);

  var unionSet = union(set1, set2);
  var intersectionSet = intersection(set1, set2);

  IO.println("Union: " + unionSet);
  IO.println("Intersection: " + intersectionSet);
}

<T> Set<T> union(Set<? extends T> set1, Set<? extends T> set2) {

  Set<T> result = new HashSet<>(set1);
  result.addAll(set2);
  return result;
}

<T> Set<T> intersection(Set<? extends T> set1, Set<? extends T> set2) {

  Set<T> result = new HashSet<>(set1);
  result.retainAll(set2);
  return result;
}
```

Set operations use covariant wildcards to accept sets of T or subtypes. This  
allows combining sets of the same type or compatible types. The wildcards  
enable mathematical set operations while maintaining type safety and  
flexibility in the input types.  


## Wildcards with Map

This example shows using wildcards with Map for flexible key-value operations.  

```java
void main() {

  Map<String, Integer> scores = Map.of("Alice", 95, "Bob", 87);
  Map<String, Double> prices = Map.of("Apple", 1.99, "Banana", 0.59);

  printMap(scores);
  printMap(prices);
}

<K, V> void printMap(Map<? extends K, ? extends V> map) {

  map.forEach((key, value) ->
      IO.println(key + ": " + value)
  );
}
```

The printMap method uses covariant wildcards for both keys and values,  
allowing it to accept maps with different value types. This demonstrates how  
wildcards work with multi-parameter generic types. The method can read from  
any map regardless of the specific key and value types.  


## Generic method reference with wildcards

This example demonstrates using method references with bounded wildcards.  

```java
void main() {

  List<String> words = List.of("hello", "world", "java");
  List<Integer> numbers = List.of(1, 2, 3, 4, 5);

  process(words, String::toUpperCase);
  process(numbers, Object::toString);
}

<T, R> void process(List<T> list, Function<? super T, R> mapper) {

  list.stream()
      .map(mapper)
      .forEach(IO::println);
}
```

The process method accepts a Function with contravariant input type, allowing  
method references from supertypes. This enables using Object::toString on any  
type or String::toUpperCase on strings. Contravariant wildcards with method  
references provide flexible function composition.  


## Recursive type bounds

This example demonstrates using recursive type bounds with wildcards for  
type-safe comparisons.  

```java
record Person(String name, int age) implements Comparable<Person> {

  @Override
  public int compareTo(Person other) {
    return Integer.compare(this.age, other.age);
  }
}

void main() {

  List<Person> people = List.of(
      new Person("Alice", 30),
      new Person("Bob", 25),
      new Person("Charlie", 35)
  );

  var oldest = findMaxElement(people);
  IO.println("Oldest: " + oldest);
}

<T extends Comparable<? super T>> T findMaxElement(List<? extends T> list) {

  T max = list.getFirst();
  for (T element : list) {
    if (element.compareTo(max) > 0) {
      max = element;
    }
  }
  return max;
}
```

The recursive bound `T extends Comparable<? super T>` allows T to be compared  
with its supertypes, not just itself. This is more flexible than  
`Comparable<T>` and matches how many Java classes implement Comparable. The  
covariant list parameter adds additional flexibility for inputs.  


## Common pitfalls with wildcards

This example demonstrates common mistakes when working with wildcards and how  
to avoid them.  

```java
void main() {

  List<Integer> integers = List.of(1, 2, 3);

  // This demonstrates what NOT to do
  demonstratePitfalls(integers);

  // Correct approach
  demonstrateCorrectUsage(integers);
}

void demonstratePitfalls(List<? extends Number> numbers) {

  // Cannot add to covariant list (except null)
  // numbers.add(42);  // Compile error!
  // numbers.add(3.14);  // Compile error!

  Number first = numbers.getFirst();
  IO.println("Can read: " + first);
}

void demonstrateCorrectUsage(List<Integer> integers) {

  // Use specific type when you need to add
  List<Number> numbers = new ArrayList<>();
  numbers.addAll(integers);
  numbers.add(3.14);

  IO.println("Correct: " + numbers);
}
```

This example shows that you cannot add elements to a covariant list  
(`? extends T`) because the compiler doesn't know the exact subtype. The  
solution is to use the specific type when additions are needed, or use  
contravariant wildcards (`? super T`) for consumer scenarios. Understanding  
these limitations prevents compile errors.  


## Wildcard guidelines and best practices

This example demonstrates best practices for choosing between different  
wildcard types.  

```java
void main() {

  List<Integer> source = List.of(1, 2, 3, 4, 5);
  List<Number> target = new ArrayList<>();

  // Best practice: Use PECS
  copyWithPECS(source, target);
  IO.println("Copied: " + target);

  // Reading only: use extends
  double sum = sumNumbers(source);
  IO.println("Sum: " + sum);

  // Writing only: use super
  List<Object> objects = new ArrayList<>();
  addNumbers(objects);
  IO.println("Objects: " + objects);
}

<T> void copyWithPECS(List<? extends T> src, List<? super T> dest) {

  dest.addAll(src);
}

double sumNumbers(List<? extends Number> numbers) {

  return numbers.stream()
      .mapToDouble(Number::doubleValue)
      .sum();
}

void addNumbers(List<? super Integer> list) {

  for (int i = 1; i <= 5; i++) {
    list.add(i);
  }
}
```

This example codifies best practices: use `? extends T` for producers (reading),  
`? super T` for consumers (writing), and combine them when both operations are  
needed (PECS principle). Following these guidelines results in flexible,  
type-safe APIs that work naturally with Java's type system and provide maximum  
usability for clients.  
