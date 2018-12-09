package com.zetcode;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class JavaXmlDomWrite {

    public static void main(String[] args) throws ParserConfigurationException,
            TransformerException {

        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();
        var doc = builder.newDocument();

        var root = doc.createElementNS("zetcode.com", "users");

        doc.appendChild(root);

        root.appendChild(createUser(doc, "1", "Robert", "Brown", "programmer"));
        root.appendChild(createUser(doc, "2", "Pamela", "Kyle", "writer"));
        root.appendChild(createUser(doc, "3", "Peter", "Smith", "teacher"));

        var transformerFactory = TransformerFactory.newInstance();
        var transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        var source = new DOMSource(doc);

        var myFile = new File("src/main/resources/users.xml");

        var console = new StreamResult(System.out);
        var file = new StreamResult(myFile);

        transformer.transform(source, console);
        transformer.transform(source, file);
    }

    private static Node createUser(Document doc, String id, String firstName,
                                   String lastName, String occupation) {

        var user = doc.createElement("user");

        user.setAttribute("id", id);
        user.appendChild(createUserElement(doc, "firstname", firstName));
        user.appendChild(createUserElement(doc, "lastname", lastName));
        user.appendChild(createUserElement(doc, "occupation", occupation));

        return user;
    }

    private static Node createUserElement(Document doc, String name,
                                          String value) {

        var node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));

        return node;
    }
}
