# Java Modules

Java Modules, introduced in Java 9 as part of the Java Platform Module System  
(JPMS), represent a fundamental shift in how Java applications are structured  
and organized. A module is a named, self-describing collection of code and data  
that explicitly declares its dependencies and the packages it exports to other  
modules.  

Before Java 9, the JDK was a monolithic entity where all classes were accessible  
to any application, regardless of whether they were intended for public use. This  
led to several problems: large runtime footprints, security vulnerabilities from  
accessing internal APIs, and difficulties in maintaining backward compatibility.  
The classpath-based system also suffered from "JAR hell"—conflicts arising from  
multiple versions of the same library on the classpath.  

JPMS addresses these issues by introducing strong encapsulation and reliable  
configuration. Modules explicitly declare which packages they export (making them  
accessible to other modules) and which modules they depend on. This enables the  
Java runtime to verify that all dependencies are satisfied before running an  
application, preventing runtime errors from missing classes.  

The modular system also enables the creation of custom runtime images using the  
`jlink` tool, containing only the modules required by an application. This can  
dramatically reduce the size of deployed applications—from hundreds of megabytes  
to just a few dozen, making Java more suitable for containerized deployments and  
microservices architectures.  

## Key Concepts  

- **Module**: A named, self-describing collection of packages and resources  
- **Module Descriptor**: The `module-info.java` file declaring module metadata  
- **Exports**: Packages that other modules can access  
- **Requires**: Dependencies on other modules  
- **Opens**: Packages available for deep reflection at runtime  
- **Provides/Uses**: Service provider and consumer declarations  
- **Readability**: The relationship between a module and its dependencies  
- **Accessibility**: Whether a type is accessible to code in another module  
- **Strong Encapsulation**: Internal APIs hidden from other modules by default  
- **Reliable Configuration**: Dependencies verified at compile and runtime  

## Module Types  

| Type            | Description                                    | Use Case                   |
|-----------------|------------------------------------------------|----------------------------|
| Named Module    | Has module-info.java with explicit name        | New modular applications   |
| Automatic Module| JAR on module path without module-info.java    | Legacy library migration   |
| Unnamed Module  | Code on classpath (no module-info.java)        | Legacy applications        |

## JDK Modules  

The JDK itself is organized into approximately 70 modules. Every module  
implicitly depends on `java.base`, which contains fundamental classes like  
`java.lang`, `java.util`, and `java.io`.  

| Module          | Description                                              |
|-----------------|----------------------------------------------------------|
| `java.base`     | Fundamental classes (always available)                   |
| `java.sql`      | JDBC API for database access                             |
| `java.xml`      | XML processing APIs                                      |
| `java.logging`  | Java Logging API                                         |
| `java.desktop`  | AWT and Swing UI libraries                               |
| `java.net.http` | HTTP Client API                                          |
| `java.compiler` | Java Compiler API                                        |
| `jdk.httpserver`| Simple HTTP server for testing                           |


## Basic module declaration  

A module is declared using the `module-info.java` file placed at the root of  
the module's source directory.  

```java
// module-info.java
module com.example.greeting {
    // This is the simplest possible module declaration
    // It implicitly requires java.base
}
```

```java
// com/example/greeting/Greeter.java
package com.example.greeting;

public class Greeter {
    public String greet(String name) {
        return "Hello, " + name + "!";
    }
}
```

The module declaration establishes a namespace and encapsulation boundary for  
the code. Without any `exports` directive, all packages in the module are  
hidden from other modules. The module name typically follows reverse DNS  
naming conventions, similar to package names.  


## Exporting packages  

The `exports` directive makes a package accessible to other modules.  

```java
// module-info.java
module com.example.math {
    exports com.example.math.operations;
}
```

```java
// com/example/math/operations/Calculator.java
package com.example.math.operations;

public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }
}
```

```java
// com/example/math/internal/MathHelper.java
package com.example.math.internal;

// This class is NOT accessible outside the module
class MathHelper {
    static int square(int n) {
        return n * n;
    }
}
```

Only types in exported packages are accessible to other modules. The  
`com.example.math.internal` package remains encapsulated and cannot be  
accessed even if another module tries to import it. This enforces a clear  
public API boundary.  


## Requiring dependencies  

The `requires` directive declares a dependency on another module.  

```java
// module-info.java for com.example.app
module com.example.app {
    requires com.example.math;
}
```

```java
// com/example/app/Main.java
package com.example.app;

import com.example.math.operations.Calculator;

public class Main {
    public static void main(String[] args) {
        var calc = new Calculator();
        System.out.println("5 + 3 = " + calc.add(5, 3));
        System.out.println("5 * 3 = " + calc.multiply(5, 3));
    }
}
```

