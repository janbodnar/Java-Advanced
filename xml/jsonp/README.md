JSON-P

JSON Processing (JSON-P) is a Java API to process (for e.g. parse, generate, transform and query) 
JSON messages. It produces and consumes JSON text in a streaming fashion (similar to StAX API for XML) 
and allows to build a Java object model for JSON text using API classes (similar to DOM API for XML). 

Properties:

```
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
</properties>
```


Dependencies:

```
<dependencies>
    <dependency>
        <groupId>javax.json</groupId>
        <artifactId>javax.json-api</artifactId>
        <version>1.1</version>
    </dependency>

    <dependency>
        <groupId>org.glassfish</groupId>
        <artifactId>javax.json</artifactId>
        <version>1.1</version>
    </dependency>
</dependencies>
```
