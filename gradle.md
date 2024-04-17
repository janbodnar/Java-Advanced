# Gradle 


## Executable fat JAR

```gradle
application {
    // Define the main class for the application.
    mainClass = 'com.example.Main'
}

jar {
  manifest {
    attributes(
      'Main-Class': 'com.example.Main'
    )
  }
  from {
    configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
  }
}
```
