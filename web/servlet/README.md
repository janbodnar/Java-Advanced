The examples are built with `jetty-maven-plugin`. The application is run with `mvn jetty:run`. 
Maven standard directory layout has the `src/main/webapp` directory, which contains web application 
resources, such as JavaScript, CSS, HTML files, view templates, and images. 


Java WAR

In software engineering, a WAR file (or Web application ARchive) is a 
JAR file used to distribute a collection of JavaServer Pages, Java Servlets, 
Java classes, XML files, tag libraries, static web pages 
(HTML and related files) and other resources that together constitute a web application.

The `/WEB-INF/classes` directory is on the ClassLoader's classpath. 
(The classpath consists of a list of locations from which .class files can be 
loaded and executed by the JVM.) The `/WEB-INF/classes` directory contains 
the classes associated with the web application itself.

There are special files and directories within a WAR file:

The `WEB-INF` directory in the WAR file contains a file named `web.xml` which 
defines the structure of the web application. If the web application is 
only serving JSP files, the `web.xml` file is not strictly necessary. 
If the web application uses servlets, then the servlet container uses web.xml 
to ascertain to which servlet a URL request will be routed. The web.xml file is 
also used to define context variables which can be referenced within the 
servlets and it is used to define environmental dependencies which the 
deployer is expected to set up. An example of this is a dependency on a mail 
session used to send email. The servlet container is responsible for 
providing this service.

Any JAR files placed in the `/WEB-INF/lib` directory will also be placed on 
the ClassLoader's classpath.
