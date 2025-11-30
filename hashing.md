# HashMap and HashSet in Java  

HashMap and HashSet are fundamental hash-based collections in Java that provide  
efficient data storage and retrieval. HashMap is a key-value store that maps keys  
to values using hash codes, while HashSet is a collection of unique elements that  
internally uses a HashMap with elements as keys and a constant dummy value.  

Both collections offer constant-time average performance for basic operations like  
`get`, `put`, `add`, and `remove`. This efficiency comes from their hash-based  
implementation, which uses an array of buckets to distribute entries based on  
their hash codes. Understanding how these collections work internally—including  
bucket distribution, collision handling, and the equals/hashCode contract—is  
essential for writing correct and performant Java code.  

Hash-based collections are widely used because they excel at lookups. Whether  
you need to check if an element exists, retrieve a value by key, or ensure  
uniqueness, these collections provide optimal performance. However, their  
correctness depends on proper implementation of `hashCode` and `equals` methods  
for objects used as keys or elements.  


## Buckets and Hashing  

Hash-based collections store entries in an array called a hash table. Each  
position in this array is called a bucket. When you add an entry, Java computes  
its hash code and uses it to determine which bucket should store the entry.  

The bucket index is typically calculated as: `index = hash(key) & (n - 1)`  
where `n` is the current capacity (always a power of 2). This bitwise AND  
operation is equivalent to modulo but faster.  

### Bucket Distribution Diagram  

```
Hash Table (capacity = 8)
┌───────┬───────┬───────┬───────┬───────┬───────┬───────┬───────┐
│   0   │   1   │   2   │   3   │   4   │   5   │   6   │   7   │
└───┬───┴───────┴───┬───┴───────┴───┬───┴───────┴───────┴───────┘
    │               │               │
    ▼               ▼               ▼
┌───────┐       ┌───────┐       ┌───────┐
│ Key A │       │ Key B │       │ Key C │
│ Val 1 │       │ Val 2 │       │ Val 3 │
└───┬───┘       └───────┘       └───┬───┘
    │                               │
    ▼                               ▼
┌───────┐                       ┌───────┐
│ Key D │ (collision)           │ Key E │ (collision)
│ Val 4 │                       │ Val 5 │
└───────┘                       └───────┘
```

### Hash Distribution Example  

This example demonstrates how hash codes are distributed across buckets.  

```java
void main() {

    var map = new HashMap<String, Integer>();

    var keys = List.of("apple", "banana", "cherry", "date", "elderberry");

    for (var key : keys) {
        int hash = key.hashCode();
        int bucket = hash & 7;  // Simulating capacity of 8
        IO.println(key + " -> hash: " + hash + ", bucket: " + bucket);
    }

    for (var key : keys) {
        map.put(key, key.length());
    }

    IO.println("Map contents: " + map);
}
```

This example shows how different keys are distributed across buckets based on  
their hash codes. The `& 7` operation simulates a hash table with 8 buckets  
(index 0-7). In practice, HashMap automatically manages the capacity and  
recalculates bucket indices as needed.  


## Collision Handling  

A collision occurs when two different keys hash to the same bucket. HashMap  
handles collisions using two strategies: linked lists and tree bins.  

### Linked List Collision Handling  

Initially, each bucket contains a linked list of entries. When a collision  
occurs, the new entry is added to the list at that bucket position.  

```
Bucket 3 with linked list collision handling:
┌───────────┐    ┌───────────┐    ┌───────────┐
│ Key: "a"  │───▶│ Key: "q"  │───▶│ Key: "z"  │───▶ null
│ Val: 1    │    │ Val: 17   │    │ Val: 26   │
└───────────┘    └───────────┘    └───────────┘
```

### Tree Bin Collision Handling (Java 8+)  

When a bucket exceeds 8 entries, the linked list is converted to a balanced  
red-black tree, improving worst-case performance from O(n) to O(log n).  

```
Bucket 3 with tree bin (red-black tree):
              ┌─────────────┐
              │ Key: "m"    │
              │ Val: 13     │
              └─────┬───────┘
           ┌────────┴────────┐
           ▼                 ▼
     ┌─────────────┐   ┌─────────────┐
     │ Key: "f"    │   │ Key: "t"    │
     │ Val: 6      │   │ Val: 20     │
     └─────────────┘   └─────────────┘
```

