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

## Comparison

```java
import java.util.Comparator;
import java.util.List;

void main() {

    var users = List.of(
            new User("John", "Doe", 1230),
            new User("Lucy", "Novak", 670),
            new User("Ben", "Walter", 2050),
            new User("Robin", "Brown", 2300),
            new User("Amy", "Doe", 1250),
            new User("Joe", "Draker", 1190),
            new User("Janet", "Doe", 980),
            new User("Albert", "Novak", 1930));

    var sorted = users.stream().sorted(Comparator.comparingInt(User::salary)).toList();
    System.out.println(sorted);
}

record User(String fname, String lname, int salary) {
}
```

## Comparable

```java
import java.util.List;

void main() {

    var users = List.of(
            new User("John", "Doe", 1230),
            new User("Lucy", "Novak", 670),
            new User("Ben", "Walter", 2050),
            new User("Robin", "Brown", 2300),
            new User("Amy", "Doe", 1250),
            new User("Joe", "Draker", 1190),
            new User("Janet", "Doe", 980),
            new User("Albert", "Novak", 1930));

    var sorted = users.stream().sorted().toList();
    System.out.println(sorted);
}

record User(String fname, String lname, int salary) implements Comparable<User> {
    @Override
    public int compareTo(User u) {
        return this.lname.compareTo(u.lname);
    }
}
```

## Filtering

```java
import java.util.List;
import java.util.stream.Collectors;

void main() {

    var users = List.of(
            User.of("John", "Doe", 1230),
            User.of("Lucy", "Novak", 670),
            User.of("Ben", "Walter", 2050),
            User.of("Robin", "Brown", 2300),
            User.of("Amy", "Doe", 1250),
            User.of("Joe", "Draker", 1190),
            User.of("Janet", "Doe", 980),
            User.of("Albert", "Novak", 1930));

    var filtered = users.stream().filter(e -> e.salary() > 2000)
            .collect(Collectors.toList());
    System.out.println(filtered);
}

record User(String fname, String lname, int salary) {
    public static User of(String fname, String lname, int salary) {
        return new User(fname, lname, salary);
    }
}
```