The `requires` directive establishes a readability relationship—code in  
`com.example.app` can read (access exported types from) `com.example.math`.  
If the required module is not present at compile or runtime, an error occurs.  
This provides reliable configuration, catching missing dependencies early.  


## Transitive dependencies  

The `requires transitive` directive passes readability to dependent modules.  

```java
// module-info.java for com.example.core
module com.example.core {
    requires transitive com.example.logging;
    exports com.example.core;
}
```

```java
// module-info.java for com.example.app
module com.example.app {
    requires com.example.core;
    // Implicitly can read com.example.logging due to 'requires transitive'
}
```

When module A requires transitive module B, any module that requires A can  
also read B. This is useful when a module's public API exposes types from  
another module. Without `transitive`, clients would need to explicitly require  
both modules, leading to verbose and error-prone declarations.  


## Qualified exports  

The `exports ... to` directive restricts package access to specific modules.  

```java
// module-info.java
module com.example.framework {
    exports com.example.framework.api;
    exports com.example.framework.internal to com.example.framework.impl;
}
```

Qualified exports allow fine-grained access control. The internal package  
is only accessible to the specified implementation module, not to any other  
module. This pattern is common in frameworks where internal APIs need to be  
shared between closely related modules while remaining hidden from users.  


## Opening packages for reflection  

The `opens` directive allows runtime reflective access to a package.  

```java
// module-info.java
module com.example.model {
    exports com.example.model.dto;
    opens com.example.model.entity to com.example.orm;
}
```

```java
// com/example/model/entity/User.java
package com.example.model.entity;

public class User {
    private String name;
    private int age;

    public User() {}

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
```

The `opens` directive allows frameworks like Hibernate or Jackson to access  
private fields via reflection. Unlike `exports`, which controls compile-time  
access, `opens` specifically enables deep reflection at runtime. You can use  
`opens ... to` for qualified access or just `opens` for universal access.  


## Open modules  

An `open module` allows all packages to be reflectively accessed.  

```java
// module-info.java
open module com.example.webapp {
    requires com.example.framework;
    exports com.example.webapp.controllers;
}
```

Open modules simplify migration of applications that heavily use reflection,  
such as Spring Boot or Jakarta EE applications. All packages are open for  
deep reflection while still requiring explicit `exports` for compile-time  
access. This provides a balance between encapsulation and framework compatibility.  


## Service provider interface  

The `provides ... with` directive declares that a module provides a service  
implementation.  

```java
// module-info.java for service API module
module com.example.messaging.api {
    exports com.example.messaging;
}
```

```java
// com/example/messaging/MessageService.java
package com.example.messaging;

public interface MessageService {
    void send(String message);
}
```

```java
// module-info.java for service provider module
module com.example.messaging.email {
    requires com.example.messaging.api;
    provides com.example.messaging.MessageService
        with com.example.messaging.email.EmailService;
}
```

```java
// com/example/messaging/email/EmailService.java
package com.example.messaging.email;

import com.example.messaging.MessageService;

public class EmailService implements MessageService {
    @Override
    public void send(String message) {
        System.out.println("Sending email: " + message);
    }
}
```

Services enable loose coupling between modules. The API module defines the  
interface, and provider modules supply implementations without the consumer  
needing compile-time knowledge of specific implementations. This is the  
foundation for plugin architectures.  


## Service consumer  

The `uses` directive declares that a module consumes a service.  

```java
// module-info.java for service consumer module
module com.example.app {
    requires com.example.messaging.api;
    uses com.example.messaging.MessageService;
}
```

```java
// com/example/app/Application.java
package com.example.app;

import com.example.messaging.MessageService;
import java.util.ServiceLoader;

public class Application {
    public static void main(String[] args) {
        ServiceLoader<MessageService> loader = 
            ServiceLoader.load(MessageService.class);

        for (MessageService service : loader) {
            service.send("Hello from modular Java!");
        }
    }
}
```

The `ServiceLoader` discovers and instantiates all available implementations  
at runtime. Multiple providers can coexist, and new implementations can be  
added by simply placing the provider module on the module path. This enables  
runtime extensibility without recompilation.  


## Static dependencies  

The `requires static` directive declares an optional compile-time dependency.  

```java
// module-info.java
module com.example.app {
    requires static com.example.annotations;
    requires static org.slf4j;
}
```

Static dependencies are required at compile time but optional at runtime.  
This is useful for annotation processors that are only needed during  
compilation, or for logging facades where the implementation might not always  
be present. If the module is absent at runtime, no error occurs unless code  
actually attempts to use it.  


## Listing module dependencies  

Use the `java` command with `--describe-module` to view module information.  

```bash
# Describe a JDK module
java --describe-module java.sql

# Output example:
# java.sql jar:file:///path/to/jdk/jmods/java.sql.jmod!/module-info.class
# exports java.sql
# exports javax.sql
# requires java.base mandated
# requires java.xml transitive
# requires java.logging transitive
# uses java.sql.Driver
```