### Collision Demonstration  

This example demonstrates how collisions are handled internally.  

```java
record BadHashKey(String value) {
    @Override
    public int hashCode() {
        return 1;  // All keys go to the same bucket (bad practice!)
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BadHashKey other && 
               Objects.equals(this.value, other.value);
    }
}

void main() {

    var map = new HashMap<BadHashKey, Integer>();

    for (int i = 0; i < 10; i++) {
        map.put(new BadHashKey("key" + i), i);
    }

    IO.println("Map size: " + map.size());
    IO.println("All keys have same hashCode: " + 
               (new BadHashKey("a").hashCode() == new BadHashKey("b").hashCode()));

    long startTime = System.nanoTime();
    var result = map.get(new BadHashKey("key5"));
    long endTime = System.nanoTime();

    IO.println("Retrieved value: " + result);
    IO.println("Lookup time: " + (endTime - startTime) + " ns");
}
```

This example creates keys with identical hash codes, forcing all entries into  
the same bucket. While the map still functions correctly, performance degrades  
because every lookup must traverse the collision chain. In real applications,  
well-distributed hash codes are essential for maintaining O(1) average lookup.  


## Optimization Algorithms  

HashMap employs several optimization strategies to maintain performance as the  
collection grows.  

### Load Factor and Capacity  

| Property | Default Value | Description |  
|----------|---------------|-------------|  
| Initial Capacity | 16 | Number of buckets at creation |  
| Load Factor | 0.75 | Ratio of entries to capacity before resize |  
| Threshold | capacity × loadFactor | Number of entries that trigger resize |  
| Resize Factor | 2× | Capacity doubles on each resize |  

### Rehashing and Resizing  

When the number of entries exceeds the threshold, HashMap doubles its capacity  
and rehashes all entries to redistribute them across the new bucket array.  

```
Before resize (capacity = 4, threshold = 3):
┌───┬───┬───┬───┐
│ A │ B │ C │ D │  ← 4 entries, exceeds threshold
└───┴───┴───┴───┘

After resize (capacity = 8, threshold = 6):
┌───┬───┬───┬───┬───┬───┬───┬───┐
│ A │   │ B │   │ C │   │ D │   │  ← entries redistributed
└───┴───┴───┴───┴───┴───┴───┴───┘
```

### Resize Demonstration  

This example shows how capacity changes affect performance.  

```java
void main() {

    var mapSmall = new HashMap<Integer, String>(2, 0.75f);
    var mapLarge = new HashMap<Integer, String>(1024, 0.75f);

    int entries = 1000;

    long startSmall = System.nanoTime();
    for (int i = 0; i < entries; i++) {
        mapSmall.put(i, "value" + i);
    }
    long timeSmall = System.nanoTime() - startSmall;

    long startLarge = System.nanoTime();
    for (int i = 0; i < entries; i++) {
        mapLarge.put(i, "value" + i);
    }
    long timeLarge = System.nanoTime() - startLarge;

    IO.println("Small initial capacity time: " + timeSmall / 1000 + " µs");
    IO.println("Large initial capacity time: " + timeLarge / 1000 + " µs");
    IO.println("Ratio: " + (double) timeSmall / timeLarge);
}
```

Starting with a small initial capacity causes multiple resize operations as the  
map grows, each requiring rehashing of all entries. When you know the approximate  
size in advance, specifying an adequate initial capacity avoids these costly  
resize operations and improves performance.  


### Treeification Threshold  

When a bucket contains more than 8 entries, HashMap converts the linked list  
to a red-black tree. This improves worst-case lookup from O(n) to O(log n).  

| Threshold | Value | Description |  
|-----------|-------|-------------|  
| TREEIFY_THRESHOLD | 8 | Entries before converting to tree |  
| UNTREEIFY_THRESHOLD | 6 | Entries before reverting to list |  
| MIN_TREEIFY_CAPACITY | 64 | Minimum capacity for treeification |  

### Performance Comparison  

| Scenario | Linked List | Red-Black Tree |  
|----------|-------------|----------------|  
| Average lookup | O(n/2) | O(log n) |  
| Worst case | O(n) | O(log n) |  
| Memory overhead | Lower | Higher |  
| When used | ≤ 8 entries | > 8 entries |  


## Equals and HashCode Contract  

