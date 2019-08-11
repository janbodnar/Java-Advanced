package com.zetcode;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

import javax.servlet.ServletException;

import static io.undertow.servlet.Servlets.defaultContainer;
import static io.undertow.servlet.Servlets.deployment;
import static io.undertow.servlet.Servlets.servlet;

// http://localhost:8080/myapp/hello

public class UndertowServletEx {

    private static final String MYAPP = "/myapp";

    public static void main(String[] args) throws ServletException {

            DeploymentInfo servletBuilder = deployment()
                    .setClassLoader(UndertowServletEx.class.getClassLoader())
                    .setContextPath(MYAPP)
                    .setDeploymentName("helloservlet.war")
                    .addServlets(
                            servlet("MessageServlet", HelloServlet.class)
                                    .addInitParam("message", "Hello there")
                                    .addMapping("/hello"));

            DeploymentManager manager = defaultContainer().addDeployment(servletBuilder);
            manager.deploy();

            HttpHandler servletHandler = manager.start();
            PathHandler path = Handlers.path(Handlers.redirect(MYAPP))
                    .addPrefixPath(MYAPP, servletHandler);

            Undertow server = Undertow.builder()
                    .addHttpListener(8080, "localhost")
                    .setHandler(path)
                    .build();

            server.start();
    }
}
