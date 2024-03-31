# Read text


```
The Battle of Thermopylae was fought between an alliance of
Greek city-states, led by King Leonidas of Sparta, and the Persian Empire of
Xerxes I over the course of three days, during the second Persian invasion of
Greece. 
```

This is the `thermopylae.txt` file.  

## FileReader with BufferedReader

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

## Word frequency

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.nio.charset.StandardCharsets;


void main() throws IOException {

    var fileName = "thermopylae.txt";
    var wordFreq = new HashMap<String, Integer>();

    try (var bufferedReader = new BufferedReader(new FileReader(fileName, 
            StandardCharsets.UTF_8))) {

        String line;
        while ((line = bufferedReader.readLine()) != null) {

            var words = line.split("\s");

            for (var word : words) {

                if (word.endsWith(",") || word.endsWith("?")
                    || word.endsWith("!") || word.endsWith(".")) {

                    word = word.substring(0, word.length()-1);
                }

                if (wordFreq.containsKey(word)) {
                    wordFreq.put(word, wordFreq.get(word) + 1);
                } else {
                    wordFreq.put(word, 1);
                }

            }
        }

        wordFreq.forEach((k, v) -> {
            System.out.printf("%s -> %d%n", k, v);
        });
    }
}
```

