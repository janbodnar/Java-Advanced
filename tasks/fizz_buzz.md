# FizzBuzz task

FizzBuzz Instructions

- Print numbers from 1 to N (typically N = 100).
- For numbers divisible by 3, print "Fizz" instead.
- For numbers divisible by 5, print "Buzz" instead.
- For numbers divisible by both 3 and 5, print "FizzBuzz".
- Otherwise, print the number itself.

## Solution 1

Using classic for loop and if/else statements. 

```java
void main() {

    for (int i = 1; i <= 100; i++) {

        if (i % 3 == 0 && i % 5 == 0) {
            System.out.println("FizzBuzz");
        } else if (i % 3 == 0) {
            System.out.println("Fizz");
        } else if (i % 5 == 0) {
            System.out.println("Buzz");
        } else {
            System.err.println(i);
        }
    }
}
```

## Solution 2

Using stream and switch expression. 

```java
void main() {

    IntStream.range(1, 101).forEach(this::doFizzBuzz);
}

void doFizzBuzz(int e) {

    switch (e) {
        case int n when n % 3 == 0 && n % 5 == 0 -> System.out.println("FizzBuzz");
        case int n when n % 3 == 0 -> System.out.println("Fizz");
        case int n when n % 5 == 0 -> System.out.println("Buzz");
        default -> System.out.println(e);
    }
}
```
