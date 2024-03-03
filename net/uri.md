# URI/URL

The `java.net.URI` is old-school code. The `HttpClient` is the modern solution.  


## openStream

```java
import java.net.URL;
import java.net.URI;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

void main() throws Exception {

    URL url = URI.create("https://webcode.me").toURL();

    try (InputStream is = url.openStream()) {

        byte[] data = is.readAllBytes();
        String msg = new String(data, StandardCharsets.UTF_8);

        System.out.println(msg);
    }
}
```

## HttpURLConnection

Set timeouts.  

```java
import java.net.URL;
import java.net.URI;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

void main() throws Exception {

    URL url = URI.create("https://webcode.me").toURL();

    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setConnectTimeout(5000);
    con.setReadTimeout(5000);
    con.setRequestMethod("GET");

    con.connect();

    try (InputStream is = con.getInputStream()) {

        byte[] data = is.readAllBytes();
        String msg = new String(data, StandardCharsets.UTF_8);

        System.out.println(msg);
    }
}
```

## HEAD request

```java
import java.net.URL;
import java.net.URI;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

void main() throws Exception {

    URL url = URI.create("https://webcode.me").toURL();

    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("HEAD");
    con.connect();

    Map<String, List<String>> fields = con.getHeaderFields();

    fields.forEach((k, vals) -> {

        System.out.println(k);

        for (var e: vals) {
            System.out.println(e);
        }

        System.out.println();
    });
}
```

## Download image

```java
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

void main(String[] args) throws IOException {

    var imageUrl = "https://something.com/favicon.ico";
    var destinationFile = "favicon.ico";

    var url = URI.create(imageUrl).toURL();

    try (var is = url.openStream();
            var fos = new FileOutputStream(destinationFile)) {

        byte[] buf = new byte[1024];
        int noOfBytes;

        while ((noOfBytes = is.read(buf)) != -1) {

            fos.write(buf, 0, noOfBytes);
        }
    }
}
```

---

```java
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

void main(String[] args) throws IOException {

    var urlFile = "http://httpbin.org/image/jpeg";

    try (InputStream in = URI.create(urlFile).toURL().openStream()) {

        Files.copy(in, Paths.get("jackal.jpg"));
    }
}
```

## InputStreamReader

```java
import java.net.URL;
import java.net.URI;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;

void main() throws IOException {

    URL url = URI.create("https://webcode.me").toURL();

    try (var isr = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
            var br = new BufferedReader(isr)) {

        String line;
        while ((line = br.readLine()) != null) {

            System.out.println(line);
        }
    }
}
```