The `--describe-module` (or `-d`) option displays the module's exports,  
requires, uses, and provides directives. This is invaluable for understanding  
module dependencies and troubleshooting configuration issues. You can also  
use `--list-modules` to see all available modules.  


## Compiling modular code  

Modular applications are compiled using the `--module-source-path` option.  

```bash
# Project structure:
# src/
#   com.example.greeting/
#     module-info.java
#     com/example/greeting/Greeter.java
#   com.example.app/
#     module-info.java
#     com/example/app/Main.java

# Compile specific modules (recommended)
javac -d out --module-source-path src -m com.example.greeting,com.example.app

# Or compile all modules using find (for small projects)
javac -d out --module-source-path src @sources.txt
# where sources.txt contains: find src -name "*.java" > sources.txt
```

The `--module-source-path` option specifies the root directory containing  
module source code. Each module must be in a directory matching its module  
name. The `-d` option specifies the output directory for compiled classes.  
The compiler validates all module dependencies during compilation.  


## Running modular applications  

Modular applications run using the `--module-path` and `--module` options.  

```bash
# Run a modular application
java --module-path out --module com.example.app/com.example.app.Main

# Or use short options
java -p out -m com.example.app/com.example.app.Main
```

The `--module-path` (or `-p`) specifies where to find compiled modules.  
The `--module` (or `-m`) specifies the module and main class to run in the  
format `module/mainclass`. The module system verifies all dependencies are  
satisfied before starting execution.  


## Creating modular JARs  

Modular JARs include the `module-info.class` at the JAR root.  

```bash
# Create a modular JAR
jar --create --file greeting.jar \
    --module-version=1.0 \
    -C out/com.example.greeting .

# Create an executable modular JAR
jar --create --file app.jar \
    --main-class=com.example.app.Main \
    --module-version=1.0 \
    -C out/com.example.app .
```

Modular JARs are standard JAR files with a `module-info.class` file. The  
`--module-version` option embeds version information in the module descriptor.  
The `--main-class` option sets the entry point, allowing the JAR to be run  
with just `-m module-name` without specifying the main class.  


## Multi-release JARs  

Multi-release JARs support different code for different Java versions.  

```
# JAR structure:
mymodule.jar
├── module-info.class           (for Java 9+)
├── META-INF/
│   ├── MANIFEST.MF
│   └── versions/
│       ├── 11/
│       │   └── com/example/Helper.class
│       └── 17/
│           └── com/example/Helper.class
└── com/example/
    ├── Main.class
    └── Helper.class             (base version for Java 8)
```

Multi-release JARs allow a single JAR to contain version-specific code. The  
JVM automatically selects the appropriate classes based on the runtime version.  
This enables libraries to use modern APIs when available while maintaining  
compatibility with older Java versions.  


## Automatic modules  

JARs without module-info.java become automatic modules on the module path.  

```java
// module-info.java
module com.example.app {
    // Automatic module name is derived from JAR filename
    // guava-31.1-jre.jar becomes guava
    requires guava;
}
```

```bash
# Run with automatic module
java --module-path libs:out \
     --module com.example.app/com.example.app.Main
```

Automatic modules enable gradual migration by allowing non-modular JARs to  
participate in the module system. The module name is derived from the JAR  
filename (or from `Automatic-Module-Name` manifest attribute). Automatic  
modules export all packages and can read all other modules.  


## Automatic-Module-Name manifest entry  

Library maintainers can declare a stable automatic module name.  

```
# META-INF/MANIFEST.MF
Manifest-Version: 1.0
Automatic-Module-Name: com.example.library
```

```bash
# Build JAR with Automatic-Module-Name
jar --create --file library.jar \
    --manifest=MANIFEST.MF \
    -C classes .
```

The `Automatic-Module-Name` attribute provides a stable module name for  
non-modular JARs. This is important for library authors who want to reserve  
their module name before fully modularizing. It ensures dependent modules  
won't break when the library later adds a real module-info.java.  


## Unnamed module  

Code on the classpath belongs to the unnamed module.  

```bash
# Running with classpath (unnamed module)
java -cp out:libs/* com.example.app.Main

# Mixing classpath and module path
java --module-path mods \
     --classpath libs/* \
     --add-modules com.example.api \
     -m com.example.app/com.example.app.Main
```

The unnamed module can read all named modules but named modules cannot  
require the unnamed module. This ensures a clean migration path—modular code  
cannot depend on non-modular code, encouraging bottom-up modularization of  
dependencies before applications.  


## Adding modules at runtime  

The `--add-modules` option adds modules to the root set.  

