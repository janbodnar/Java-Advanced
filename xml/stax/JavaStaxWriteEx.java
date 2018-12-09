package com.zetcode;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// TODO write new user John Doe, gardener

public class JavaStaxWriteEx {

    public static void main(String[] args) throws XMLStreamException, IOException {

        var path = Paths.get("src/main/resources/users.xml");

        var factory = XMLOutputFactory.newInstance();

        try (var bwr = Files.newBufferedWriter(path)) {

            var writer = factory.createXMLStreamWriter(bwr);

            writer.writeStartDocument();
            writer.writeCharacters("\n");
            writer.writeStartElement("users");
            writer.writeCharacters("\n");

            // user 1
            writer.writeCharacters("\n");
            writer.writeStartElement("user");
            writer.writeAttribute("id", "1");

            writer.writeCharacters("\n");
            writer.writeStartElement("firstname");
            writer.writeCharacters("Robert");
            writer.writeEndElement();

            writer.writeCharacters("\n");
            writer.writeStartElement("lastname");
            writer.writeCharacters("Brown");
            writer.writeEndElement();

            writer.writeCharacters("\n");
            writer.writeStartElement("occupation");
            writer.writeCharacters("programmer");
            writer.writeEndElement();

            writer.writeCharacters("\n");
            writer.writeEndElement();

            // end of user 1

            writer.writeCharacters("\n\n");

            // user 2

            writer.writeStartElement("user");
            writer.writeAttribute("id", "2");

            writer.writeCharacters("\n");
            writer.writeStartElement("firstname");
            writer.writeCharacters("Pamela");
            writer.writeEndElement();

            writer.writeCharacters("\n");
            writer.writeStartElement("lastname");
            writer.writeCharacters("Kyle");
            writer.writeEndElement();

            writer.writeCharacters("\n");
            writer.writeStartElement("occupation");
            writer.writeCharacters("writer");
            writer.writeEndElement();

            writer.writeCharacters("\n");

            writer.writeEndElement();
            writer.writeCharacters("\n\n");

            // end of user 2

            writer.writeEndElement();
            writer.writeEndDocument();
            writer.writeCharacters("\n");

            writer.flush();
            writer.close();

        }
    }
}
