# Deprecations

## URI.toURL 

`URI.toURL` instead of `new URL`. 

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

## Locale constructors

Local constructors such as `new Locale("sk", "SK")` are deprecated since Java 19.  
We use `Locale.of("sk", "SK")` or a builder:  

```java
Locale persian = new Locale.Builder().setLanguage("fa").setRegion("IR").build();
```

```java
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

void main() {

    // NumberFormat nf = NumberFormat.getInstance(new Locale("sk", "SK"));
    NumberFormat nf = NumberFormat.getInstance(Locale.of("sk", "SK"));
    nf.setMaximumFractionDigits(3);

    try {
        Number num = nf.parse("150000,456");
        System.out.println(num.doubleValue());

    } catch (ParseException e) {
        e.printStackTrace(); // printStackTrace used only for debuggin/testing, not in production
    }
}
```



