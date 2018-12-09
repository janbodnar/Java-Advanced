package com.zetcode;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JacksonJsonTreeReadEx {

    public static void main(String[] args) throws IOException {

        var mapper = new ObjectMapper();

        var fileName = new File("src/main/resources/users.json");

        var rootNode = mapper.readTree(fileName);

        var it = rootNode.elements();

        while (it.hasNext()) {

            var nextNode = it.next();

            var idNode = nextNode.path("id");
            var fnNode = nextNode.path("firstname");
            var lnNode = nextNode.path("lastname");
            var ocNode = nextNode.path("occupation");

            System.out.printf("%d %s %s %s %n", idNode.intValue(),
                    fnNode.textValue(), lnNode.textValue(), ocNode.textValue());
        }
    }
}
