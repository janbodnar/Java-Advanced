package com.zetcode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyHandler extends DefaultHandler {

    private List<User> users = new ArrayList<>();
    private User user;

    private boolean bfn = false;
    private boolean bln = false;
    private boolean boc = false;

    public List<User> parseUsers() throws ParserConfigurationException,
            SAXException, IOException {

        String fileName = "src/main/resources/users.xml";
        
        File xmlDocument = Paths.get(fileName).toFile();

        // SAX Factory
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        // Parsing the document
        saxParser.parse(xmlDocument, this);

        return users;
    }

    @Override
    public void startElement(String uri, String localName,
            String qName, Attributes attributes) throws SAXException {

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
    public void characters(char[] ch, int start, int length) throws SAXException {

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
    public void endElement(String uri, String localName,
            String qName) throws SAXException {

        if ("user".equals(qName)) {
            users.add(user);
        }
    }
}
