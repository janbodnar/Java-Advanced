package com.zetcode.factory;

import com.zetcode.producer.Message;
import com.zetcode.producer.MessageType;
import com.zetcode.provider.HelloMessageProvider;
import com.zetcode.provider.TimeMessageProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MessageFactory {

    private Properties properties;

    @Produces
    @Message(MessageType.HELLO_MESSAGE)
    public HelloMessageProvider getHelloMessageProvider() {
        return new HelloMessageProvider();
    }

    @Produces
    @Message(MessageType.TIME_MESSAGE)
    public TimeMessageProvider getTimeMessageProvider() {

        var format = String.valueOf(properties.get("timeFormat"));

        var timeMessageProvider = new TimeMessageProvider();
        timeMessageProvider.setFormat(format);
        return timeMessageProvider;
    }

    @PostConstruct
    private void loadProperties() {

        this.properties = new Properties();
        final InputStream stream = MessageFactory.class
                .getResourceAsStream("/application.properties");

        if (stream == null) {

            throw new RuntimeException("properties file not found");
        }

        try {

            this.properties.load(stream);

        } catch (IOException e) {

            throw new RuntimeException("failed to load configuration");
        }
    }
}
