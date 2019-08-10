package com.zetcode;

import com.zetcode.factory.MessageFactory;
import com.zetcode.provider.HelloMessageProvider;
import com.zetcode.provider.TimeMessageProvider;
import com.zetcode.renderer.ConsoleMessageRenderer;

import javax.enterprise.inject.se.SeContainerInitializer;

// A producer method acts as a source of objects to be injected, where:

// the objects to be injected are not required to be instances of beans
// the concrete type of the objects to be injected may vary at runtime or
// the objects require some custom initialization that is not performed
//     by the bean constructor

public class ProducerEx {

    public static void main(String[] args) {
        var initializer = SeContainerInitializer.newInstance();

        try (var container = initializer.disableDiscovery()
                .addBeanClasses(ConsoleMessageRenderer.class, MessageFactory.class,
                        HelloMessageProvider.class, TimeMessageProvider.class).initialize()) {

            var messageRenderer = container.select(ConsoleMessageRenderer.class).get();
            messageRenderer.render();
        }
    }
}
