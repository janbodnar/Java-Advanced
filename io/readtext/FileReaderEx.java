package com.zetcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderEx {

    public static void main(String[] args) throws IOException {

        var fileName = "src/resources/thermopylae.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            var sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {

                sb.append(line);
                sb.append(System.lineSeparator());
            }

            System.out.println(sb);
        }
    }
}
