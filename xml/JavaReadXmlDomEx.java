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

public class JavaReadXmlDomEx {

    public static void main(String argv[]) throws SAXException,
            IOException, ParserConfigurationException {

        File xmlFile = new File("src/main/resources/users.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

        NodeList nList = doc.getElementsByTagName("user");

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);

            System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element elem = (Element) nNode;

                System.out.println("User id : " + elem.getAttribute("id"));
                System.out.println("First Name : " + elem.getElementsByTagName("firstname").item(0).getTextContent());
                System.out.println("Last Name : " + elem.getElementsByTagName("lastname").item(0).getTextContent());
                System.out.println("Occupation : " + elem.getElementsByTagName("occupation").item(0).getTextContent());

            }
        }
    }
}
