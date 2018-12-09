package com.zetcode;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MyHandler extends DefaultHandler {

    private List<User> users = new ArrayList<>();
    private User user;

    private boolean bfn = false;
    private boolean bln = false;
    private boolean boc = false;

    public List<User> parseUsers() throws ParserConfigurationException,
            SAXException, IOException {

        var fileName = "src/main/resources/users.xml";

        var xmlDocument = Paths.get(fileName).toFile();

        var factory = SAXParserFactory.newInstance();
        var saxParser = factory.newSAXParser();

        saxParser.parse(xmlDocument, this);

        return users;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)  {

        if ("user".equals(qName)) {
            user = new User();
        }

        switch (qName) {

            case "firstname":
                bfn = true;
                break;

            case "lastname":
                bln = true;
                break;

            case "occupation":
                boc = true;
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {

        if (bfn) {
            user.setFirstName(new String(ch, start, length));
            bfn = false;
        }

        if (bln) {
            user.setLastName(new String(ch, start, length));
            bln = false;
        }

        if (boc) {
            user.setOccupation(new String(ch, start, length));
            boc = false;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)  {

        if ("user".equals(qName)) {
            users.add(user);
        }
    }
}
