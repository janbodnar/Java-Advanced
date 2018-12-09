package com.zetcode;

import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class JavaXmlDomReadText {

    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException {

        var factory = DocumentBuilderFactory.newInstance();
        var loader = factory.newDocumentBuilder();
        var document = loader.parse("src/main/resources/continents.xml");

        var traversal = (DocumentTraversal) document;

        var iterator = traversal.createNodeIterator(
                document.getDocumentElement(), NodeFilter.SHOW_TEXT, null, true);

        for (var n = iterator.nextNode(); n != null; n = iterator.nextNode()) {

            var text = n.getTextContent().trim();

            if (!text.isEmpty()) {
                System.out.println(text);
            }
        }
    }
}
