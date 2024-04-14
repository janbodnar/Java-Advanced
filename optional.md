# Optional 

`Optional` is a container object introduced which is used to represent a value  
that may or may not be present. An `Optional` object can either contain a  
non-null value (in which case it is considered present), or it can contain no  
value at all (in which case it is considered empty).  

The advantages of `Optional`:

- *Null Safety*: Optional forces you to handle the case where the value is  
  absent, reducing the risk of `NullPointerException`. It provides a clear and  
  explicit way to signal that a method might not always return a value.  

- *Improved Code Readability*: Optional can help in writing a neat code without   
  using too many null checks. By using `Optional`, we can specify alternate  
  values to return or alternate code to run. This makes the code more readable  
  because the facts which were hidden are now visible to the developer.  

- *Fluent Functional Programming Style*: We can use the `Optional` class to wrap  
  our data and avoid the classical null checks and some of the try-catch  
  blocks3. As a result, we’ll be able to chain method calls and have more  
  fluent, functional code.  

- *API Design*: Optional helps to create clear and robust APIs. It makes it  
  explicit to the API user that a returned value can be null, and forces them to  
  handle that case.  



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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;    
import java.util.Optional;    

void main() {

    List<String> words = new ArrayList<>();
    words.add("sky"); 
    words.add(null); 
    words.add("rock"); 
    words.add("atom"); 

    var res4 = getRandomElement(words);
    res4.ifPresentOrElse((e) -> {System.out.println(e.toUpperCase());}, () -> {System.out.println("N/A");});
}



public <T> Optional<T> getRandomElement(List<T> list) {
    Collections.shuffle(list);
    return Optional.ofNullable(list.get(0));
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
