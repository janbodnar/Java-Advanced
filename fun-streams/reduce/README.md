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




## reduce example

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
