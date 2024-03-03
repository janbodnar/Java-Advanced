# Reading text


## Files.ReadString

```java
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.nio.file.Files;
import java.nio.file.Paths;

void main() throws IOException {

    var fileName = "thermopylae.txt";

    String contents = Files.readString(Paths.get(fileName),
            StandardCharsets.UTF_8);
    System.out.println(contents);
}
```

## Files.lines

```java
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.nio.file.Files;
import java.nio.file.Paths;

void main() throws IOException {

    var fileName = "thermopylae.txt";

    Files.lines(Paths.get(fileName), 
        StandardCharsets.UTF_8).forEachOrdered(System.out::println);
}
```

## FileReader

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

void main() throws IOException {

    var fname = "thermopylae.txt";

    try (var br = new BufferedReader(new FileReader(fname, StandardCharsets.UTF_8))) {

        var sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {

            sb.append(line);
            sb.append(System.lineSeparator());
        }

        System.out.println(sb);
    }
}
```
