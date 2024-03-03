# Reading text


## Files.ReadString

```java
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.nio.file.Files;
import java.nio.file.Paths;

void main() throws IOException {

    String data = Files.readString(Paths.get("russian-text.txt"),
            StandardCharsets.UTF_8);
    System.out.println(data);
}
```
