# Java Class Loading and Custom ClassLoaders

Class loading is one of the fundamental mechanisms of the Java Virtual Machine  
(JVM) that enables the dynamic loading of classes at runtime. Unlike languages  
where all code is linked at compile time, Java loads classes on demand as they  
are needed during program execution. This lazy loading strategy reduces memory  
footprint and startup time while enabling powerful features like plugins,  
hot deployment, and runtime code generation.  

The class loading mechanism is essential for understanding how Java applications  
work at a deeper level. Every time you instantiate an object, call a static  
method, or reference a class constant, the JVM's class loading subsystem ensures  
that the required class is available in memory. This process involves locating  
the bytecode, verifying its correctness, and preparing it for execution.  

For advanced Java development, understanding class loaders is crucial. They form  
the foundation for application servers, OSGi containers, plugin systems, and  
module frameworks. Misunderstanding class loading can lead to subtle bugs like  
`ClassCastException` between seemingly identical classes, memory leaks from  
unreleased class loaders, and security vulnerabilities from improperly isolated  
code.  

## Class Loading Process Overview  

The JVM loads classes through a well-defined three-phase process:  

1. **Loading**: The class loader reads the `.class` file (bytecode) from the  
   file system, network, or other sources and creates a `Class` object in memory.  

2. **Linking**: This phase has three sub-phases:  
   - **Verification**: Ensures the bytecode is valid and doesn't violate JVM  
     security constraints  
   - **Preparation**: Allocates memory for static variables and initializes them  
     to default values  
   - **Resolution**: Converts symbolic references to direct references  

3. **Initialization**: Executes static initializers and initializes static  
   variables with their defined values. This happens lazily, only when the class  
   is first actively used.  

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           Class Loading Lifecycle                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│    .class file                                                              │
│         │                                                                   │
│         ▼                                                                   │
│    ┌─────────┐    ┌─────────────────────────────────────┐    ┌───────────┐ │
│    │ Loading │───▶│              Linking                │───▶│Initialize │ │
│    └─────────┘    │ ┌────────┐ ┌────────┐ ┌──────────┐ │    └───────────┘ │
│                   │ │Verify  │▶│Prepare │▶│ Resolve  │ │                   │
│                   │ └────────┘ └────────┘ └──────────┘ │                   │
│                   └─────────────────────────────────────┘                   │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Class Loader Hierarchy  

Java uses a hierarchical class loader architecture with a parent delegation model.  
Each class loader has a parent (except the Bootstrap ClassLoader), and class  
loading requests are first delegated to the parent before the class loader  
attempts to load the class itself.  

