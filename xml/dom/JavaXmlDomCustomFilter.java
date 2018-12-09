package com.zetcode;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class JavaXmlDomCustomFilter {

    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException {

        var factory = DocumentBuilderFactory.newInstance();
        var documentBuilder = factory.newDocumentBuilder();
        var document = documentBuilder.parse("src/main/resources/continents.xml");

        var documentTraversal = (DocumentTraversal) document;

        var filter = new MyFilter();

        var it = documentTraversal.createNodeIterator(document.getDocumentElement(),
                NodeFilter.SHOW_ELEMENT, filter, true);

        for (var node = it.nextNode(); node != null; node = it.nextNode()) {

            var name = node.getNodeName();
            var text = node.getTextContent().trim().replaceAll("\\s+", " ");
            System.out.printf("%s: %s%n", name, text);
        }
    }

    static class MyFilter implements NodeFilter {

        @Override
        public short acceptNode(Node thisNode) {
            if (thisNode.getNodeType() == Node.ELEMENT_NODE) {

                var e = (Element) thisNode;
                var nodeName = e.getNodeName();

                if ("slovakia".equals(nodeName) || "poland".equals(nodeName)) {
                    return NodeFilter.FILTER_ACCEPT;
                }
            }

            return NodeFilter.FILTER_REJECT;
        }
    }
}
