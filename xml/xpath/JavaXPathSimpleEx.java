package com.zetcode;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;

public class JavaXPathSimpleEx {

    // XPath syntax: https://devhints.io/xpath

    /* TODO
        get second user
        get all users
        get id attribute of last user
    */

    public static void main(String[] args) throws ParserConfigurationException, IOException,
            SAXException, XPathExpressionException {

        var factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // required

        var builder = factory.newDocumentBuilder();
        var doc = builder.parse("src/main/resources/users.xml");

        var xPathFactory = XPathFactory.newInstance();
        var xpath = xPathFactory.newXPath();

        var expr = xpath.compile("/users/user[1]/firstname/text()");
        var firstName = expr.evaluate(doc, XPathConstants.STRING);

        expr = xpath.compile("/users/user[1]/lastname/text()");
        var lastName = expr.evaluate(doc, XPathConstants.STRING);

        System.out.printf("The first user is %s %s%n", firstName, lastName);
    }
}
