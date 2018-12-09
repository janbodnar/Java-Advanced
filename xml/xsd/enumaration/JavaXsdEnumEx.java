package com.zetcode;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;

public class JavaXsdEnumEx {

    public static void main(String[] args) throws SAXException, IOException {

        var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        var schema = factory.newSchema(JavaXsdEnumEx.class.getResource("/myschema.xsd"));

        var validator = schema.newValidator();

        var is = JavaXsdEnumEx.class.getResourceAsStream("/students.xml");
        var streamSource = new StreamSource(is);

        validator.validate(streamSource);

        System.out.println("Schema validation succeeded");
    }
}
