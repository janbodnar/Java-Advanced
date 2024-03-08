# Optional 


## Example 

```java
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

void main() {

    var words = Arrays.asList("rock", null, "mountain",
            null, "falcon", "sky");

    for (int i = 0; i < 5; i++) {

        Optional<String> word = Optional.ofNullable(words.get(
                new Random().nextInt(words.size())));
        word.ifPresent(System.out::println);
    }
}
```

---


```java
import java.util.Optional;

void main() {

    if (getNullMessage().isPresent()) {
        System.out.println(getNullMessage().get());
    } else {
        System.out.println("n/a");
    }

    if (getEmptyMessage().isPresent()) {
        System.out.println(getEmptyMessage().get());
    } else {
        System.out.println("n/a");
    }

    if (getCustomMessage().isPresent()) {
        System.out.println(getCustomMessage().get());
    } else {
        System.out.println("n/a");
    }
}

Optional<String> getNullMessage() {
    return Optional.ofNullable(null);
}

Optional<String> getEmptyMessage() {
    return Optional.empty();
}

Optional<String> getCustomMessage() {
    return Optional.of("Hello there!");
}
```

## flatMap

If the result is already an `Optional` `flatMap` does not wrap it within an
additional `Optional`.

```java
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

void main() {

    Function<String, Optional<String>> upperCase = s -> Optional.of(s.toUpperCase());

    var words = Arrays.asList("rock", null, "mountain",
            null, "falcon", "sky");

    for (int i = 0; i < 5; i++) {

        Optional<String> word = Optional.ofNullable(words.get(i));

        var res = word.flatMap(upperCase);
        res.ifPresent(System.out::println);
    }
}
```

## isEmpty

```java
import java.util.Arrays;
import java.util.Optional;

void main() {

    var words = Arrays.asList("rock", null, "mountain",
            null, "falcon", "sky");

    for (int i = 0; i < 5; i++) {

        Optional<String> word = Optional.ofNullable(words.get(i));

        if (word.isEmpty()) {

            System.out.println("n/a");
        }

        word.ifPresent(System.out::println);
    }
}
```

## orElse

```java
import java.util.Optional;

void main() {

    Optional<String> word1 = Optional.of("sky");
    Optional<String> word2 = Optional.empty();

    System.out.println(word1.orElse("n/a"));
    System.out.println(word2.orElse("n/a"));
}
```


## ifPresentOrElse

```java
import java.util.Arrays;
import java.util.Optional;

void main() {

    var words = Arrays.asList("rock", null, "mountain",
            null, "falcon", "sky");

    for (String val : words) {

        Optional<String> word = Optional.ofNullable(val);

        word.ifPresentOrElse(System.out::println,
                () -> System.out.println("Invalid value"));
    }
}
```

## ifPresent & get

```java
import java.util.Arrays;
import java.util.Optional;

void main() {

    var words = Arrays.asList("rock", null, "mountain",
            null, "falcon", "sky");

    for (String s : words) {

        Optional<String> word = Optional.ofNullable(s);

        if (word.isPresent()) {

            System.out.println(word.get());
        } else {

            System.out.println("Invalid value");
        }
    }
}
```


```java
import java.util.Optional;
import java.util.function.Function;

// if the result is already an Optional flatMap
// does not wrap it within an additional Optional

void main() {

    Function<String, Optional<String>> upperCase = s -> (s == null) ? Optional.empty() : Optional.of(s.toUpperCase());

    Optional<String> word = Optional.of("falcon");

    Optional<Optional<String>> opt1 = word.map(upperCase);
    Optional<String> opt2 = word.flatMap(upperCase);

    opt1.ifPresent(s -> s.ifPresent(System.out::println));
    opt2.ifPresentOrElse(System.out::println,
            () -> System.out.println("Invalid value"));
}
```