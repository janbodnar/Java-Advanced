# Gradle 


## Executable JAR

```gradle
plugins {
    id 'java'
    id 'application'
}

group = 'com.zetcode'
version = '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

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
