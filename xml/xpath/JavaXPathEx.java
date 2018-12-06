package com.zetcode;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;

public class JavaXPathEx {

    public static void main(String[] args) throws ParserConfigurationException,
            IOException, SAXException, XPathExpressionException {

        var factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // required

        var builder = factory.newDocumentBuilder();
        var doc = builder.parse("src/main/resources/inventory.xml");

        var xPathFactory = XPathFactory.newInstance();
        var xpath = xPathFactory.newXPath();

        System.out.println("Book titles written in 2018");

        var expr = xpath.compile("//book[@year=2018]/title/text()");
        var result = expr.evaluate(doc, XPathConstants.NODESET);

        var nodes = (NodeList) result;

        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i).getNodeValue());
        }

        System.out.println("\nBook titles cheaper than 40");

        expr = xpath.compile("//book[price<40]/title/text()");
        result = expr.evaluate(doc, XPathConstants.NODESET);
        nodes = (NodeList) result;

        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i).getNodeValue());
        }

        System.out.println("\nReading the comment");

        expr = xpath.compile("//inventory/comment()");
        result = expr.evaluate(doc, XPathConstants.STRING);

        var comment = (String) result;
        System.out.println(comment.trim());

        System.out.println("\nCount all book titles ");

        expr = xpath.compile("count(//book/title)");
        result = expr.evaluate(doc, XPathConstants.NUMBER);

        var nOfTitles = (Double) result;
        System.out.printf("There are %d book titles%n", nOfTitles.intValue());

        System.out.println("\nCount all publishers");

        // XSLT 2.0 has better syntax for unique
        expr = xpath.compile("count(//book/publisher[not(following::book/publisher/text() = text())])");
        result = expr.evaluate(doc, XPathConstants.NUMBER);

        var nOfPublishers = (Double) result;
        System.out.printf("There are %d publishers%n", nOfPublishers.intValue());

        System.out.println("\nList all authors (possible duplicates)");

        expr = xpath.compile("//book/author/text()");
        result = expr.evaluate(doc, XPathConstants.NODESET);
        nodes = (NodeList) result;

        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i).getNodeValue());
        }

        System.out.println("\nFound book titles containing Python");

        expr = xpath.compile("//book[contains(title, 'Python')]");
        result = expr.evaluate(doc, XPathConstants.NODESET);
        nodes = (NodeList) result;

        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i)
                    .getChildNodes()
                    .item(1)                //node <title> is on first index
                    .getTextContent());
        }


        System.out.println("\nGet book title added in last node");

        expr = xpath.compile("//book[last()]/title/text()");
        result = expr.evaluate(doc, XPathConstants.NODESET);
        nodes = (NodeList) result;

        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i).getNodeValue());
        }

    }
}
