package com.zetcode.provider;

import javax.enterprise.inject.Alternative;
import javax.inject.Named;

//@Alternative
@Named("HelloMessageProvider")
public class HelloMessageProvider implements MessageProvider {

    public String getMessage() {

        return "Hello there";
    }
}
