# The map & flatMap methods 

## map & array

```java
import java.util.Arrays;
import java.util.stream.IntStream;

void main() {
    var nums = IntStream.of(1, 2, 3, 4, 5, 6, 7, 8);
    var squares = nums.map(e -> e * e).toArray();

    System.out.println(Arrays.toString(squares));
}
```

## map & random 

```java
import java.util.Random;
import java.util.stream.Stream;

void main() {

    Stream.generate(new Random()::nextDouble)
        .map(e -> (e * 100))
        .mapToInt(Double::intValue)
        .limit(5)
        .forEach(System.out::println);
}
```

## map & strings

```java
import java.util.stream.Stream;

void main() {

    var words = Stream.of("cardinal", "pen", "coin", "globe");
    // words.map(e -> capitalize(e)).forEach(System.out::println);
    words.map(this::capitalize).forEach(System.out::println);
}

String capitalize(String word) {

    word = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    return word;
}
```

## map & CSV

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


void main() throws IOException {

    var lines = Files.readAllLines(Path.of("numbers.csv"));
    var vals = lines.stream().map(line -> Arrays.asList(line.split(",")))
        .flatMap(Collection::stream)
        .map(String::trim) 
        .mapToInt(Integer::valueOf)
        .boxed().collect(Collectors.toList());

    System.out.println(vals);
}
```

## map with record

```java
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

record User(String name, String occupation) {}

void main() {

    var users = List.of(new User("Peter", "programmer"),
            new User("Jane", "accountant"), new User("Robert", "teacher"),
            new User("Milan", "programmer"), new User("Jane", "designer"));

    var userNames = users.stream().map(user -> user.name()).sorted()
            .collect(Collectors.toList());
    System.out.println(userNames);

    var occupations = users.stream().map(user -> user.occupation())
            .sorted(Comparator.reverseOrder()).distinct().collect(Collectors.toList());

    System.out.println(occupations);
}
```

## flatMap

The `flatMap()` operation has the effect of applying a one-to-many transformation to the  
elements of the stream, and then flattening the resulting elements into a new stream.  

```java

import java.util.List;
import java.util.stream.Stream;


record User(String name, String occupation, List<Address> addresses) {}
record Address(String street, String city, String state) {}

void main() {

    // Use flatMap to transform User stream to a stream of Addresses
    Stream<Address> allAddresses = getUsers().flatMap(user -> user.addresses().stream());

    // You can further process the stream of addresses
    allAddresses.forEach(address -> System.out.println(address));

    System.out.println("---------------------------");

    getUsers().map(user -> user.occupation()).forEach(System.out::println);
}


Stream<User> getUsers() {
    User u1 = new User("John Doe", "gardener", List.of(
        new Address("123 Main St", "Anytown", "CA"),
        new Address("456 Elm St", "Springfield", "NY")
    ));
    User u2 = new User("Roger Roe", "driver", List.of(
        new Address("15 Brown St", "Anytown", "CA"),
        new Address("34 Sesame St", "Sunnyvale", "CA")
    ));

    return Stream.of(u1, u2);
}
```

## Flattening

```java
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

void main() {
    
    var vals1 = List.of(1, 2, 3);
    var vals2 = List.of(4, 5, 6);
    var vals3 = List.of(7, 8, 9);

    var vals = Stream.of(vals1, vals2, vals3)
            .collect(Collectors.toList());

    System.out.println("Before flattening : " + vals);

    var flattened = Stream.of(vals1, vals2, vals3).flatMap(Collection::stream)
            .collect(Collectors.toList());

    System.out.println("After flattening : " + flattened);
}
```
