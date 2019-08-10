package com.zetcode;

import com.zetcode.provider.HelloMessageProvider;
import com.zetcode.renderer.ConsoleMessageRenderer;
import com.zetcode.renderer.MessageRenderer;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

public class CdiSimple {

    static {
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    public static void main(String[] args) {

        SeContainerInitializer initializer = SeContainerInitializer.newInstance();

        try (SeContainer container = initializer.disableDiscovery()
                .addBeanClasses(HelloMessageProvider.class, ConsoleMessageRenderer.class).initialize()) {

            MessageRenderer messageRenderer = container.select(ConsoleMessageRenderer.class).get();
            messageRenderer.render();
        }
    }
}
