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

## Files.readAllLines

Read all lines into a list.  

```java
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

void main() throws IOException {

    var fname = "thermopylae.txt";

    List<String> lines = Files.readAllLines(Paths.get(fname), StandardCharsets.UTF_8);

    for (String line : lines) {

        System.out.println(line);
    }
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

        String line;
        while ((line = br.readLine()) != null) {

            System.out.println(line);
        }

    }
}
```

## FileReader with CharBuffer

```java
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

void main() {

    String fname = "thermopylae.txt";

    try (var reader = new FileReader(fname, StandardCharsets.UTF_8)) {
        // Create a CharBuffer with a reasonable size (adjust as needed)
        var buffer = CharBuffer.allocate(1024);

        int charsRead;
        StringBuilder content = new StringBuilder();

        // Read characters in chunks using read(CharBuffer)
        while ((charsRead = reader.read(buffer)) != -1) {
            // Flip the buffer to prepare it for reading
            buffer.flip();
            // Append the read characters to the StringBuilder
            content.append(buffer);
            // Clear the buffer for the next read
            buffer.clear();
        }

        System.out.println(content.toString());
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

## Scanner 

```java

import java.io.IOException;
import java.io.File;
import java.util.Scanner;

void main() throws IOException {

    var fname = "thermopylae.txt";

    try (var scanner = new Scanner(new File(fname))) {

        while (scanner.hasNext()) {

            String line = scanner.nextLine();
            System.out.println(line);
        }
    }
}
```