```bash
# Add specific modules
java --module-path libs:out \
     --add-modules com.example.logging \
     -m com.example.app/com.example.app.Main

# Add all automatic modules
java --module-path libs:out \
     --add-modules ALL-MODULE-PATH \
     -m com.example.app/com.example.app.Main
```

By default, only the root module and its transitive dependencies are  
resolved. `--add-modules` explicitly includes additional modules that might  
not be directly required. `ALL-MODULE-PATH` adds all modules found on the  
module path, useful during migration.  


## Adding exports at runtime  

The `--add-exports` option opens internal APIs at runtime.  

```bash
# Export internal package to specific module
java --module-path out \
     --add-exports com.example.core/com.example.core.internal=com.example.app \
     -m com.example.app/com.example.app.Main

# Export to all unnamed modules
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED \
     -cp out com.example.Main
```

`--add-exports` is a workaround for accessing non-exported packages. This is  
often needed during migration when code depends on internal APIs. The format  
is `module/package=target-module`. Use sparingly and work toward removing  
these dependencies as they may break in future Java versions.  


## Adding opens at runtime  

The `--add-opens` option enables deep reflection at runtime.  

```bash
# Open package for reflection
java --module-path out \
     --add-opens com.example.model/com.example.model.entity=com.example.orm \
     -m com.example.app/com.example.app.Main

# Open for all unnamed modules (common for frameworks on classpath)
java --add-opens java.base/java.lang=ALL-UNNAMED \
     -cp libs/*:out com.example.Main
```

`--add-opens` is necessary when frameworks need to reflectively access  
private fields or methods in packages that aren't opened. This is commonly  
needed for serialization frameworks, dependency injection, and ORM tools.  
Like `--add-exports`, this should be considered a temporary migration aid.  


## Reading modules programmatically  

The `ModuleLayer` and `Module` APIs provide runtime module introspection.  

```java
void main() {

    // Get the boot layer (contains all platform modules)
    var bootLayer = ModuleLayer.boot();

    // List all modules in the boot layer
    println("Modules in boot layer:");
    bootLayer.modules().stream()
        .map(Module::getName)
        .sorted()
        .limit(10)
        .forEach(name -> println("  " + name));

    // Get information about a specific module
    var sqlModule = bootLayer.findModule("java.sql");
    sqlModule.ifPresent(m -> {
        println("\njava.sql module:");
        println("  Packages: " + m.getPackages());
        println("  Descriptor: " + m.getDescriptor());
    });
}
```

The `ModuleLayer` represents a resolved set of modules. The boot layer is  
created by the JVM and contains platform modules plus any modules resolved  
from the module path. Applications can inspect module metadata at runtime,  
useful for debugging or building module-aware frameworks.  


## Module descriptor API  

The `ModuleDescriptor` class provides compile-time module metadata.  

```java
void main() {

    var descriptor = ModuleLayer.boot()
        .findModule("java.sql")
        .map(Module::getDescriptor)
        .orElseThrow();

    println("Module: " + descriptor.name());
    println("Version: " + descriptor.version().orElse(null));

    println("\nExports:");
    descriptor.exports().forEach(e ->
        println("  " + e.source() + 
            (e.isQualified() ? " to " + e.targets() : "")));

    println("\nRequires:");
    descriptor.requires().forEach(r ->
        println("  " + r.name() + 
            (r.modifiers().isEmpty() ? "" : " " + r.modifiers())));

    println("\nUses:");
    descriptor.uses().forEach(u -> println("  " + u));
}
```

The `ModuleDescriptor` API provides programmatic access to all information  
in a module-info.java file. This enables tools to analyze module structure,  
verify dependencies, or generate documentation without parsing source code.  


## Checking module readability  

Verify if one module can read another at runtime.  

```java
void main() {

    var bootLayer = ModuleLayer.boot();

    var sqlModule = bootLayer.findModule("java.sql").orElseThrow();
    var loggingModule = bootLayer.findModule("java.logging").orElseThrow();
    var baseModule = bootLayer.findModule("java.base").orElseThrow();

    println("java.sql reads java.logging: " + sqlModule.canRead(loggingModule));
    println("java.sql reads java.base: " + sqlModule.canRead(baseModule));

    // Check if a package is exported
    println("\njava.sql exports javax.sql: " + 
        sqlModule.isExported("javax.sql"));
    println("java.sql exports sun.* (internal): " + 
        sqlModule.isExported("com.sun.rowset.internal"));
}
```

The `canRead` method checks if a readability relationship exists between  
modules. The `isExported` method checks if a package is accessible to other  
modules. These APIs are useful for debugging module configuration issues and  
building tools that analyze module graphs.  


## Custom module layers  

Create isolated module configurations using ModuleLayer.  

