package com.zetcode;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class JavaWriteXmlDomEx {

    public static void main(String[] args) throws ParserConfigurationException, TransformerException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        //add elements to Document
        Element rootElement = doc.createElementNS("zetcode.com", "users");
        
        //append root element to document
        doc.appendChild(rootElement);

        //append first child element to root element
        rootElement.appendChild(createUser(doc, "1", "Robert", "Brown", "programmer"));
        rootElement.appendChild(createUser(doc, "2", "Pamela", "Kyle", "writer"));
        rootElement.appendChild(createUser(doc, "3", "Lucy", "Smith", "teacher"));

        // output to file and console
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        
        // pretty print
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(doc);

        // write to console or file
        File myFile = new File("src/main/resources/users.xml");
        StreamResult console = new StreamResult(System.out);
        StreamResult file = new StreamResult(myFile);

        // write data
        transformer.transform(source, console);
        transformer.transform(source, file);
    }

    private static Node createUser(Document doc, String id, String firstName, 
            String lastName, String occupation) {
        
        Element user = doc.createElement("user");

        user.setAttribute("id", id);
        user.appendChild(createUserElements(doc, "firstname", firstName));
        user.appendChild(createUserElements(doc, "lastname", lastName));
        user.appendChild(createUserElements(doc, "occupation", occupation));

        return user;
    }

    private static Node createUserElements(Document doc, String name, 
            String value) {

        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));

        return node;
    }
}
