package com.zetcode;

import com.zetcode.conf.AppConfig;
import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.weld.environment.servlet.Listener;

public class ContextEx {

    public static void main(String[] args) {

        var server = new UndertowJaxrsServer();
        Undertow.Builder serverBuilder = Undertow.builder().addHttpListener(8080, "0.0.0.0");

        ResteasyDeployment deployment = new ResteasyDeploymentImpl();

        DeploymentInfo deploymentInfo = server.undertowDeployment(deployment, "/");
        deployment.setApplicationClass(AppConfig.class.getName());
        deployment.setInjectorFactoryClass("org.jboss.resteasy.cdi.CdiInjectorFactory");

        deploymentInfo.setClassLoader(ContextEx.class.getClassLoader());
        deploymentInfo.setDeploymentName("My Application");
        deploymentInfo.setContextPath("/api");
        deploymentInfo.addListeners(Servlets.listener(Listener.class));

        server.deploy(deploymentInfo);
        server.start(serverBuilder);
    }
}
