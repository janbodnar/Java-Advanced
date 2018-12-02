package com.zetcode;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class JavaPropertiesEx {

    public static void main(String[] args) throws IOException {

        var props = new Properties();

        try (var fis = new FileInputStream("src/resource/db.properties")) {

            props.load(fis);
        }

        var url = props.getProperty("db.url");
        var user = props.getProperty("db.user");
        var password = props.getProperty("db.password");

        System.out.println(url);
        System.out.println(user);
        System.out.println(password);
    }
}