```java
import java.lang.module.*;
import java.nio.file.Path;

void main() throws Exception {

    // Find modules from a path
    var finder = ModuleFinder.of(Path.of("plugins"));

    // Create a configuration for the new layer
    var bootLayer = ModuleLayer.boot();
    var cf = bootLayer.configuration().resolve(
        finder,
        ModuleFinder.of(),
        Set.of("com.example.plugin")
    );

    // Create the layer with a new class loader
    var layer = bootLayer.defineModulesWithOneLoader(
        cf,
        ClassLoader.getSystemClassLoader()
    );

    // Find and use a class from the plugin module
    var pluginClass = layer.findLoader("com.example.plugin")
        .loadClass("com.example.plugin.Plugin");

    println("Loaded plugin: " + pluginClass.getName());
}
```

Custom module layers enable plugin systems and application isolation. Each  
layer can have its own version of modules, allowing multiple versions of the  
same library to coexist. This is particularly useful for application servers  
hosting multiple applications.  


## ServiceLoader with modules  

ServiceLoader integrates seamlessly with the module system.  

```java
// module-info.java for API
module com.example.plugin.api {
    exports com.example.plugin;
}
```

```java
// com/example/plugin/Plugin.java
package com.example.plugin;

public interface Plugin {
    String getName();
    void execute();
}
```

```java
// module-info.java for implementation
module com.example.plugin.hello {
    requires com.example.plugin.api;
    provides com.example.plugin.Plugin
        with com.example.plugin.hello.HelloPlugin;
}
```

```java
// com/example/plugin/hello/HelloPlugin.java
package com.example.plugin.hello;

import com.example.plugin.Plugin;

public class HelloPlugin implements Plugin {
    @Override
    public String getName() {
        return "Hello Plugin";
    }

    @Override
    public void execute() {
        System.out.println("Hello from plugin!");
    }
}
```

```java
// Application using plugins
void main() {

    var plugins = ServiceLoader.load(Plugin.class);

    println("Available plugins:");
    for (var plugin : plugins) {
        println("  - " + plugin.getName());
        plugin.execute();
    }
}
```

The `provides` and `uses` directives make service relationships explicit  
in module descriptors. This allows the module system to verify at launch time  
that required services are available, preventing runtime service discovery  
failures.  


## Resource encapsulation  

Resources in modules follow the same encapsulation rules as packages.  

```java
void main() throws Exception {

    // Access resource from own module
    try (var stream = getClass().getResourceAsStream("/config.properties")) {
        if (stream != null) {
            var props = new java.util.Properties();
            props.load(stream);
            println("Config: " + props);
        }
    }

    // Access resource from another module (must be in open or exported package)
    var module = ModuleLayer.boot().findModule("java.base").orElseThrow();
    try (var stream = module.getResourceAsStream("java/lang/Object.class")) {
        if (stream != null) {
            println("Object.class size: " + stream.readAllBytes().length);
        }
    }
}
```

Resources in a module package are encapsulated along with the package code.  
Resources in the module root or in open/exported packages are accessible.  
For other packages, use `Module.getResourceAsStream` with the fully qualified  
resource name including the package path.  


## Patching modules  

The `--patch-module` option adds or replaces classes in a module.  

```bash
# Patch a module with additional classes
java --module-path mods \
     --patch-module com.example.app=patches/com.example.app \
     -m com.example.app/com.example.app.Main

# Patch a JDK module for testing
java --patch-module java.base=patches/java.base \
     --add-exports java.base/jdk.internal.misc=ALL-UNNAMED \
     -cp test TestInternals
```

Module patching is useful for testing, debugging, or temporarily fixing bugs  
without rebuilding modules. The patched classes are loaded instead of the  
original classes. This is primarily a development and testing tool and should  
not be used in production deployments.  


## jdeps dependency analysis  

The `jdeps` tool analyzes class dependencies and module requirements.  

```bash
# Analyze dependencies of a JAR
jdeps --module-path libs myapp.jar

# Generate module-info.java
jdeps --generate-module-info out --module-path libs myapp.jar

# Check for JDK internal API usage
jdeps --jdk-internals myapp.jar

# Show package-level dependencies
jdeps -verbose:package myapp.jar

# Analyze in dot format for visualization
jdeps --dot-output deps myapp.jar
```

The `jdeps` tool is essential for migration planning. It identifies  
dependencies on JDK internal APIs (which are now encapsulated) and helps  
generate initial module-info.java files. The `--jdk-internals` option  
specifically highlights code that will break under strict encapsulation.  


## jlink custom runtime image  

The `jlink` tool creates minimal runtime images containing only required  
modules.  

```bash
# Create custom runtime with only needed modules
jlink --module-path $JAVA_HOME/jmods:out \
      --add-modules com.example.app \
      --output custom-runtime \
      --strip-debug \
      --compress zip-6 \
      --no-header-files \
      --no-man-pages

# Run application with custom runtime
./custom-runtime/bin/java -m com.example.app/com.example.app.Main
```

