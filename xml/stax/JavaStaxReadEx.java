package com.zetcode;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

// TODO add new nationality parameter to read

public class JavaStaxReadEx {

    public static void main(String[] args) throws IOException, XMLStreamException {

        boolean bFirstName = false;
        boolean bLastName = false;
        boolean bOccupation = false;

        var factory = XMLInputFactory.newInstance();

        try(var fis = new FileInputStream("src/main/resources/users.xml")) {

            var eventReader = factory.createXMLEventReader(fis, StandardCharsets.UTF_8.name());

            while (eventReader.hasNext()) {

                var event = eventReader.nextEvent();

                switch (event.getEventType()) {

                    case XMLStreamConstants.START_ELEMENT:

                        var startElement = event.asStartElement();
                        var tagName = startElement.getName().getLocalPart();

                        System.out.println("tag: " + tagName);

                        if (tagName.equalsIgnoreCase("user")) {

                            System.out.println("Start Element : user");

                            var attributes = startElement.getAttributes();
                            var id = attributes.next().getValue();

                            System.out.println("Id: " + id);
                        } else if (tagName.equalsIgnoreCase("firstname")) {
                            bFirstName = true;
                        } else if (tagName.equalsIgnoreCase("lastname")) {
                            bLastName = true;
                        } else if (tagName.equalsIgnoreCase("occupation")) {
                            bOccupation = true;
                        }

                        break;

                    case XMLStreamConstants.CHARACTERS:

                        var characters = event.asCharacters();

                        if (bFirstName) {
                            System.out.println("First name: " + characters.getData());
                            bFirstName = false;
                        }
                        if (bLastName) {
                            System.out.println("Last name: " + characters.getData());
                            bLastName = false;
                        }
                        if (bOccupation) {
                            System.out.println("Occupation: " + characters.getData());
                            bOccupation = false;
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:

                        var endElement = event.asEndElement();

                        if (endElement.getName().getLocalPart().equalsIgnoreCase("user")) {
                            System.out.println("End element : user");
                            System.out.println();
                        }
                        break;
                }
            }

            eventReader.close();
        }
    }
}
