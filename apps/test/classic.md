# Apps

## Classic console app

```
javac -d bin -cp lib\jsoup-1.17.2.jar src\main\java\com\example\Main.java src\main\java\com\example\utils\*.java
java -cp bin;lib\jsoup-1.17.2.jar com.example.Main https://webcode.me
jar -cvfm main.jar manifest.txt com ../lib
java -jar main.jar
```


```manifest
Manifest-Version: 1.0
Main-Class: com.example.Main
Class-Path: ../lib/jsoup-1.17.2.jar
```


```java
package com.example;

import com.example.utils.Scraper;
import com.example.utils.Utils;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        String word = new Utils().getRandomWord();
        System.out.println(word);

        String url = "https://example.com";

        if (args.length > 0) {
            url = args[0];
        }

        var scraper = new Scraper(url);

        String title = scraper.getTitle();
        System.err.println(title);
    }
}
```

```java
package com.example.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public final class Scraper {

    private final String url;

    public Scraper(String url) {
        this.url = url;
    }

    public String getTitle() throws IOException {

        Document doc = Jsoup.connect(url).get();
        return doc.title();
    }
}
```

```java
package com.example.utils;

import java.util.List;
import java.util.Random;

final public class Utils {

    List<String> words = List.of("sky", "town", "blue", "forest", "snake",
        "book", "pen", "cloud", "cup");

    public String getRandomWord() {

        int idx = new Random().nextInt(words.size());
        return words.get(idx);
    }
}
```

## Modern console app

`java --enable-preview --source 22 -cp lib\jsoup-1.17.2.jar Main.java https://root.cz`  

```java
import utils.Scraper;
import utils.Utils;
import java.io.IOException;

void main(String[] args) throws IOException {
    
    String word = new Utils().getRandomWord();
    System.out.println(word);

    String url = "https://example.com";

    if (args.length > 0) {
        url = args[0];
    }

    var scraper = new Scraper(url);

    String title = scraper.getTitle();
    System.err.println(title);
}
```


