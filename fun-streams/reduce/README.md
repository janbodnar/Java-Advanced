# The reduce operation

## builtins

The `sum` and `count` are built-int reduction operations

```java
import java.util.Arrays;

void main() {

    int vals[] = {2, 4, 6, 8, 10, 12, 14, 16};

    int sum = Arrays.stream(vals).sum();
    System.out.printf("The sum of values: %d%n", sum);

    long n = Arrays.stream(vals).count();
    System.out.printf("The number of values: %d%n", n);
}
```

## reduce custom sum

```java
import java.util.List;
import java.util.Optional;

void main() {

    List<Integer> nums = List.of(1, 2, 3, 4, 5);
    Optional<Integer> res = nums.stream().reduce((a, b) -> a + b);
    res.ifPresent(System.out::println);    

    Optional<Integer> res3 = nums.stream().reduce(Integer::sum);
    res3.ifPresent(System.out::println);  
}
```

## couting chars

```java
import java.util.List;

void main(String[] args) {

    List<String> words = List.of("coin", "pet", "tennis", "sky");

    int n = words.stream().map(String::length).reduce(0, (l1, l2) -> l1 + l2);
    System.out.printf("The count of characters in the list: %d%n", n);
}
```

## reduce & Optional

```java
import java.util.stream.IntStream;

void main(String[] args) {
    IntStream.range(1, 10).reduce((x, y) -> x + y).ifPresent(s -> System.out.println(s));
    IntStream.range(1, 10).reduce(Integer::sum).ifPresent(s -> System.out.println(s));
    IntStream.range(1, 10).reduce(MyUtils::add2Ints).ifPresent(s -> System.out.println(s));
}


class MyUtils {
    public static int add2Ints(int num1, int num2) {
        return num1 + num2;
    }
}
```

## max price

```java
import java.util.List;
import java.util.Optional;


void main(String[] args) {

    List<Car> persons = List.of(Car.of("Skoda", 18544),
            Car.of("Volvo", 22344),
            Car.of("Fiat", 23650),
            Car.of("Renault", 19700));

    Optional<Car> car = persons.stream().reduce((c1, c2)
            -> c1.price() > c2.price() ? c1 : c2);

    car.ifPresent(System.out::println);
}

record Car(String name, int price) {

    public static Car of(String name, int price) {
        return new Car(name, price);
    }
}
```

## max age

```java
import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.util.ArrayList;
import java.util.List;

void main() {
        
    List<User> users = new ArrayList<>();
    users.add(new User("Frank", LocalDate.of(1979, 11, 23)));
    users.add(new User("Peter", LocalDate.of(1985, 1, 18)));
    users.add(new User("Lucy", LocalDate.of(2002, 5, 14)));
    users.add(new User("Albert", LocalDate.of(1996, 8, 30)));
    users.add(new User("Frank", LocalDate.of(1967, 10, 6)));
    
    int maxAge = users.stream().map(User::getAge)
            .reduce(0, (a1, a2) -> a1 > a2 ? a1 : a2);
    
    System.out.printf("The oldest user's age: %s%n", maxAge);
}

record User(String name, LocalDate dob) {
    public int getAge() {
        return dob.until(IsoChronology.INSTANCE.dateNow())
            .getYears();
    }
}
```
