package com.zetcode;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JavaReadXmlJaxbEx {

    private static final String BOOKSTORE_XML = "src/main/resources/bookstore.xml";

    public static void main(String[] args) throws JAXBException, FileNotFoundException {

        // create JAXB context and unmarshaller
        var context = JAXBContext.newInstance(BookStore.class);
        var um = context.createUnmarshaller();

        var bookstore = (BookStore) um.unmarshal(new InputStreamReader(
                new FileInputStream(BOOKSTORE_XML), StandardCharsets.UTF_8));
        var bookList = bookstore.getBooksList();

        bookList.forEach((book) -> {
            System.out.println(book);
        });
    }
}

