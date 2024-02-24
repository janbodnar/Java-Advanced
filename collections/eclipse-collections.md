# Eclipse collections

Place the two JARS into the `lib` and run on command line: `java -cp "lib\*" Main.java`

```java
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

void main() {

    MutableList<User> users = Lists.mutable.with(
            new User("Sally", "Smith"),
            new User("Ted", "Watson"),
            new User("Paul", "Smith"),
            new User("Mary", "Williams"));

    MutableList<User> smiths = users.select(user -> user.lastNameEquals("Smith"));
    String res = smiths.collect(User::lastName).makeString();
    System.out.println(res);
}

record User(String firstName, String lastName) {
    public boolean lastNameEquals(String name) {
        return name.equals(this.lastName);
    }
}
```