| Class Loader | Description | Location |
|--------------|-------------|----------|
| Bootstrap ClassLoader | Loads core Java classes | `JAVA_HOME/lib` |
| Platform ClassLoader | Loads platform modules | Java modules |
| Application ClassLoader | Loads application classes | Classpath |
| Custom ClassLoader | User-defined loaders | Custom sources |

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         Class Loader Hierarchy                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│                        ┌───────────────────────┐                            │
│                        │ Bootstrap ClassLoader │ (Native code)              │
│                        │   java.lang, etc.     │                            │
│                        └───────────┬───────────┘                            │
│                                    │                                        │
│                                    ▼                                        │
│                        ┌───────────────────────┐                            │
│                        │ Platform ClassLoader  │ (Java 9+)                  │
│                        │   java.sql, etc.      │                            │
│                        └───────────┬───────────┘                            │
│                                    │                                        │
│                                    ▼                                        │
│                        ┌───────────────────────┐                            │
│                        │Application ClassLoader│                            │
│                        │   Your application    │                            │
│                        └───────────┬───────────┘                            │
│                                    │                                        │
│                                    ▼                                        │
│                        ┌───────────────────────┐                            │
│                        │ Custom ClassLoader(s) │                            │
│                        │   Plugins, modules    │                            │
│                        └───────────────────────┘                            │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```


## Examining class loaders

This example demonstrates how to examine the class loader hierarchy by printing  
the class loaders for various classes in the system.  

```java
void main() {

    // Get the class loader of our main class
    var appLoader = getClass().getClassLoader();
    println("Application class loader: " + appLoader);

    // Get the parent class loaders
    var platformLoader = appLoader.getParent();
    println("Platform class loader: " + platformLoader);

    // Bootstrap class loader returns null
    var bootstrapLoader = platformLoader.getParent();
    println("Bootstrap class loader: " + bootstrapLoader);

    // Different classes are loaded by different class loaders
    println("\nClass loader examples:");
    println("String.class loader: " + String.class.getClassLoader());
    println("ArrayList.class loader: " + java.util.ArrayList.class.getClassLoader());
    println("This class loader: " + getClass().getClassLoader());
}
```

This example shows the three-tier class loader hierarchy in Java. The `String`  
class returns `null` for its class loader because it's loaded by the Bootstrap  
ClassLoader, which is implemented in native code. The application class is  
loaded by the Application ClassLoader, and we can traverse up the parent chain  
to see the Platform ClassLoader.  


## Finding where a class was loaded from

This example shows how to determine the physical location from which a class  
was loaded using the `ProtectionDomain` and `CodeSource` APIs.  

```java
void main() {

    // Find where core Java classes are loaded from
    var stringLocation = String.class.getProtectionDomain()
        .getCodeSource();
    println("String loaded from: " + stringLocation);

    // Find where ArrayList is loaded from
    var listLocation = java.util.ArrayList.class.getProtectionDomain()
        .getCodeSource();
    println("ArrayList loaded from: " + listLocation);

    // Find where this class is loaded from
    var thisLocation = getClass().getProtectionDomain()
        .getCodeSource();
    println("This class loaded from: " + 
        (thisLocation != null ? thisLocation.getLocation() : "unknown"));

    // Find where a third-party library class would be loaded from
    try {
        var objectMapper = Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
        var location = objectMapper.getProtectionDomain().getCodeSource();
        println("ObjectMapper loaded from: " + location.getLocation());
    } catch (ClassNotFoundException e) {
        println("Jackson not available in classpath");
    }
}
```

Understanding where classes are loaded from is essential for debugging classpath  
issues and version conflicts. The `getCodeSource` method returns the location  
of the JAR file or directory from which the class was loaded. For bootstrap  
classes, this may return null since they are loaded from the JVM's internal  
modules.  


## Loading classes dynamically with Class.forName

The `Class.forName` method is the standard way to load classes dynamically at  
runtime. This is useful when the class name is determined at runtime.  

```java
void main() {

    // Load a class by name
    try {
        var listClass = Class.forName("java.util.ArrayList");
        println("Loaded: " + listClass.getName());
        println("Simple name: " + listClass.getSimpleName());
        println("Package: " + listClass.getPackageName());
        
        // Create an instance using reflection
        @SuppressWarnings("unchecked")
        var list = (java.util.List<String>) listClass.getDeclaredConstructor()
            .newInstance();
        list.add("Hello");
        list.add("World");
        println("List contents: " + list);
        
    } catch (Exception e) {
        println("Failed to load class: " + e.getMessage());
    }

    // Load a class without initializing it
    try {
        var mapClass = Class.forName("java.util.HashMap", false, 
            ClassLoader.getSystemClassLoader());
        println("\nLoaded without initialization: " + mapClass.getName());
    } catch (ClassNotFoundException e) {
        println("Class not found: " + e.getMessage());
    }
}
```

The `Class.forName` method has two variants. The simple version loads and  
initializes the class, which means static initializers run immediately. The  
three-argument version allows you to specify whether to initialize the class  
and which class loader to use, giving you more control over the loading process.  


## Understanding class initialization order

This example demonstrates when static initialization occurs during class loading  
and how the order affects program behavior.  

```java
class FirstClass {
    static {
        System.out.println("FirstClass static block executed");
    }
    static String value = initValue();
    
    static String initValue() {
        System.out.println("FirstClass.initValue() called");
        return "First";
    }
}

class SecondClass {
    static {
        System.out.println("SecondClass static block executed");
    }
    static String value = "Second";
}

void main() {

    println("Before any class access...\n");

    // Accessing FirstClass triggers its initialization
    println("Accessing FirstClass.value:");
    var first = FirstClass.value;
    println("Got: " + first + "\n");

    // Accessing SecondClass triggers its initialization
    println("Accessing SecondClass.value:");
    var second = SecondClass.value;
    println("Got: " + second + "\n");

    // Loading without initialization
    println("Loading ThirdClass without initialization:");
    try {
        Class.forName("ThirdClass", false, 
            ClassLoader.getSystemClassLoader());
        println("ThirdClass loaded but not initialized");
    } catch (ClassNotFoundException e) {
        println("ThirdClass not found (expected)");
    }
}

