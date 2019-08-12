package com.zetcode.service;

import com.zetcode.TextModify;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class TextService {

    @Inject @TextModify
    private Event<String> textEvent;

    public String upper(String val) {

        var msg = String.format("%s modified to uppercase", val);

        textEvent.fire(msg);

        return val.toUpperCase();
    }
}