The equals/hashCode contract is a fundamental agreement that ensures correct  
behavior in hash-based collections. Violating this contract leads to subtle  
bugs that are difficult to diagnose.  

### Contract Rules  

1. **Consistency**: Multiple invocations of `hashCode` on the same object must  
   return the same integer, provided the object hasn't changed.  

2. **Equality implies same hash**: If `a.equals(b)` returns `true`, then  
   `a.hashCode()` must equal `b.hashCode()`.  

3. **Same hash does not imply equality**: Two objects with the same hash code  
   are not required to be equal (collisions are allowed).  

4. **Reflexivity**: `x.equals(x)` must return `true`.  

5. **Symmetry**: If `x.equals(y)` returns `true`, then `y.equals(x)` must also  
   return `true`.  

6. **Transitivity**: If `x.equals(y)` and `y.equals(z)` return `true`, then  
   `x.equals(z)` must also return `true`.  

7. **Null handling**: `x.equals(null)` must return `false`.  

### Contract Visualization  

```
                    ┌─────────────────────────────────┐
                    │         Contract Rules          │
                    └─────────────────────────────────┘
                                    │
        ┌───────────────────────────┼───────────────────────────┐
        │                           │                           │
        ▼                           ▼                           ▼
┌───────────────┐         ┌───────────────┐         ┌───────────────┐
│   If equal    │         │  Hash codes   │         │   equals()    │
│   objects     │         │  can collide  │         │   properties  │
│               │         │               │         │               │
│ a.equals(b)   │         │  a.hash == b  │         │  Reflexive    │
│      ↓        │         │  doesn't mean │         │  Symmetric    │
│ a.hash ==     │         │  a.equals(b)  │         │  Transitive   │
│ b.hash        │         │               │         │  Null-safe    │
└───────────────┘         └───────────────┘         └───────────────┘
```


## Why the Contract is Needed  

The equals/hashCode contract exists because hash-based collections use a  
two-step lookup process:  

1. **Hash code lookup**: Find the bucket using `hashCode()`  
2. **Equality check**: Search within the bucket using `equals()`  

If these methods are inconsistent, lookups fail even when objects exist.  

### Broken Contract Scenarios  

```
Scenario: Equal objects with different hash codes

Object A: hashCode() = 42, equals(B) = true
Object B: hashCode() = 99, equals(A) = true

HashMap stores A in bucket 42
HashMap.get(B) searches bucket 99 → NOT FOUND!

Result: Lost entry despite equal keys
```

### Contract Violation Example  

This example demonstrates what happens when the contract is violated.  

```java
class BrokenKey {
    private String id;
    private int counter = 0;

    public BrokenKey(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id.hashCode() + counter++;  // Changes on each call!
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BrokenKey other && 
               Objects.equals(this.id, other.id);
    }
}

void main() {

    var map = new HashMap<BrokenKey, String>();
    var key = new BrokenKey("mykey");

    map.put(key, "my value");
    IO.println("Immediately after put: " + map.get(key));
    IO.println("Second get attempt: " + map.get(key));
    IO.println("Third get attempt: " + map.get(key));
    IO.println("Map contains key? " + map.containsKey(key));
    IO.println("Map size: " + map.size());
}
```

The `BrokenKey` class violates the contract because `hashCode` returns a  
different value each time it's called. After adding the key, subsequent lookups  
fail because the new hash code doesn't match the original bucket location.  
The entry exists but is unreachable—a particularly insidious bug.  


### Mutable Key Problem  

Another common violation occurs when mutable objects are used as keys and  
their state changes after insertion.  

```java
class MutablePerson {
    public String name;

    public MutablePerson(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MutablePerson other && 
               Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return "Person[" + name + "]";
    }
}

void main() {

    var set = new HashSet<MutablePerson>();
    var person = new MutablePerson("Alice");

    set.add(person);
    IO.println("Before mutation - contains Alice? " + 
               set.contains(new MutablePerson("Alice")));

    person.name = "Bob";  // Mutating the key!

    IO.println("After mutation - contains Alice? " + 
               set.contains(new MutablePerson("Alice")));
    IO.println("After mutation - contains Bob? " + 
               set.contains(new MutablePerson("Bob")));
    IO.println("Set contents: " + set);
    IO.println("Set size: " + set.size());

    set.add(person);  // Adding "same" object again
    IO.println("After re-add - Set size: " + set.size());
}
```