class ThirdClass {
    static {
        System.out.println("ThirdClass static block - should not appear!");
    }
}
```

Static initializers run in the order they appear in the source code, but only  
when the class is first actively used. Using `Class.forName` with the second  
parameter set to `false` loads the class without triggering initialization,  
which can be useful when you need to inspect a class without running its static  
code.  


## Parent delegation model

This example illustrates how class loaders delegate to their parents before  
attempting to load a class themselves.  

```java
void main() {

    // Create a custom class loader that logs delegation
    var loggingLoader = new ClassLoader(getClass().getClassLoader()) {
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            println("Attempting to load: " + name);
            
            // First, check if already loaded
            var loaded = findLoadedClass(name);
            if (loaded != null) {
                println("  -> Already loaded");
                return loaded;
            }
            
            // Delegate to parent first (standard behavior)
            try {
                println("  -> Delegating to parent");
                return getParent().loadClass(name);
            } catch (ClassNotFoundException e) {
                println("  -> Parent couldn't load, trying ourselves");
                throw e;
            }
        }
    };

    // Try loading a standard library class
    println("Loading String class:");
    try {
        var stringClass = loggingLoader.loadClass("java.lang.String");
        println("Loaded by: " + stringClass.getClassLoader() + "\n");
    } catch (ClassNotFoundException e) {
        println("Failed: " + e.getMessage());
    }

    // Try loading ArrayList
    println("Loading ArrayList class:");
    try {
        var listClass = loggingLoader.loadClass("java.util.ArrayList");
        println("Loaded by: " + listClass.getClassLoader());
    } catch (ClassNotFoundException e) {
        println("Failed: " + e.getMessage());
    }
}
```

The parent delegation model ensures that core Java classes are always loaded by  
the Bootstrap ClassLoader, preventing malicious code from replacing system  
classes. When a class loader receives a request to load a class, it first asks  
its parent. Only if the parent cannot load the class does the child attempt to  
load it.  


## Simple custom class loader from bytes

This example demonstrates creating a minimal custom class loader that loads a  
class from a byte array representation.  

```java
void main() {

    // A simple class represented as bytecode
    // This is the compiled bytecode for:
    // public class DynamicClass { 
    //     public String getMessage() { return "Hello from dynamic class!"; }
    // }
    
    var customLoader = new ClassLoader(getClass().getClassLoader()) {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if ("DynamicClass".equals(name)) {
                // In a real scenario, you would read bytes from a file or network
                // Here we're simulating with a placeholder
                throw new ClassNotFoundException(
                    "DynamicClass bytecode not available - this is a demonstration");
            }
            throw new ClassNotFoundException(name);
        }
    };

    println("Custom class loader created: " + customLoader);
    println("Parent: " + customLoader.getParent());
    println("System class loader: " + ClassLoader.getSystemClassLoader());

    // Demonstrate that standard classes still work through delegation
    try {
        var stringClass = customLoader.loadClass("java.lang.String");
        println("\nLoaded String via custom loader: " + 
            stringClass.getClassLoader());
    } catch (ClassNotFoundException e) {
        println("Unexpected error: " + e.getMessage());
    }

    // Try to load a custom class
    try {
        customLoader.loadClass("DynamicClass");
    } catch (ClassNotFoundException e) {
        println("\nExpected: " + e.getMessage());
    }
}
```

Custom class loaders extend the `ClassLoader` class and override the `findClass`  
method to specify how classes are located and loaded. The `defineClass` method  
is then used to convert the raw bytecode into a `Class` object. This pattern is  
the foundation for plugin systems and dynamic code loading.  


## File-based custom class loader

This example shows a practical custom class loader that loads classes from a  
specific directory on the file system.  

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FileSystemClassLoader extends ClassLoader {
    private final Path classesDir;

    FileSystemClassLoader(Path classesDir, ClassLoader parent) {
        super(parent);
        this.classesDir = classesDir;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        var classFile = classesDir.resolve(name.replace('.', '/') + ".class");
        
        if (!Files.exists(classFile)) {
            throw new ClassNotFoundException(name + " not found in " + classesDir);
        }

        try {
            var bytes = Files.readAllBytes(classFile);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Error reading " + classFile, e);
        }
    }

    @Override
    public String toString() {
        return "FileSystemClassLoader[" + classesDir + "]";
    }
}

void main() {

    // Create a class loader for a specific directory
    var classesDir = Path.of("/tmp/custom-classes");
    var loader = new FileSystemClassLoader(classesDir, getClass().getClassLoader());

    println("Created: " + loader);
    println("Looking for classes in: " + classesDir);

    // Attempt to load a class (will fail if directory doesn't exist)
    try {
        var customClass = loader.loadClass("com.example.CustomClass");
        println("Loaded: " + customClass.getName());
    } catch (ClassNotFoundException e) {
        println("Class not found: " + e.getMessage());
        println("(This is expected if the directory doesn't contain classes)");
    }

    // Standard library classes still work via delegation
    try {
        var listClass = loader.loadClass("java.util.ArrayList");
        println("\nArrayList loaded by: " + listClass.getClassLoader());
    } catch (ClassNotFoundException e) {
        println("Error: " + e.getMessage());
    }
}
```

A file-based class loader reads `.class` files from a directory structure that  
mirrors the package hierarchy. The `defineClass` method converts the raw bytes  
into a `Class` object. This pattern is commonly used for loading plugins or  
modules from a specific location outside the main classpath.  


## URL class loader for loading classes from JARs

The `URLClassLoader` is a built-in class loader that can load classes from URLs,  
including JAR files and directories.  

```java
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

void main() {

    // Create URLs for class loading
    try {
        // Create a URL for a local directory (convert path to URL)
        var localDir = Path.of("/tmp/plugin-libs").toUri().toURL();
        println("Local directory URL: " + localDir);

        // Create a URLClassLoader with multiple sources
        var urls = new URL[] { localDir };
        
        try (var urlLoader = new URLClassLoader(urls, getClass().getClassLoader())) {
            println("Created URLClassLoader with " + urls.length + " URL(s)");
            println("Parent: " + urlLoader.getParent());

            // List the URLs this loader will search
            println("\nSearch URLs:");
            for (var url : urlLoader.getURLs()) {
                println("  - " + url);
            }

            // Try loading a class from the URL
            try {
                var pluginClass = urlLoader.loadClass("com.example.Plugin");
                println("\nLoaded plugin: " + pluginClass.getName());
            } catch (ClassNotFoundException e) {
                println("\nPlugin class not found (expected if directory is empty)");
            }

            // Standard classes still work
            var stringClass = urlLoader.loadClass("java.lang.String");
            println("String loaded by: " + stringClass.getClassLoader());
        }

    } catch (Exception e) {
        println("Error: " + e.getMessage());
    }
}
```

