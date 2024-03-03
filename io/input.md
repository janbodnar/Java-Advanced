# Input


## Scanner 

Read input from user from command line

```java
import java.io.IOException;
import java.util.Scanner;

void main() throws IOException {

    try (var scanner = new Scanner(System.in)) {

        System.out.print("Enter your name: ");
        String name = scanner.nextLine(); // Read a line of text (including spaces)

        String msg = STR."Hello \{name}!";
        System.out.println(msg);
    }
}
```