When `name` changes from "Alice" to "Bob", the object's hash code changes but  
its bucket location doesn't. Now the object can't be found with either name,  
and adding it again creates a duplicate. This demonstrates why keys should be  
immutable or why mutable fields should not be used in `hashCode` and `equals`.  


## Correct Implementation  

A proper implementation follows the contract and uses consistent fields.  

### Using Records (Recommended)  

Java records automatically generate correct `hashCode` and `equals` methods.  

```java
record Person(String name, int age) {}

void main() {

    var set = new HashSet<Person>();

    var alice1 = new Person("Alice", 30);
    var alice2 = new Person("Alice", 30);

    set.add(alice1);
    set.add(alice2);  // Same as alice1, won't be added

    IO.println("Set size: " + set.size());
    IO.println("alice1 equals alice2: " + alice1.equals(alice2));
    IO.println("Same hash: " + (alice1.hashCode() == alice2.hashCode()));
    IO.println("Contains alice2? " + set.contains(alice2));
}
```

Records are the simplest way to create proper value-based classes. They  
automatically implement `hashCode`, `equals`, and `toString` based on all  
components. Since record components are final, the immutability requirement  
is also satisfied.  


### Manual Implementation  

When you need a class instead of a record, implement both methods carefully.  

```java
class Product {
    private final String sku;
    private final String name;
    private final double price;

    public Product(String sku, String name, double price) {
        this.sku = sku;
        this.name = name;
        this.price = price;
    }

    public String getSku() { return sku; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public int hashCode() {
        return Objects.hash(sku, name, price);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product other)) return false;
        return Objects.equals(sku, other.sku) &&
               Objects.equals(name, other.name) &&
               Double.compare(price, other.price) == 0;
    }

    @Override
    public String toString() {
        return "Product[sku=%s, name=%s, price=%.2f]".formatted(sku, name, price);
    }
}

void main() {

    var inventory = new HashMap<Product, Integer>();

    var laptop = new Product("LAP001", "Laptop", 999.99);
    var sameLaptop = new Product("LAP001", "Laptop", 999.99);

    inventory.put(laptop, 50);
    inventory.put(sameLaptop, 75);  // Overwrites previous entry

    IO.println("Inventory size: " + inventory.size());
    IO.println("Laptop stock: " + inventory.get(laptop));
    IO.println("Same laptop stock: " + inventory.get(sameLaptop));
}
```

The `Product` class follows best practices: all fields are `final` for  
immutability, `Objects.hash` combines field hash codes consistently, and  
`equals` uses `Objects.equals` for null-safe comparison. Note the use of  
`Double.compare` for floating-point equality instead of `==`.  


## Best Practices  

Follow these guidelines for robust hash-based collections:  

### Implementation Guidelines  

| Practice | Description |  
|----------|-------------|  
| Use records | Let Java generate correct implementations |  
| Make fields final | Prevents mutation after insertion |  
| Use Objects.hash | Combines multiple fields correctly |  
| Use Objects.equals | Handles null values safely |  
| Override both methods | Never override one without the other |  
| Use same fields | hashCode and equals must use identical fields |  

### Objects.hash and Objects.equals  

These utility methods simplify correct implementation.  

```java
class Employee {
    private final String id;
    private final String department;
    private final String email;

    public Employee(String id, String department, String email) {
        this.id = id;
        this.department = department;
        this.email = email;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, department, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Employee other)) return false;
        return Objects.equals(id, other.id) &&
               Objects.equals(department, other.department) &&
               Objects.equals(email, other.email);
    }
}

void main() {

    var employees = new HashSet<Employee>();

    employees.add(new Employee("E001", "Engineering", "john@example.com"));
    employees.add(new Employee("E002", "Marketing", "jane@example.com"));
    employees.add(new Employee("E001", "Engineering", "john@example.com"));

    IO.println("Employee count: " + employees.size());

    var lookup = new Employee("E001", "Engineering", "john@example.com");
    IO.println("Contains E001? " + employees.contains(lookup));
}
```

`Objects.hash` creates a composite hash code from multiple values, handling  
nulls correctly. `Objects.equals` performs null-safe equality comparison.  
Together, they provide a concise and correct implementation pattern that  
avoids common pitfalls like NullPointerException.  


### When to Override  

