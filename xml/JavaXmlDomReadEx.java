package com.zetcode;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class JavaXmlDomReadEx {

    public static void main(String argv[]) throws SAXException,
            IOException, ParserConfigurationException {

        File xmlFile = new File("src/main/resources/users.xml");
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

        NodeList nList = doc.getElementsByTagName("user");

        for (int i = 0; i < nList.getLength(); i++) {

            Node nNode = nList.item(i);

            System.out.println("\nCurrent Element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element elem = (Element) nNode;

                String uid = elem.getAttribute("id");

                Node node1 = elem.getElementsByTagName("firstname").item(0);
                String fname = node1.getTextContent();

                Node node2 = elem.getElementsByTagName("lastname").item(0);
                String lname = node2.getTextContent();

                Node node3 = elem.getElementsByTagName("occupation").item(0);
                String occup = node3.getTextContent();

                System.out.printf("User id: %s%n", uid);
                System.out.printf("First name: %s%n", fname);
                System.out.printf("Last name: %s%n", lname);
                System.out.printf("Occupation: %s%n", occup);
            }
        }
    }
}
