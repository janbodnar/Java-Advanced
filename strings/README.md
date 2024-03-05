# Strings 

## Text blocks

```java
void main() {

    String name = "John Doe";
    int age = 34;
    String occupation = "gardener";

    var msg = """
            %s is %d years old
            he is an %s
            """.formatted(name, age, occupation);

    System.out.println(msg);

}
```

## Pyramid

```java
void main() {

    String val = "0";
    String empty = " ";

    int len = 30;

    for (int i = 1; i < len; i += 2) {

        System.out.printf("%s%s%n", empty.repeat(len - i / 2), val.repeat(i));
    }
}
```

```bash
$ java Main.java

                              0
                             000
                            00000
                           0000000
                          000000000
                         00000000000
                        0000000000000
                       000000000000000
                      00000000000000000
                     0000000000000000000
                    000000000000000000000
                   00000000000000000000000
                  0000000000000000000000000
                 000000000000000000000000000
                00000000000000000000000000000
```

## Blank

The string is blank if it is empty or contains only white space. 

```java
import java.util.List;

void main() {

    var data = List.of("sky", "\n\n", "  ", "blue", "\t\t", "", "sky");

    for (int i = 0; i < data.size(); i++) {

        var e = data.get(i);

        if (e.isBlank()) {
            System.out.printf("element with index %d is blank%n", i);
        } else {

            System.out.println(data.get(i));
        }
    }
}
```

## Upper & lower 

```java
void main() {

    var word1 = "Cherry";

    var u_word1 = word1.toUpperCase();
    var l_word1 = u_word1.toLowerCase();

    System.out.println(u_word1);
    System.out.println(l_word1);

    var word2 = "Čerešňa";

    var u_word2 = word2.toUpperCase();
    var l_word2 = u_word2.toLowerCase();

    System.out.println(u_word2);
    System.out.println(l_word2);
}
```

## Pattern split 

```java
import java.util.regex.Pattern;
import java.util.stream.Collectors;

void main() {

    var phoneNumber = "202-555-0154";

    var output = Pattern.compile("-")
            .splitAsStream(phoneNumber)
            .collect(Collectors.toList());

    output.forEach(System.out::println);
}
```

---

```java
import java.util.Arrays;

void main() {

    var input = " wood, falcon\t, sky, forest\n";
    var output = input.trim().split("\\s*,\\s*");

    Arrays.stream(output).forEach(System.out::println);
}
```

## Bytes and strings

```java
import java.nio.charset.StandardCharsets;
import java.util.Base64;

void main() {


    byte[] data = { (byte) 0xc4, (byte) 0x8d, (byte) 0x65, (byte) 0x72,
            (byte) 0x65, (byte) 0xc5, (byte) 0xa1, (byte) 0xc5, (byte) 0x88, (byte) 0x61 };

    var text = new String(data, StandardCharsets.UTF_8);
    System.out.println(text);


    String str = "čerešňa";
    byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
    var hexString = new StringBuilder();

    for (byte b : bytes) {

        String hex = Integer.toHexString(0xff & b);
        
        if (hex.length() == 1) {
            hexString.append('0');
        }

        hexString.append(STR."0x\{hex}, ");
    }

    int len = hexString.length();
    hexString.delete(len-2, len-1); // delete last space and comma

    System.out.println(hexString.toString());
}
```
