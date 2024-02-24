# The Function inteface

## Calculating age

```java
import java.util.function.Function;
import java.util.function.Consumer;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

Function<LocalDate, Integer> findAge = dob -> Period.between(dob,
        LocalDate.now()).getYears();
Consumer<User> action = u -> System.out.println(STR."\{u.name} is \{findAge.apply(u.dob)} years old");

void main() {

    List<User> users = List.of(
            User.of("John Doe", "gardener", LocalDate.of(1956, 11, 12)),
            User.of("Roger Roe", "driver", LocalDate.of(1976, 11, 12)),
            User.of("John Doe", "teacher", LocalDate.of(1967, 1, 7)),
            User.of("John Doe", "gardener", LocalDate.of(1998, 5, 22)));

    users.stream().forEach(action);
}

record User(String name, String occupation, LocalDate dob) {
    static User of(String name, String occupation, LocalDate dob) {
        return new User(name, occupation, dob);
    }
}
```
