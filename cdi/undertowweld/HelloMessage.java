package com.zetcode.bean;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

public class HelloMessage {

    @Named("hello")
    @Produces
    public String message() {
        return "Hello there";
    }
}
