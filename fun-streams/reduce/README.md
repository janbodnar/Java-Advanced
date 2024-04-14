# The reduce operation






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
