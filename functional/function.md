# The Function inteface

```java
package com.zetcode;

import java.util.List;
import java.util.function.Function;

// Represents a function that accepts one argument and produces a result.
// <T> the type of the input to the function
// <R> the type of the result of the function

public class FunctionEx {

    public static void main(String[] args) {

        var vals = List.of(1, 2, 3, 4, 5);

        Function<List<Integer>, Integer> sumList = FunctionEx::doSumList;

        var mysum = sumList.apply(vals);
        System.out.println(mysum);
    }

    private static Integer doSumList(List<Integer> vals) {

        return vals.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}
```

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

## Categorizing age

```java
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.List;

Function<Integer, String> ageCategory = age -> {
    return switch (age) {
        case Integer n when n < 18 && n > 0 -> "minor";
        case Integer n when n >= 18 && n < 64 -> "adult";
        case Integer n when n > 64 -> "senior";
        default -> "n/a";
    };
};

Consumer<String> action = msg -> System.out.println(msg);

void main() {

    List<Integer> ages = List.of(11, 18, 17, 19, 21, 55, 86, 99, 43, 65, 63);
    ages.stream().map(age -> ageCategory.apply(age)).forEach(action);
}
```

## Filter 

`Predicate<Integer> isEven = n -> n % 2 == 0;`

```java
import java.util.function.Function;
import java.util.List;

Function<Integer, Boolean> isEven = n -> n % 2 == 0; 

void main() {

    var vals = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
    vals.stream().filter(isEven::apply).forEach(System.out::println);
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

Three functions

```java
import java.util.function.Function;

Function<Integer, Integer> fn1 = n -> n + 1;
Function<Integer, Integer> fn2 = n -> n * 2;
Function<Integer, Integer> fn3 = n -> n * n;

Function<Integer, Integer> cfn1 = fn1.andThen(fn2).andThen(fn3);
Function<Integer, Integer> cfn2 = fn1.compose(fn2).compose(fn3);

void main() {

    Integer res = cfn1.apply(10);
    System.out.println(res);

    Integer res2 = cfn2.apply(10);
    System.out.println(res2);
}
```

## Filter & sum

```java
import java.util.function.Function;
import java.util.List;

// Function<List<Integer>, Integer> sumEvens = vals -> vals.stream()
//         .filter(n -> n % 2 == 0)
//         .mapToInt(Integer::intValue)
//         .sum();

Function<List<Integer>, Integer> sumEvens = vals -> vals.stream()
        .filter(n -> n % 2 == 0)
        .reduce(0, Integer::sum);

void main() {

    List<Integer> vals = List.of(1, 2, 14, 22, 11, 24, 5, 21, 12, 22);

    Integer res = sumEvens.apply(vals);
    System.out.println(res);
}
```
