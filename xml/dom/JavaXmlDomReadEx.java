package com.zetcode;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class JavaXmlDomReadEx {

    public static void main(String argv[]) throws SAXException,
            IOException, ParserConfigurationException {

        var xmlFile = new File("src/main/resources/users.xml");

        var factory = DocumentBuilderFactory.newInstance();
        var dBuilder = factory.newDocumentBuilder();
        var doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

        // NodeList
        var nList = doc.getElementsByTagName("user");

        for (int i = 0; i < nList.getLength(); i++) {

            // Node
            var nNode = nList.item(i);

            System.out.println("\nCurrent Element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                var elem = (Element) nNode;

                var uid = elem.getAttribute("id");

                var node1 = elem.getElementsByTagName("firstname").item(0);
                var firstName = node1.getTextContent();

                var node2 = elem.getElementsByTagName("lastname").item(0);
                var lastName = node2.getTextContent();

                var node3 = elem.getElementsByTagName("occupation").item(0);
                var occupation = node3.getTextContent();

                System.out.printf("User id: %s%n", uid);
                System.out.printf("First name: %s%n", firstName);
                System.out.printf("Last name: %s%n", lastName);
                System.out.printf("Occupation: %s%n", occupation);
            }
        }
    }
}
