package com.zetcode.renderer;

import com.zetcode.provider.MessageProvider;

import javax.inject.Inject;
import javax.inject.Named;

public class ConsoleMessageRenderer implements MessageRenderer {

    @Inject
    @Named("TimeMessageProvider")
    private MessageProvider messageProvider;

    public void render() {

        var msg = messageProvider.getMessage();

        System.out.println(msg);
    }
}
