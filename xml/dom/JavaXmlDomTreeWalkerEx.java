package com.zetcode;

import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class JavaXmlDomTreeWalkerEx {

    public static void main(String[] args) throws SAXException, IOException,
            ParserConfigurationException {

        var factory = DocumentBuilderFactory.newInstance();
        var loader = factory.newDocumentBuilder();
        var document = loader.parse("src/main/resources/continents.xml");

        var traversal = (DocumentTraversal) document;

        var walker = traversal.createTreeWalker(
                document.getDocumentElement(),
                NodeFilter.SHOW_ELEMENT | NodeFilter.SHOW_TEXT, null, true);

        traverseLevel(walker, "");
    }

    private static void traverseLevel(TreeWalker walker, String indent) {

        var node = walker.getCurrentNode();

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            System.out.println(indent + node.getNodeName());
        }

        if (node.getNodeType() == Node.TEXT_NODE) {

            var content_trimmed = node.getTextContent().trim();

            if (content_trimmed.length() > 0) {
                System.out.print(indent);
                System.out.printf("%s%n", content_trimmed);
            }
        }

        for (var n = walker.firstChild(); n != null;
             n = walker.nextSibling()) {

            traverseLevel(walker, indent + "  ");
        }

        // return position to the current (level up):
        walker.setCurrentNode(node);
    }
}
