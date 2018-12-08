package com.zetcode;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;

public class JavaXmlXsdValidationEx {

    public static void main(String[] args) throws SAXException, IOException {

        System.out.println("Validating  XML against XSD Schema");

        var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        var schema = factory.newSchema(JavaXmlXsdValidationEx.class.getResource("/myschema.xsd"));

        var validator = schema.newValidator();

        var is = JavaXmlXsdValidationEx.class.getResourceAsStream("/books.xml");
        var streamSource = new StreamSource(is);

        validator.validate(streamSource);

        System.out.println("Schema validation succeeded");

    }
}