Custom runtime images can be dramatically smaller than the full JDK. A simple  
application might only need `java.base`, reducing the image from ~300MB to  
~30MB. This is ideal for containerized deployments where image size impacts  
startup time and resource usage.  


## jlink launcher scripts  

Create native launchers for modular applications.  

```bash
# Create runtime with launcher
jlink --module-path $JAVA_HOME/jmods:out \
      --add-modules com.example.app \
      --output myapp \
      --launcher myapp=com.example.app/com.example.app.Main

# Run using the launcher
./myapp/bin/myapp
```

The `--launcher` option creates a platform-specific script (or executable)  
that runs the application without requiring users to know the module and  
class names. This provides a more user-friendly deployment experience.  


## Encapsulation and reflection  

Strong encapsulation affects reflective access to module internals.  

```java
import java.lang.reflect.*;

void main() throws Exception {

    // This works - String is in an exported package
    var stringClass = String.class;
    var valueField = stringClass.getDeclaredField("value");
    println("String.value field: " + valueField.getType());

    // This would fail without --add-opens
    try {
        valueField.setAccessible(true);  // Requires deep reflection
        println("Got accessible (module is open)");
    } catch (InaccessibleObjectException e) {
        println("Cannot access: " + e.getMessage());
        println("Use: --add-opens java.base/java.lang=ALL-UNNAMED");
    }
}
```

The `setAccessible(true)` call requires deep reflective access. In the module  
system, this only succeeds if the target package is opened (via `opens`  
directive or `--add-opens`). This protects internal implementation details  
from being modified by malicious or buggy code.  


## Module resolution  

The module system resolves dependencies in a specific order.  

```
Resolution process:
1. Root modules (specified with -m or --add-modules)
2. Add required modules transitively
3. Add service providers for used services
4. Verify no split packages (same package in multiple modules)
5. Check for cyclic dependencies (not allowed)
6. Create readable graph
```

```bash
# Show module resolution
java --module-path mods --show-module-resolution -m com.example.app/com.example.app.Main
```

Module resolution is deterministic and happens at startup. The  
`--show-module-resolution` option displays the resolution process, helpful  
for debugging complex module configurations. Any resolution failure results  
in an error before the application starts.  


## Split package handling  

Split packages (same package in multiple modules) are prohibited.  

```java
// This is NOT allowed:
// Module A contains: com.example.utils.StringUtils
// Module B contains: com.example.utils.NumberUtils
// Both export com.example.utils -> SPLIT PACKAGE ERROR
```

```bash
# Resolution error example:
# Error: Package com.example.utils in both moduleA and moduleB

# Workaround: Merge into single module or rename packages
```

Split packages occur when the same package appears in multiple modules. This  
was common with the classpath where JARs could contribute classes to the same  
package. The module system requires each package to belong to exactly one  
module. Migration may require refactoring packages.  


## Illegal access warnings  

The JVM warns about illegal reflective access during migration.  

```bash
# Java 9-15: Warning by default, access permitted
# Java 16: Warning by default, access permitted
# Java 17+: Strong encapsulation, access denied by default

# Show all illegal access warnings
java --illegal-access=warn ...

# Deny all illegal access (default in Java 17+)
java --illegal-access=deny ...

# Permit illegal access with warnings (deprecated)
java --illegal-access=permit ...
```

These warnings help identify code that needs migration. The progression from  
warnings to denial gives developers time to update their code. Modern Java  
versions require explicit `--add-opens` flags for any deep reflection into  
JDK internals.  


## Maven module configuration  

Configure Maven for modular builds.  

```xml
<!-- pom.xml -->
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>mymodule</artifactId>
    <version>1.0.0</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
            </plugin>
        </plugins>
    </build>
</project>
```

```
# Project structure:
src/
  main/
    java/
      module-info.java
      com/example/mymodule/
        Main.java
```

Maven automatically detects `module-info.java` and compiles in module mode.  
The module descriptor should be placed at `src/main/java/module-info.java`.  
Maven handles the module path configuration automatically during compilation  
and testing.  


## Gradle module configuration  

Configure Gradle for modular builds.  

```groovy
// build.gradle
plugins {
    id 'java'
    id 'application'
}

java {
    modularity.inferModulePath = true
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

application {
    mainModule = 'com.example.app'
    mainClass = 'com.example.app.Main'
}

tasks.withType(JavaCompile) {
    options.encoding = 'UTF-8'
}
```

```
# Project structure:
src/
  main/
    java/
      module-info.java
      com/example/app/
        Main.java
```

Gradle 6.4+ supports JPMS with automatic module path inference. The  
`inferModulePath` option automatically places modular JARs on the module path  
and non-modular JARs on the classpath. The `application` plugin configuration  
specifies the module and main class.  


