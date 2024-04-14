# The map & flatMap methods 


## flatMap

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
