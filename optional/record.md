# Records

A `record` provides a compact way to define an immutable data class introduced in Java 14.  
It simplifies creating classes that primarily hold data. 

Key features of records:

- Concise Syntax: Uses the record keyword for a cleaner declaration compared to traditional classes.
- Automatic Methods: Essential methods like constructors, getters (accessors), `equals`, `hashCode`,
  and `toString` are automatically generated.
- Immutable: Records are intended to be immutable, meaning their data cannot be changed after creation.

In essence, Java records are lightweight classes focused on data storage with built-in functionality.

## Simple example

```java
void main() {

    var u = new User("John Doe", "gardener");
    System.out.println(u);

    System.out.println(u.name());
    System.out.println(u.occupation());
}

record User(String name, String occupation) {
}
```