`URLClassLoader` is the most commonly used class loader for dynamically loading  
external code. It supports loading from JAR files, directories, and even remote  
URLs. The loader searches its URLs in order and uses parent delegation to  
ensure core classes are loaded from the system class loader.  


## Class loader isolation

This example demonstrates how different class loaders provide isolation,  
allowing the same class to be loaded multiple times as different `Class` objects.  

```java
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

void main() {

    try {
        var classPath = Path.of(".").toUri().toURL();
        
        // Create two independent class loaders with no parent relationship
        var loader1 = new URLClassLoader(new URL[]{classPath}, null);
        var loader2 = new URLClassLoader(new URL[]{classPath}, null);

        println("Loader 1: " + loader1);
        println("Loader 2: " + loader2);
        println();

        // Both loaders can load the same class name
        // but they will be different Class objects
        try {
            var class1 = loader1.loadClass("java.util.ArrayList");
            var class2 = loader2.loadClass("java.util.ArrayList");

            println("Class 1: " + class1.getName());
            println("Class 1 loader: " + class1.getClassLoader());
            println("Class 2: " + class2.getName());
            println("Class 2 loader: " + class2.getClassLoader());

            println("\nAre they the same class object? " + (class1 == class2));
            println("Are they equals? " + class1.equals(class2));

        } catch (ClassNotFoundException e) {
            println("Error: " + e.getMessage());
        }

        loader1.close();
        loader2.close();

    } catch (Exception e) {
        println("Error: " + e.getMessage());
    }
}
```

When two class loaders load the same class, they create distinct `Class` objects.  
Objects created by one loader cannot be cast to the same class loaded by another  
loader, even if the bytecode is identical. This isolation is the foundation for  
plugin systems and OSGi containers that need to load different versions of the  
same library.  


## Context class loader

The thread context class loader is a class loader associated with each thread,  
commonly used by frameworks to load resources and classes.  

```java
void main() throws InterruptedException {

    // Get the context class loader of the current thread
    var contextLoader = Thread.currentThread().getContextClassLoader();
    println("Main thread context loader: " + contextLoader);
    println("Class class loader: " + getClass().getClassLoader());
    println("Are they the same? " + (contextLoader == getClass().getClassLoader()));

    // Create a thread with a custom context class loader
    var customLoader = new ClassLoader(contextLoader) {
        @Override
        public String toString() {
            return "CustomContextLoader";
        }
    };

    var thread = new Thread(() -> {
        var loader = Thread.currentThread().getContextClassLoader();
        println("\nWorker thread context loader: " + loader);
        
        // Use context class loader to load a resource
        var resource = loader.getResource("java/lang/String.class");
        println("Found String.class resource: " + (resource != null));
    });

    thread.setContextClassLoader(customLoader);
    thread.start();
    thread.join();

    println("\nContext loader is useful for SPI and framework code");
    println("It allows libraries to load classes in the caller's context");
}
```

The context class loader allows framework code to load classes in the context  
of the calling application. This is essential for Service Provider Interface  
(SPI) mechanisms like JDBC drivers, where the driver code needs to load classes  
using the application's class loader rather than its own.  


## Loading resources with class loaders

Class loaders are used not only for loading classes but also for loading  
resources like configuration files, images, and other assets.  

```java
import java.io.BufferedReader;
import java.io.InputStreamReader;

void main() {

    var loader = getClass().getClassLoader();

    // Find a resource on the classpath
    var resourceUrl = loader.getResource("java/lang/Object.class");
    println("Object.class URL: " + resourceUrl);

    // Try to find a properties file
    var propsUrl = loader.getResource("application.properties");
    if (propsUrl != null) {
        println("Found application.properties: " + propsUrl);
    } else {
        println("application.properties not found in classpath");
    }

    // Get system resources (searches all class loaders)
    println("\nSearching for all manifest files:");
    try {
        var manifests = loader.getResources("META-INF/MANIFEST.MF");
        int count = 0;
        while (manifests.hasMoreElements() && count < 3) {
            println("  " + manifests.nextElement());
            count++;
        }
        if (count == 3) {
            println("  ... (more manifests exist)");
        }
    } catch (Exception e) {
        println("Error: " + e.getMessage());
    }

    // Read a resource as a stream
    var stream = loader.getResourceAsStream("java/lang/String.class");
    if (stream != null) {
        println("\nString.class first bytes: ");
        try {
            var bytes = stream.readNBytes(8);
            for (var b : bytes) {
                System.out.printf("%02X ", b);
            }
            println("");
            stream.close();
        } catch (Exception e) {
            println("Error reading: " + e.getMessage());
        }
    }
}
```

Resources are loaded using the same delegation model as classes. The `getResource`  
method returns a URL to the resource, while `getResourceAsStream` returns an  
input stream for reading the content. The `getResources` method returns all  
matching resources from all class loaders in the hierarchy.  


