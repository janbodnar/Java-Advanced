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

## equals

Two records are considered equal if a) they are of the same type (record class), b) all  
their fields have the same value (determined by calling equals on each corresponding field).  

Java automatically generates an `equals` method for records based on the record's fields.  
The default `equals` method considers all record fields for equality checks.  

```java
void main() {

    var u = new User("John Doe", "gardener");
    var u2 = new User("Roger Roe", "driver");
    var u3 = new User("John Doe", "gardener");

    if (u.equals(u2)) {
        System.out.println("users are equal");
    } else {
        System.out.println("users are not equal");
    }

    if (u.equals(u3)) {
        System.out.println("users are equal");
    } else {
        System.out.println("users are not equal");
    }
}

record User(String name, String occupation) {
}
```
