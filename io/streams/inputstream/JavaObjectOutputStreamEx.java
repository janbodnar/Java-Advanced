package com.zetcode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class JavaObjectOutputStreamEx {

    public static void main(String[] args) throws IOException {

        String fileName = "src/resources/myfile.dat";

        try (OutputStream fis = new FileOutputStream(fileName);
                ObjectOutputStream out = new ObjectOutputStream(fis)) {

            List<Country> countries = new ArrayList<>();
            countries.add(new Country("Slovakia", 5429000));
            countries.add(new Country("Norway", 5271000));
            countries.add(new Country("Croatia", 4225000));
            countries.add(new Country("Russia", 143439000));

            out.writeObject(countries);
        }
    }
}
