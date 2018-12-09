package com.zetcode;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class JavaReadXmlSaxEx {

    public static void main(String[] args)
            throws ParserConfigurationException, SAXException, IOException {

        var handler = new MyHandler();
        var lines = handler.parseUsers();

        lines.forEach(System.out::println);
    }
}
