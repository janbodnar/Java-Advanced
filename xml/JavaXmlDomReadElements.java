package com.zetcode;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

public class JavaXmlDomReadElements {

    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder loader = factory.newDocumentBuilder();
        Document document = loader.parse("src/main/resources/continents.xml");

        DocumentTraversal trav = (DocumentTraversal) document;

        NodeIterator it = trav.createNodeIterator(document.getDocumentElement(), 
                NodeFilter.SHOW_ELEMENT, null, true);

        int c = 1;
        
        for (Node node = it.nextNode(); node != null;
                node = it.nextNode()) {

            String name = node.getNodeName();
            
            System.out.printf("%d %s%n", c, name);
            c++;         
        }
    }
}