## Reloading classes at runtime

This example demonstrates the concept of class reloading, which requires  
creating new class loader instances to load updated versions of classes.  

```java
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

void main() throws Exception {

    var classPath = Path.of("/tmp/hot-reload").toUri().toURL();
    println("Hot reload directory: " + classPath);

    // Simulate class reloading by creating new class loaders
    for (int version = 1; version <= 3; version++) {
        println("\n--- Loading version " + version + " ---");
        
        // Create a new class loader for each version
        var loader = new URLClassLoader(
            new URL[]{classPath}, 
            getClass().getClassLoader()
        );

        try {
            // Attempt to load a class
            var serviceClass = loader.loadClass("com.example.Service");
            println("Loaded: " + serviceClass.getName());
            println("Class loader: " + serviceClass.getClassLoader());
            println("Identity hash: " + System.identityHashCode(serviceClass));

            // Create an instance
            var service = serviceClass.getDeclaredConstructor().newInstance();
            println("Instance: " + service);

        } catch (ClassNotFoundException e) {
            println("Service class not found (place a compiled class in the directory)");
        } finally {
            loader.close();
        }
    }

    println("\nNote: Each version uses a different class loader instance.");
    println("The old class remains in memory until its loader is garbage collected.");
}
```

Java does not support true class reloading where an existing `Class` object is  
modified. Instead, hot reloading is achieved by creating a new class loader and  
loading a fresh copy of the class. The old class remains in memory until its  
class loader is garbage collected, which requires all references to classes  
loaded by that loader to be released.  


## Module system interaction

In Java 9+, the module system (JPMS) interacts with class loading through  
module layers and module class loaders.  

```java
void main() {

    // Get information about the module system
    var thisModule = getClass().getModule();
    println("This class module: " + thisModule);
    println("Is named module: " + thisModule.isNamed());
    println("Module name: " + thisModule.getName());

    // Examine java.base module
    var baseModule = String.class.getModule();
    println("\njava.base module: " + baseModule);
    println("Module layer: " + baseModule.getLayer());

    // List some packages in java.base
    println("\nSome packages in java.base:");
    baseModule.getPackages().stream()
        .filter(p -> p.startsWith("java.lang") || p.startsWith("java.util"))
        .limit(5)
        .forEach(p -> println("  - " + p));

    // Get the boot layer
    var bootLayer = ModuleLayer.boot();
    println("\nBoot layer: " + bootLayer);
    println("Modules in boot layer: " + bootLayer.modules().size());

    // Check module exports
    println("\njava.base exports java.lang to: " + 
        (baseModule.isExported("java.lang") ? "everyone" : "no one"));

    // Class loading in modular environment
    var loader = String.class.getClassLoader();
    println("\nString class loader: " + loader);
    println("This suggests bootstrap classes are in unnamed modules for the loader");
}
```

The Java Platform Module System introduces a new layer of encapsulation on top  
of class loading. Modules define which packages are exported and which are  
internal. Class loaders work alongside the module system, with each module layer  
potentially having its own class loader configuration.  


## Service Provider Interface (SPI) loading

This example shows how the `ServiceLoader` mechanism uses class loaders to  
discover and load service implementations dynamically.  

```java
import java.nio.charset.spi.CharsetProvider;
import java.util.ServiceLoader;
import java.util.spi.LocaleServiceProvider;

void main() {

    // ServiceLoader uses the context class loader
    var contextLoader = Thread.currentThread().getContextClassLoader();
    println("Context class loader: " + contextLoader);

    // Load CharsetProvider services
    println("\nCharset Providers:");
    var charsetProviders = ServiceLoader.load(CharsetProvider.class);
    for (var provider : charsetProviders) {
        println("  - " + provider.getClass().getName());
        println("    Loaded by: " + provider.getClass().getClassLoader());
    }

    // Load with a specific class loader
    println("\nLocale Service Providers (via system loader):");
    var localeProviders = ServiceLoader.load(
        LocaleServiceProvider.class, 
        ClassLoader.getSystemClassLoader()
    );
    int count = 0;
    for (var provider : localeProviders) {
        println("  - " + provider.getClass().getName());
        count++;
        if (count >= 3) {
            println("  ... (more providers exist)");
            break;
        }
    }

    // Demonstrate lazy loading nature of ServiceLoader
    println("\nServiceLoader loads services lazily on iteration");
    println("Services are discovered via META-INF/services files");
}
```

The `ServiceLoader` uses the thread context class loader to discover service  
implementations listed in `META-INF/services` files. This mechanism enables  
loose coupling between service interfaces and their implementations, allowing  
applications to discover implementations at runtime without hard-coded  
dependencies.  


## Security and class loading

Class loaders play a crucial role in Java's security model by defining security  
domains and enforcing access controls.  

