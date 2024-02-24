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

## Composition


```java
import java.util.function.Function;

void main() {

    Function<String, String> upperFun = val -> val.toUpperCase();
    Function<String, String> reverseFun = val -> {
        return new StringBuilder(val).reverse().toString();
    };

    var res = upperFun.compose(reverseFun).apply("falcon");
    System.out.println(res);
}
```

## compose vs andThen

The order in which the functions are applied may matter.  

```java
import java.util.function.Function;

void main() {

    Function<Double, Double> half = a -> a / 2;
    Function<Double, Double> square = a -> a * a;
    
    Function<Double, Double> squareAndThenHalf = square.andThen(half);
    Double res1 = squareAndThenHalf.apply(3d);  // First squares, then halves
    
    System.out.println(res1);

    Function<Double, Double> squareComposeHalf = square.compose(half);
    Double res2 = squareComposeHalf.apply(3d);  // First halves, then squares
    System.out.println(res2);
}
```