## Testing modular code  

Configure testing frameworks for modular applications.  

```java
// src/main/java/module-info.java
module com.example.calculator {
    exports com.example.calculator;
}
```

```java
// src/test/java/module-info.java
open module com.example.calculator.test {
    requires com.example.calculator;
    requires org.junit.jupiter.api;
}
```

```java
// src/test/java/com/example/calculator/test/CalculatorTest.java
package com.example.calculator.test;

import com.example.calculator.Calculator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    @Test
    void testAdd() {
        var calc = new Calculator();
        assertEquals(5, calc.add(2, 3));
    }
}
```

Test modules typically require the module under test and testing frameworks.  
The `open` modifier allows test frameworks to reflectively access test  
classes. Some build tools use `--add-reads` and `--add-opens` instead of  
separate test modules.  


## Handling legacy libraries  

Strategies for using non-modular libraries in modular applications.  

```java
// module-info.java
module com.example.app {
    // Library with Automatic-Module-Name in manifest
    requires org.apache.commons.lang3;

    // Library without - name derived from filename
    requires gson;  // from gson-2.10.jar
}
```

```bash
# If module name conflicts or missing:
# 1. Check for Automatic-Module-Name in JAR manifest
unzip -p gson-2.10.jar META-INF/MANIFEST.MF | grep Automatic

# 2. Use --add-modules for runtime-only modules
java --module-path libs:out \
     --add-modules ALL-MODULE-PATH \
     -m com.example.app/com.example.app.Main
```

When using legacy libraries, check if they define `Automatic-Module-Name` in  
their manifest. If not, the module name is derived from the JAR filename  
after removing version numbers and replacing hyphens with dots. Consider  
reporting issues to library maintainers encouraging modularization.  


## Migration strategy bottom-up  

Migrate applications from classpath to module path incrementally.  

```
Bottom-up migration approach:
1. Analyze dependencies with jdeps
2. Wait for/request library modularization
3. Modularize lowest-level (leaf) modules first
4. Work up the dependency tree
5. Modularize application last

Advantages:
- Clean dependency graph
- Each step is independently testable
- Libraries become reusable modules
```

```bash
# Step 1: Analyze with jdeps
jdeps --jdk-internals myapp.jar

# Step 2: Check library status
jdeps --module-path libs --check libs/dependency.jar

# Step 3: Start with leaf dependencies
# (those with no dependencies on your other code)
```

Bottom-up migration is the recommended approach. It ensures each module can  
properly declare its dependencies on already-modularized code. This approach  
may be slow if waiting for third-party libraries but produces the cleanest  
module architecture.  


## Migration strategy top-down  

Migrate the application module first, using automatic modules for  
dependencies.  

```java
// module-info.java for application
module com.example.app {
    // Automatic modules from classpath JARs
    requires commons.lang3;
    requires guava;
    requires slf4j.api;

    // Your modularized code
    requires com.example.core;

    exports com.example.app;
}
```

```bash
# Run with mix of modules and classpath
java --module-path mods:libs \
     -m com.example.app/com.example.app.Main
```

Top-down migration enables faster adoption of the module system for your  
application code. Dependencies become automatic modules, which export  
everything and can read all other modules. This approach sacrifices some  
encapsulation benefits but allows immediate modularization.  


## Avoiding common pitfalls  

Common mistakes when adopting the module system and how to avoid them.  

```java
// PITFALL 1: Accessing internal APIs
// Old code using sun.misc.Unsafe
// Solution: Use VarHandle or --add-opens as temporary fix

// PITFALL 2: Forgetting to export packages
// module-info.java
module com.example.api {
    // WRONG: Package not exported, clients can't use it
    // exports com.example.api;  // Uncomment this!
}

// PITFALL 3: Cyclic dependencies
// Module A requires Module B
// Module B requires Module A  // NOT ALLOWED
// Solution: Extract shared code to Module C

// PITFALL 4: Split packages
// Same package in multiple modules
// Solution: Rename packages or merge modules
```

Most pitfalls can be avoided by running `jdeps` early to identify issues.  
Start with `--illegal-access=warn` to see potential problems. Design modules  
around clear API boundaries rather than trying to modularize existing package  
structures that may have circular dependencies.  


## Module best practices  

Guidelines for effective module design.  

```java
// Best Practice 1: Export API packages only
module com.example.service {
    exports com.example.service.api;          // Public API
    // Internal packages not exported:
    // com.example.service.internal
    // com.example.service.impl
}

// Best Practice 2: Minimize dependencies
module com.example.core {
    requires transitive com.example.model;    // Part of API
    requires com.example.logging;             // Implementation detail
}

// Best Practice 3: Use services for extensibility
module com.example.framework {
    exports com.example.framework.spi;
    uses com.example.framework.spi.Extension;
}

// Best Practice 4: Open specific packages, not entire module
module com.example.entity {
    exports com.example.entity;
    opens com.example.entity to com.fasterxml.jackson.databind;
}
```

