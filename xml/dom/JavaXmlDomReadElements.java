package com.zetcode;

import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class JavaXmlDomReadElements {

    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException {

        var factory = DocumentBuilderFactory.newInstance();
        var documentBuilder = factory.newDocumentBuilder();
        var document = documentBuilder.parse("src/main/resources/continents.xml");

        var docTrav = (DocumentTraversal) document;

        var it = docTrav.createNodeIterator(document.getDocumentElement(),
                NodeFilter.SHOW_ELEMENT, null, true);

        int c = 1;

        for (var node = it.nextNode(); node != null; node = it.nextNode()) {

            var name = node.getNodeName();

            System.out.printf("%d %s%n", c, name);
            c++;
        }
    }
}
