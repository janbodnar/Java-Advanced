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
