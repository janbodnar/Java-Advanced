# FizzBuzz task

FizzBuzz Instructions

- Print numbers from 1 to N (typically N = 100).
- For numbers divisible by 3, print "Fizz" instead.
- For numbers divisible by 5, print "Buzz" instead.
- For numbers divisible by both 3 and 5, print "FizzBuzz".
- Otherwise, print the number itself.

```java
void main() {

    for (int i = 1; i <= 100; i++) {
        
        if (i % 3 == 0) {
            System.out.println("Fizz");
        } else if (i % 5 == 0) {
            System.out.println("Buzz");
        } else if (i % 3 == 0 && i % 5 == 5) {
            System.out.println("FizzBuzz");
        } else {
            System.err.println(i);
        }
    }
}
```
