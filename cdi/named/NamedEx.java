package com.zetcode;

import com.zetcode.provider.HelloMessageProvider;
import com.zetcode.provider.TimeMessageProvider;
import com.zetcode.renderer.ConsoleMessageRenderer;
import com.zetcode.renderer.FileMessageRenderer;

import javax.enterprise.inject.se.SeContainerInitializer;

public class NamedEx {

    public static void main(String[] args) {

        var initializer = SeContainerInitializer.newInstance();

        try (var container = initializer.disableDiscovery()
                .addBeanClasses(FileMessageRenderer.class, ConsoleMessageRenderer.class,
                        HelloMessageProvider.class, TimeMessageProvider.class).initialize()) {

            var messageRenderer = container.select(ConsoleMessageRenderer.class).get();
            messageRenderer.render();

            var messageRenderer2 = container.select(FileMessageRenderer.class).get();
            messageRenderer2.render();
        }
    }
}