| Scenario | Override? | Reason |  
|----------|-----------|--------|  
| Value semantics needed | Yes | Two objects with same data should be equal |  
| Used as Map key | Yes | Correct lookup requires proper hash/equals |  
| Used in Set | Yes | Uniqueness based on content, not reference |  
| Entity with database ID | Partially | Often only ID is used for equality |  
| Singleton or enum | No | Reference equality is correct |  
| Temporary/internal class | Usually no | Reference equality often sufficient |  


### HashSet Implementation Detail  

HashSet internally uses HashMap with elements as keys. Understanding this  
helps explain HashSet behavior.  

```java
void main() {

    var set = new HashSet<String>();

    set.add("apple");
    set.add("banana");
    set.add("apple");  // Duplicate, returns false

    IO.println("Set: " + set);
    IO.println("Size: " + set.size());

    boolean added1 = set.add("cherry");
    boolean added2 = set.add("cherry");

    IO.println("First add returned: " + added1);
    IO.println("Second add returned: " + added2);

    for (var element : set) {
        int hash = element.hashCode();
        IO.println(element + " hash: " + hash);
    }
}
```

Internally, HashSet stores elements as keys in a HashMap with a dummy constant  
value (PRESENT). The `add` method returns `true` if the element was new, `false`  
if it already existed. This explains why HashSet operations have the same  
performance characteristics as HashMap.  


## HashMap Internal Structure  

Understanding the internal structure helps with debugging and optimization.  

```java
import java.lang.reflect.Field;

void main() throws Exception {

    var map = new HashMap<String, Integer>();

    for (int i = 0; i < 20; i++) {
        map.put("key" + i, i);
    }

    Field tableField = HashMap.class.getDeclaredField("table");
    tableField.setAccessible(true);
    Object[] table = (Object[]) tableField.get(map);

    IO.println("Bucket array length: " + table.length);
    IO.println("Map size: " + map.size());
    IO.println("Load factor threshold: " + (int)(table.length * 0.75));

    int nonEmptyBuckets = 0;
    for (Object bucket : table) {
        if (bucket != null) nonEmptyBuckets++;
    }
    IO.println("Non-empty buckets: " + nonEmptyBuckets);
    IO.println("Average entries per bucket: " + 
               (double) map.size() / nonEmptyBuckets);
}
```

This example uses reflection to inspect HashMap internals. The bucket array  
grows in powers of 2, starting at 16. When size exceeds the threshold  
(capacity × 0.75), the array doubles. Good hash distribution keeps entries  
spread across buckets, maintaining O(1) average operations.  


## Performance Characteristics  

| Operation | HashMap | HashSet | TreeMap | LinkedHashMap |  
|-----------|---------|---------|---------|---------------|  
| get/contains | O(1) avg | O(1) avg | O(log n) | O(1) avg |  
| put/add | O(1) avg | O(1) avg | O(log n) | O(1) avg |  
| remove | O(1) avg | O(1) avg | O(log n) | O(1) avg |  
| Iteration | O(n + capacity) | O(n + capacity) | O(n) | O(n) |  
| Memory | Medium | Medium | Higher | Higher |  
| Order | None | None | Sorted | Insertion |  


## Summary  

HashMap and HashSet are essential Java collections that provide efficient  
storage and retrieval through hashing. Key concepts to remember:  

**Buckets and Hashing**: Entries are distributed across buckets using hash  
codes. Good distribution minimizes collisions and maintains O(1) performance.  

**Collision Handling**: When collisions occur, entries are stored in linked  
lists (≤8 entries) or red-black trees (>8 entries) within the same bucket.  

**Optimization**: HashMap automatically resizes when load exceeds 75%, doubling  
capacity and rehashing entries. Treeification converts high-collision buckets  
to balanced trees for O(log n) worst-case performance.  

**Equals/HashCode Contract**: Equal objects must have equal hash codes. Both  
methods should use the same fields. Violation causes lost entries, duplicate  
keys, and unpredictable behavior.  

**Best Practices**: Use records for automatic correct implementation. Make  
fields final. Use `Objects.hash` and `Objects.equals` for utility. Never  
override only one of `equals` or `hashCode`.  

Understanding these internals enables you to write efficient code, choose  
appropriate initial capacities, implement proper key classes, and debug  
collection-related issues effectively.  