Design modules around stable APIs rather than implementation details. Keep  
modules focused—a module should have a single clear purpose. Use qualified  
exports and opens to limit access to specific trusted modules rather than  
exposing internals to everyone.  


## Module versioning  

Modules can include version information for documentation and tooling.  

```java
// Specify version when creating JAR
// jar --create --file mylib.jar --module-version=2.1.0 -C classes .
```

```java
void main() {

    ModuleLayer.boot().modules().stream()
        .filter(m -> m.getName().startsWith("java."))
        .limit(5)
        .forEach(m -> {
            var version = m.getDescriptor()
                .version()
                .map(v -> v.toString())
                .orElse("(no version)");
            println(m.getName() + ": " + version);
        });
}
```

Module versions are optional and for informational purposes—they don't  
affect resolution. The JPMS does not support multiple versions of the same  
module in a single layer. For versioning requirements, use build tools like  
Maven or Gradle that handle version resolution before creating the module  
path.  


## Comparing modules with OSGi  

Differences between JPMS and OSGi module systems.  

```
Feature          | JPMS                    | OSGi
-----------------|-------------------------|---------------------------
Scope            | Platform-level          | Application framework
Versioning       | Single version per layer| Multiple versions supported
Dynamic updates  | Not supported           | Hot deployment supported
Service registry | ServiceLoader           | OSGi Service Registry
Encapsulation    | Compile & runtime       | Runtime only
Integration      | Built into JVM          | Requires OSGi container
Complexity       | Simpler                 | More powerful, complex
```

JPMS and OSGi solve different problems. JPMS provides platform-level  
modularity with strong compile-time checking. OSGi offers more dynamic  
capabilities including runtime updates and multiple versions. Many  
applications may benefit from using both—JPMS for platform structure and  
OSGi for dynamic plugin management.  


## Performance implications  

Module system impact on application performance.  

```java
void main() {

    // Module resolution happens at startup
    // - Single resolution, cached for JVM lifetime
    // - May slightly increase startup time
    // - No runtime overhead for module checks

    // Benefits:
    // - Smaller runtime images (faster startup, less memory)
    // - Class loading optimizations
    // - Ahead-of-time compilation benefits (GraalVM)

    // Measure startup time
    var start = ProcessHandle.current().info().startInstant();
    var now = java.time.Instant.now();
    start.ifPresent(s -> 
        println("Startup time: " + 
            java.time.Duration.between(s, now).toMillis() + "ms"));
}
```

The module system adds minimal overhead. Resolution happens once at startup  
and results are cached. The main performance benefits come from custom  
runtime images—smaller images load faster and use less memory. The explicit  
dependency graph also enables better class loading optimizations.  


## Module annotations  

Use annotations for module-level metadata.  

```java
// module-info.java
@Deprecated(since = "2.0", forRemoval = true)
module com.example.legacy {
    exports com.example.legacy;
}
```

```java
void main() {

    var module = ModuleLayer.boot()
        .findModule("java.rmi")
        .orElseThrow();

    // Check for deprecation
    if (module.isAnnotationPresent(Deprecated.class)) {
        var deprecated = module.getAnnotation(Deprecated.class);
        println(module.getName() + " is deprecated");
        println("  Since: " + deprecated.since());
        println("  For removal: " + deprecated.forRemoval());
    }
}
```

Modules can be annotated with `@Deprecated` to signal they will be removed  
in future versions. This annotation appears in documentation and generates  
warnings when the module is used. Other annotations may be used by frameworks  
or tools but have no effect on the module system itself.  


## Conclusion  

The Java Platform Module System represents a significant evolution in Java's  
architecture. By introducing strong encapsulation and explicit dependencies,  
JPMS enables:  

- **Better maintainability**: Clear module boundaries make codebases easier  
  to understand and modify  
- **Improved security**: Internal APIs are hidden by default, reducing attack  
  surface  
- **Smaller deployments**: Custom runtime images contain only required modules  
- **Reliable configuration**: Dependency issues are caught at startup rather  
  than causing runtime errors  
- **Scalable development**: Large applications can be decomposed into  
  well-defined modules with explicit interfaces  

While migration requires effort, especially for applications using internal  
APIs or reflection-heavy frameworks, the long-term benefits justify the  
investment. Start with `jdeps` analysis, modularize incrementally, and design  
new code with modularity in mind.  

The module system works best when combined with good software engineering  
practices: clear API boundaries, minimal dependencies, and separation of  
concerns. Embrace modules as a tool for better software design, not just a  
JVM feature to adopt.  
