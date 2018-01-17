package com.zetcode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JavaReadWriteXmlJaxbEx {

    private static final String BOOKSTORE_XML = "src/main/resources/bookstore.xml";

    public static void main(String[] args) throws JAXBException, IOException {

        ArrayList<Book> bookList = new ArrayList<>();

        // create books
        Book book1 = new Book();
        book1.setIsbn("978-0060554736");
        book1.setName("The Game");
        book1.setAuthor("Neil Strauss");
        book1.setPublisher("Harpercollins");
        bookList.add(book1);

        Book book2 = new Book();
        book2.setIsbn("978-3832180577");
        book2.setName("Feuchtgebiete");
        book2.setAuthor("Charlotte Roche");
        book2.setPublisher("Dumont Buchverlag");
        bookList.add(book2);

        // create bookstore, assigning book
        BookStore bookstore = new BookStore();
        bookstore.setName("Fraport Bookstore");
        bookstore.setLocation("Livres belles");
        bookstore.setBookList(bookList);

        // create JAXB context and instantiate marshaller
        JAXBContext context = JAXBContext.newInstance(BookStore.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // Write to System.out
        m.marshal(bookstore, System.out);

        // Write to File
        m.marshal(bookstore, new File(BOOKSTORE_XML));

        // get variables from our XML file, created before
        System.out.println();
        System.out.println("Output from our XML File: ");
        Unmarshaller um = context.createUnmarshaller();
        
        BookStore bookstore2 = (BookStore) um.unmarshal(new InputStreamReader(
                new FileInputStream(BOOKSTORE_XML), StandardCharsets.UTF_8));
        ArrayList<Book> list = bookstore2.getBooksList();
        
        list.forEach((book) -> {
            System.out.println(book);
        });
    }
}