```java
void main() {

    // Examine protection domains
    var stringPD = String.class.getProtectionDomain();
    println("String ProtectionDomain: " + stringPD);

    var thisPD = getClass().getProtectionDomain();
    println("\nThis class ProtectionDomain: " + thisPD);

    // Check code source
    var thisCodeSource = thisPD.getCodeSource();
    println("Code source: " + thisCodeSource);

    if (thisCodeSource != null) {
        println("Location: " + thisCodeSource.getLocation());
        var certs = thisCodeSource.getCertificates();
        println("Certificates: " + (certs != null ? certs.length : 0));
    }

    // Check permissions
    var permissions = thisPD.getPermissions();
    println("\nPermissions type: " + permissions.getClass().getSimpleName());

    // Security best practices for custom class loaders
    println("\nSecurity Best Practices:");
    println("1. Always use parent delegation");
    println("2. Validate bytecode before loading");
    println("3. Set appropriate protection domains");
    println("4. Don't expose class loader to untrusted code");
    println("5. Consider using SecurityManager (deprecated in Java 17+)");

    // Modern approach: use module system for encapsulation
    println("\nModern approach: Use JPMS for encapsulation instead of SecurityManager");
}
```

Each class is associated with a `ProtectionDomain` that defines its code source  
and permissions. Custom class loaders should properly assign protection domains  
to loaded classes. While the `SecurityManager` is deprecated, class loader  
security remains important through proper code isolation and module system  
encapsulation.  


## Memory leak prevention

Improper handling of class loaders can cause memory leaks. This example shows  
how to properly manage class loader lifecycle.  

```java
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

void main() {

    println("Demonstrating class loader memory management\n");

    // Create and properly close a class loader
    WeakReference<ClassLoader> loaderRef;

    try {
        var url = Path.of("/tmp").toUri().toURL();
        var loader = new URLClassLoader(new URL[]{url});
        loaderRef = new WeakReference<>(loader);

        println("Created class loader: " + loader);
        println("Weak reference: " + loaderRef.get());

        // Use the loader
        try {
            loader.loadClass("java.lang.String");
        } catch (ClassNotFoundException e) {
            // Expected for non-delegating loader
        }

        // Close the loader when done
        loader.close();
        println("Closed class loader");

    } catch (Exception e) {
        println("Error: " + e.getMessage());
        return;
    }

    // Clear strong reference and suggest GC
    System.gc();
    Thread.yield();

    println("\nAfter GC:");
    println("Weak reference: " + loaderRef.get());

    // Common memory leak causes
    println("\nCommon Class Loader Memory Leak Causes:");
    println("1. ThreadLocal variables not cleaned up");
    println("2. Shutdown hooks not removed");
    println("3. Static references to loaded classes");
    println("4. JDBC drivers not deregistered");
    println("5. Logging frameworks holding references");
    println("6. JMX MBeans not unregistered");
}
```

Class loader memory leaks occur when class loaders cannot be garbage collected  
due to lingering references. Common causes include thread-local variables,  
shutdown hooks, and static fields in loaded classes. Always close `URLClassLoader`  
instances when done and clean up any resources that might hold references to  
loaded classes.  


## Handling ClassNotFoundException and NoClassDefFoundError

Understanding the difference between these two exceptions is crucial for  
debugging class loading issues.  

```java
void main() {

    println("ClassNotFoundException vs NoClassDefFoundError\n");

    // ClassNotFoundException: explicit loading fails
    println("1. ClassNotFoundException:");
    println("   Thrown when Class.forName or ClassLoader.loadClass fails");
    try {
        Class.forName("com.nonexistent.MyClass");
    } catch (ClassNotFoundException e) {
        println("   Caught: " + e.getClass().getSimpleName());
        println("   Message: " + e.getMessage());
    }

    // NoClassDefFoundError: implicit loading fails
    println("\n2. NoClassDefFoundError:");
    println("   Thrown when JVM cannot find a class needed during execution");
    println("   Usually caused by a class that was available at compile time");
    println("   but missing at runtime");

    // Simulate scenario that could cause NoClassDefFoundError
    println("\n3. Common causes:");
    println("   - Missing JAR file at runtime");
    println("   - Class path misconfiguration");
    println("   - Static initializer failure");

    // ExceptionInInitializerError
    println("\n4. ExceptionInInitializerError:");
    println("   Thrown when a static initializer fails");

    // Demonstrate with a failing static initializer
    try {
        Class.forName("FailingClass");
    } catch (ExceptionInInitializerError e) {
        println("   Caught: " + e.getClass().getSimpleName());
        println("   Cause: " + e.getCause());
    } catch (ClassNotFoundException e) {
        println("   FailingClass not defined in this example");
    }

    // LinkageError hierarchy
    println("\n5. Error Hierarchy:");
    println("   Throwable");
    println("   └── Error");
    println("       └── LinkageError");
    println("           ├── NoClassDefFoundError");
    println("           ├── ClassCircularityError");
    println("           ├── ClassFormatError");
    println("           └── IncompatibleClassChangeError");
}

// Uncomment to demonstrate ExceptionInInitializerError
// class FailingClass {
//     static {
//         if (true) throw new RuntimeException("Static init failed!");
//     }
// }
```

