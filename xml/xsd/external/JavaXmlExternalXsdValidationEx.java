package com.zetcode;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;

public class JavaXmlExternalXsdValidationEx {

    // The example uses external XSD for XML validation
    // If we do not specify the schema in code, Java looks for
    // a schema inside the XML document (xsi:schemaLocation)

    public static void main(String[] args) throws SAXException, IOException {

        System.out.println("Validating XML against external XSD Schema");

        var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        var schema = factory.newSchema();
        
        var validator = schema.newValidator();
        var streamSource = new StreamSource(new File("src/main/resources/books2.xml"));

        validator.validate(streamSource);

        System.out.println("Schema validation succeeded");
    }

}
