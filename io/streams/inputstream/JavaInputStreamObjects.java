package com.zetcode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class JavaInputStreamObjects {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        String fileName = "src/resources/myfile.dat";
        
        try (InputStream fis = new FileInputStream(fileName);
                ObjectInputStream oin = new ObjectInputStream(fis)) {
            
            List<Country> countries = (List<Country>) oin.readObject();
            
            countries.forEach(System.out::println);
        }
    }
}
