# Gradle 


## Executable JAR

*With plain JAR plugin:*

This is the default Gradle task for creating a JAR archive. It packages your  
compiled class files (*.class) and resources from your project into a single JAR  
file.  

It does not include any dependencies in the JAR. Your application needs the  
libraries it depends on to be installed separately on the target system.  

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
    mainClass = 'com.zetcode.Main'
}

jar {
    duplicatesStrategy(DuplicatesStrategy.EXCLUDE)
    manifest {
        attributes(
                'Main-Class': 'com.zetcode.Main'
        )
    }
    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    }
}
```

*With shadow JAR plugin:*  

It creates a fat JAR or shadow JAR, which is a single JAR containing your  
application code and all its transitive dependencies.  

It makes deployment easier as you only need to distribute the single JAR file  
containing everything your application needs. The JAR file will be larger due to  
the inclusion of all dependencies.  


```gradle
plugins {
    id 'java'
    id 'application'
    id 'io.github.goooler.shadow' version '8.1.8'
}

group = 'com.zetcode'
version = '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

application {
    mainClass = 'com.zetcode.Main'
}

dependencies {
    implementation 'org.eclipse.collections:eclipse-collections:11.1.0'
}

jar {
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    duplicatesStrategy(DuplicatesStrategy.EXCLUDE)
    exclude 'META-INF/*.RSA', 'META-INF/*.SF', 'META-INF/*.DSA'
    manifest {
        attributes(
                'Main-Class': 'com.zetcode.Main'
        )
    }
    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    }
}
```


