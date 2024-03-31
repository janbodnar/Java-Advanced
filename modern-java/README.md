# Modern Java


```
The Battle of Thermopylae was fought between an alliance of
Greek city-states, led by King Leonidas of Sparta, and the Persian Empire of
Xerxes I over the course of three days, during the second Persian invasion of
Greece. 
```

The `thermopylae.txt` file. 

## FileReader 

Always explicitly specify the encoding for `FileReader`. Fixed in Java 11.  
In previous versions, `FileReader` used platform defined encoding which is  
error-prone. 

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

void main() throws IOException {

    var fileName = "thermopylae.txt";

    try (var br = new BufferedReader(new FileReader(fileName,
            StandardCharsets.UTF_8))) {

        String line;
        while ((line = br.readLine()) != null) {

            System.out.println(line);
        }
    }
}
```
