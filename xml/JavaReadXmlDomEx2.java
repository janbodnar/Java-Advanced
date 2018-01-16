package com.zetcode;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class JavaReadXmlDomEx2 {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        String fileName = "src/main/resources/users.xml";

        File file = new File(fileName);

        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();

        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();

        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

        if (doc.hasChildNodes()) {

            printNode(doc.getChildNodes());
        }
    }

    private static void printNode(NodeList nodeList) {

        for (int count = 0; count < nodeList.getLength(); count++) {

            Node tempNode = nodeList.item(count);

            // make sure it's element node.
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

                // get node name and value
                System.out.println(tempNode.getNodeName());
                System.out.println(tempNode.getTextContent());

                if (tempNode.hasAttributes()) {

                    // get attributes names and values
                    NamedNodeMap nodeMap = tempNode.getAttributes();

                    System.out.println("Attributes:");

                    for (int i = 0; i < nodeMap.getLength(); i++) {

                        Node node = nodeMap.item(i);
                        System.out.printf("%s ", node.getNodeName());
                        System.out.println(node.getNodeValue());
                        System.out.println("/Attributes:");
                    }
                } else {

                    System.out.println("--------------------");
                }

                if (tempNode.hasChildNodes()) {

                    // loop again if has child nodes
                    printNode(tempNode.getChildNodes());
                }

                System.out.println("**********************");
            }
        }
    }
}
