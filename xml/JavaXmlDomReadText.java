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

public class JavaXmlDomReadText {

    public static void main(String[] args) throws ParserConfigurationException, 
            SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder loader = factory.newDocumentBuilder();
        Document document = loader.parse("src/main/resources/continents.xml");

        DocumentTraversal traversal = (DocumentTraversal) document;

        NodeIterator iterator = traversal.createNodeIterator(
                document.getDocumentElement(), NodeFilter.SHOW_TEXT, null, true);

        for (Node n = iterator.nextNode(); n != null; n = iterator.nextNode()) {
            
            String text = n.getTextContent().trim();
            
            if (!text.isEmpty()) {
                System.out.println(text);
            }
        }
    }
}
