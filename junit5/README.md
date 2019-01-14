For the examples, we need the `junit-jupiter-engine`, `junit-jupiter-params`, and a current version of `maven-surefire-plugin`.

Jupiter

~~~
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.3.2</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-params</artifactId>
    <version>5.3.2</version>
    <scope>test</scope>
</dependency>
~~~

Maven surefire plugin

~~~
<plugin>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0-M1</version>
</plugin>
~~~
