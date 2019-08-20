The example shows how to control directory listings. Navigate to `localhost:8080/about`.

The project structure:

```
pom.xml
src
├───main
│   ├───java
│   ├───resources
│   └───webapp
│       │   index.html
│       ├───about
│       │       data.txt
│       └───WEB-INF
│               web.xml
│
└───test
    └───java
```

In Jetty:

```
<context-param>
    <param-name>org.eclipse.jetty.servlet.Default.dirAllowed</param-name>
    <param-value>true</param-value>
</context-param>
```

In Tomcat:

```
<servlet>
    <servlet-name>default</servlet-name>
    <servlet-class>org.apache.catalina.servlets.DefaultServlet</servlet-class>
    <init-param>
        <param-name>debug</param-name>
        <param-value>0</param-value>
    </init-param>
    <init-param>
        <param-name>listings</param-name>
        <param-value>true</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
    <servlet-name>default</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>    
```
