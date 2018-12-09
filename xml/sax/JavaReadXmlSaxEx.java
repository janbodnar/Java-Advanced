package com.zetcode;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class JavaReadXmlSaxEx  {

    public static void main(String[] args)
            throws ParserConfigurationException, SAXException, IOException {

        MyHandler handler = new MyHandler();
        List<User> lines = handler.parseUsers();
        
        lines.forEach(System.out::println);
    }
}
