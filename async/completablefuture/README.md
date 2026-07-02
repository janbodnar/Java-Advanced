# CompletableFuture


## Async addition

```java
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;

void complexCalculation() {

    try {
        Thread.sleep(Duration.of(1, ChronoUnit.SECONDS));
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        e.printStackTrace();
    }
}

int a() {
    complexCalculation();
    return 4;
}

int b() {
    complexCalculation();
    return 5;
}

void main() {

    long startTime = System.currentTimeMillis();

    // int res = a() + b();
    var a = CompletableFuture.supplyAsync(this::a);
    var b = CompletableFuture.supplyAsync(this::b);
    var res = a.thenCombine(b, (x, y) -> x + y).join();
    
    System.out.println(res);
    System.out.println("Elapsed time: " + (System.currentTimeMillis() - startTime) + "ms");

}
```
