package com.zetcode;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.zetcode.bean.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JacksonObject2XmlEx {

    public static void main(String[] args) throws IOException {

        var mapper = new XmlMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        var user = new User("John", "Doe", "johndoe@gmail.com");

        // serialize to XML string
        var output = mapper.writeValueAsString(user);
        System.out.println(output);

        // serialize to XML file
        var path = Paths.get("src/main/resources/user.xml");
        mapper.writeValue(Files.newOutputStream(path), user);
    }
}