`ClassNotFoundException` is a checked exception thrown when explicit class  
loading fails. `NoClassDefFoundError` is an error thrown when the JVM cannot  
find a class that was available at compile time. The key difference is that  
`ClassNotFoundException` occurs during dynamic loading, while `NoClassDefFoundError`  
typically indicates a deployment or classpath configuration problem.  


## Custom class loader for encrypted classes

This example demonstrates a class loader that could decrypt classes before  
loading them, useful for protecting proprietary code.  

```java
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

class EncryptedClassLoader extends ClassLoader {
    private final byte[] key;

    EncryptedClassLoader(String keyString, ClassLoader parent) {
        super(parent);
        this.key = keyString.getBytes();
    }

    // Simulated decryption method
    byte[] decrypt(byte[] encrypted) throws Exception {
        var keySpec = new SecretKeySpec(key, 0, 16, "AES");
        var cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(encrypted);
    }

    // Simulated encryption method for demonstration
    byte[] encrypt(byte[] data) throws Exception {
        var keySpec = new SecretKeySpec(key, 0, 16, "AES");
        var cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // In a real implementation, you would:
        // 1. Read encrypted bytecode from file/network
        // 2. Decrypt the bytecode
        // 3. Call defineClass with decrypted bytes
        throw new ClassNotFoundException(
            "Encrypted class loading demonstration - no encrypted classes available");
    }
}

void main() {

    println("Encrypted Class Loader Demonstration\n");

    // In production, retrieve key from secure storage (e.g., environment variable,
    // key management service, or hardware security module)
    var key = System.getenv("CLASS_ENCRYPTION_KEY");
    if (key == null || key.length() < 16) {
        // Fallback for demonstration only - never use hardcoded keys in production
        key = "DemoKeyOnly16byt";
        println("WARNING: Using demo key - set CLASS_ENCRYPTION_KEY env var in production\n");
    }

    var loader = new EncryptedClassLoader(key, ClassLoader.getSystemClassLoader());

    println("Created encrypted class loader");
    println("Key length: " + key.length() + " characters\n");

    // Demonstrate encryption/decryption
    try {
        var original = "Hello, World!".getBytes();
        println("Original: " + new String(original));

        var encrypted = loader.encrypt(original);
        println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));

        var decrypted = loader.decrypt(encrypted);
        println("Decrypted: " + new String(decrypted));

    } catch (Exception e) {
        println("Encryption error: " + e.getMessage());
    }

    // Try loading an encrypted class
    println("\nAttempting to load encrypted class:");
    try {
        loader.loadClass("com.secret.ProtectedClass");
    } catch (ClassNotFoundException e) {
        println("Expected: " + e.getMessage());
    }

    println("\nUse Cases:");
    println("- Protecting proprietary algorithms");
    println("- License enforcement");
    println("- Code obfuscation");
    println("\nSecurity Note: Always use secure key management in production!");
}
```

An encrypted class loader decrypts bytecode before passing it to `defineClass`.  
This technique can protect proprietary code from reverse engineering. However,  
it's not foolproof since the decryption key must be available at runtime, and  
determined attackers can still extract the decrypted bytecode from memory.  
Always use secure key management (environment variables, key management services,  
or hardware security modules) rather than hardcoding keys in source code.  


## Parallel class loading

Java supports parallel class loading to improve performance when loading many  
classes concurrently.  

```java
import java.util.concurrent.CountDownLatch;

class ParallelCapableLoader extends ClassLoader {
    static {
        // Register this class loader as parallel capable
        ClassLoader.registerAsParallelCapable();
    }

    ParallelCapableLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        println("  Finding: " + name + " on " + Thread.currentThread().getName());
        
        // Simulate some loading time
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        throw new ClassNotFoundException(name);
    }
}

void main() throws InterruptedException {

    println("Parallel Class Loading Demonstration\n");

    var loader = new ParallelCapableLoader(getClass().getClassLoader());
    var numThreads = 5;
    var latch = new CountDownLatch(numThreads);

    println("Starting " + numThreads + " concurrent class loading attempts:\n");

    for (int i = 0; i < numThreads; i++) {
        var className = "com.example.Class" + i;
        new Thread(() -> {
            try {
                loader.loadClass(className);
            } catch (ClassNotFoundException e) {
                // Expected
            } finally {
                latch.countDown();
            }
        }, "Loader-" + i).start();
    }

    latch.await();

    println("\nAll loading attempts completed");
    println("\nBenefits of Parallel Class Loading:");
    println("1. Improved startup time for large applications");
    println("2. Better multi-core CPU utilization");
    println("3. Reduced lock contention");
    println("4. Required for modern application servers");
}
```

By default, class loading is synchronized using a single lock per class loader.  
Calling `registerAsParallelCapable` allows the class loader to load different  
classes concurrently, significantly improving startup time for applications that  
load many classes. Modern application servers and frameworks rely on parallel  
class loading for performance.  


## Class unloading and garbage collection

This example explores how classes are unloaded when their class loader becomes  
unreachable and is garbage collected.  

