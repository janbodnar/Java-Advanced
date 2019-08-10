**Contexts and Dependency Injection (CDI)** defines a powerful set of complementary services that help improve the structure 
of application code. CDI is a Java EE specification.

The most fundamental services provided by CDI are as follows:

* Contexts: The ability to bind the lifecycle and interactions of stateful components to 
well-defined but extensible lifecycle contexts
* Dependency injection: The ability to inject components into an application in a typesafe way, 
including the ability to choose at deployment time which implementation of a particular interface to inject

CDI provides:

* an improved lifecycle for stateful objects, bound to well-defined contexts
* a typesafe approach to dependency injection
* object interaction via an event notification facility
* a better approach to binding interceptors to objects, along with a new kind of interceptor,
    called a decorator, that is more appropriate for use in solving business problems, and 
