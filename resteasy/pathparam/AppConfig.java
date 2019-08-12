package com.zetcode.conf;

import com.zetcode.wx.ReverseResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class AppConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> set = new HashSet<>();
        set.add(ReverseResource.class);
        return set;
    }
}