```java
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

void main() throws Exception {

    println("Class Unloading Demonstration\n");

    // Track class references
    WeakReference<Class<?>> classRef;
    WeakReference<ClassLoader> loaderRef;

    {
        var url = Path.of(".").toUri().toURL();
        var loader = new URLClassLoader(new URL[]{url});
        loaderRef = new WeakReference<>(loader);

        // Load a standard library class
        var loadedClass = loader.loadClass("java.util.ArrayList");
        classRef = new WeakReference<>(loadedClass);

        println("Class loaded: " + loadedClass.getName());
        println("Loader: " + loader);
        println("Class identity: " + System.identityHashCode(loadedClass));

        // Close the loader
        loader.close();
        println("\nLoader closed");

        // Clear references
        loader = null;
        loadedClass = null;
    }

    // Request garbage collection
    println("\nRequesting garbage collection...");
    for (int i = 0; i < 5; i++) {
        System.gc();
        Thread.sleep(100);
    }

    println("\nAfter GC:");
    println("Loader reference: " + loaderRef.get());
    println("Class reference: " + classRef.get());

    println("\nClass Unloading Rules:");
    println("1. All instances of the class must be GC'd");
    println("2. The Class object must be unreachable");
    println("3. The ClassLoader must be unreachable");
    println("4. Bootstrap-loaded classes are never unloaded");
    println("5. System class loader classes are never unloaded");
}
```

Classes can only be unloaded when their defining class loader is garbage  
collected, which requires that no references exist to the class loader or any  
classes it loaded. Classes loaded by the bootstrap or system class loaders  
are never unloaded. This is why proper class loader management is essential  
in long-running applications with plugin architectures.  


## Best practices summary

This final example summarizes the key best practices for working with class  
loaders in Java applications.  

```java
void main() {

    println("╔══════════════════════════════════════════════════════════════════╗");
    println("║           Class Loader Best Practices Summary                    ║");
    println("╚══════════════════════════════════════════════════════════════════╝");

    println("\n┌─ DESIGN PRINCIPLES ─────────────────────────────────────────────┐");
    println("│ 1. Always follow parent delegation unless you have a good reason │");
    println("│ 2. Override findClass, not loadClass, in custom loaders          │");
    println("│ 3. Use URLClassLoader for file/JAR loading when possible         │");
    println("│ 4. Make class loaders closeable and implement close properly     │");
    println("│ 5. Consider thread context class loader for library code         │");
    println("└──────────────────────────────────────────────────────────────────┘");

    println("\n┌─ MEMORY MANAGEMENT ─────────────────────────────────────────────┐");
    println("│ 1. Always close URLClassLoader instances when done               │");
    println("│ 2. Remove ThreadLocal values before unloading                    │");
    println("│ 3. Deregister JDBC drivers before unloading                      │");
    println("│ 4. Clear any static references to loaded classes                 │");
    println("│ 5. Unregister JMX MBeans before unloading                        │");
    println("└──────────────────────────────────────────────────────────────────┘");

    println("\n┌─ SECURITY ─────────────────────────────────────────────────────┐");
    println("│ 1. Validate bytecode before loading from untrusted sources       │");
    println("│ 2. Set appropriate protection domains                            │");
    println("│ 3. Use JPMS for encapsulation in modern Java                     │");
    println("│ 4. Avoid exposing class loaders to untrusted code                │");
    println("│ 5. Be cautious with URL class loading from remote sources        │");
    println("└──────────────────────────────────────────────────────────────────┘");

    println("\n┌─ DEBUGGING ────────────────────────────────────────────────────┐");
    println("│ 1. Use -verbose:class to trace class loading                     │");
    println("│ 2. Check getCodeSource for class origin                          │");
    println("│ 3. Traverse class loader hierarchy for debugging                 │");
    println("│ 4. Use heap dumps to find class loader leaks                     │");
    println("│ 5. Monitor Metaspace for class loading issues                    │");
    println("└──────────────────────────────────────────────────────────────────┘");

    println("\n┌─ WHEN TO USE CUSTOM CLASS LOADERS ─────────────────────────────┐");
    println("│ ✓ Plugin/module systems with isolation requirements              │");
    println("│ ✓ Hot deployment and class reloading                             │");
    println("│ ✓ Loading classes from non-standard locations                    │");
    println("│ ✓ Bytecode transformation and instrumentation                    │");
    println("│ ✗ Avoid for simple applications (use standard loaders)           │");
    println("└──────────────────────────────────────────────────────────────────┘");

    // Demonstrate JVM class loading flags
    println("\n┌─ USEFUL JVM FLAGS ──────────────────────────────────────────────┐");
    println("│ -verbose:class          Show class loading details               │");
    println("│ -Xlog:class+load=info   JVM unified logging for class loading    │");
    println("│ -XX:+TraceClassLoading  Trace class loading (deprecated)         │");
    println("│ -XX:+TraceClassUnloading Trace class unloading                   │");
    println("└──────────────────────────────────────────────────────────────────┘");
}
```

Following these best practices will help you avoid common pitfalls when working  
with class loaders. The key is to understand the parent delegation model, manage  
class loader lifecycle properly, and use custom class loaders only when standard  
mechanisms are insufficient. Always clean up resources and references when  
unloading class loaders to prevent memory leaks.  
