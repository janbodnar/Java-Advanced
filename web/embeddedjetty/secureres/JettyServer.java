package com.zetcode;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.security.Constraint;

import java.util.Collections;

public class JettyServer {

    public static void main(String[] args) throws Exception {

        var server = new Server(8080);

        // three handlers

        var ctxHand = new ContextHandler();
        ctxHand.setContextPath("/home");
        ctxHand.setHandler(new HomeHandler());

        var ctxHand2 = new ContextHandler();
        ctxHand2.setContextPath("/hello");
        ctxHand2.setHandler(new HelloHandler());

        var ctxHand3 = new ContextHandler();
        ctxHand3.setContextPath("/admin/stat");
        ctxHand3.setHandler(new StatHandler());

        // Creating the LoginService for the realm
        var loginService = new HashLoginService("MyRealm");

        // Setting the realm configuration there the users, passwords and roles reside
        loginService.setConfig("src/main/resources/myrealm.properties");
        server.addBean(loginService);


        var constraintSecurityHandler = new ConstraintSecurityHandler();

        // This constraint requires authentication and in addition that an
        // authenticated user be a member of a given set of roles for
        // authorization purposes.
        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(true);
//        constraint.setRoles(new String[]{"user", "admin"});
        constraint.setRoles(new String[]{"admin"});

        // Binds a url pattern with the previously created constraint. The roles
        // for this constraint mapping are mined from the Constraint itself
        // although methods exist to declare and bind roles separately as well.
        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/admin/*");
        mapping.setConstraint(constraint);

        constraintSecurityHandler.setConstraintMappings(Collections.singletonList(mapping));
        constraintSecurityHandler.setAuthenticator(new BasicAuthenticator());
        constraintSecurityHandler.setLoginService(loginService);

        var handlerCollection = new HandlerCollection();
        handlerCollection.setHandlers(new Handler[]{ctxHand, ctxHand2, ctxHand3});
        constraintSecurityHandler.setHandler(handlerCollection);
        server.setHandler(constraintSecurityHandler);

        server.start();
        server.join();
    }
}
