# URI/URL


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
