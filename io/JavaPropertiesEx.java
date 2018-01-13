package com.zetcode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class JavaPropertiesEx {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Properties props = new Properties();
        FileInputStream fis = new FileInputStream("src/resources/db.properties");

        props.load(fis);

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String passwd = props.getProperty("db.passwd");
        
        System.out.println(url);
        System.out.println(user);
        System.out.println(passwd);
    }
}
