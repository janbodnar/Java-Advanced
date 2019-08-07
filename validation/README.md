Bean Validation is a Java specification for data validation in Java. Hibernate Validator
is a reference implementation of the specification.

Bean Validation 

* allows to define constraints on object models via annotations
* allows write custom constraints in an extensible way
* provides the APIs to validate objects and object graphs
* provides the APIs to validate parameters and return values of methods and constructors
* reports the set of violations (localized)
* runs on Java SE but is integrated in Java EE 6 and later; Bean Validation 2.0 is part of Java EE 8

For Java 11+, we need to all add JAXB dependencies, which were dropped from Java SE.
