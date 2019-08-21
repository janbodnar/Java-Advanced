The example demonstrates how to use container-based login form in Tomcat. The user is defined
in `tomcat-users.xml`.
Need to add SSL support. 


Project structure:

```
pom.xml
src
├───main
│   ├───java
│   │   └───com
│   │       └───zetcode
│   │           └───web
│   │                   AdminServlet.java
│   │                   LogoutServlet.java
│   ├───resources
│   └───webapp
│       │   index.jsp
│       └───WEB-INF
│               error.jsp
│               login.jsp
│               logout.jsp
│               web.xml
└───test
    └───java
```

