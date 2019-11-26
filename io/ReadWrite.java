package com.zetcode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
package com.zetcode;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

public class ReadWrite {

    public static void main(String[] args) throws IOException {

        var content = Files.readString(Paths.get("src/resources/test.txt"));
        System.out.println(content);

        System.out.println("***********************************");

        var lines = Files.readAllLines(Paths.get("src/resources/test.txt"));
        System.out.println(lines);

        System.out.println("***********************************");

        var path = Paths.get("src/resources/test.txt");
        try (var br = Files.newBufferedReader(path)) {

            String line;
            while ((line = br.readLine()) != null) {

                System.out.println(line);
            }
        }
        
        var line1 = "C# is cool";
        var line2 = Instant.now().toString();

        try (var br = new BufferedWriter(new FileWriter(("src/resources/test2.txt")))) {

            br.write(line1);
            br.newLine();
            br.write(line2);
            br.newLine();
        }

        try (var br = new BufferedReader(new FileReader(("src/resources/test.txt")))) {

            String line;
            while ((line = br.readLine()) != null) {

                System.out.println(line);
            }
        }

        var text = "old falcon";
        var data = text.getBytes();

        try (var fos = new FileOutputStream("src/resources/test.txt")) {
            fos.write(data);
        }

        try (var fr = new FileWriter("src/resources/test2.txt")) {
            fr.write("old falcon");
        }

        try (var pw = new PrintWriter("src/resources/test.txt")) {
            pw.println("Java is cool");
            pw.printf("Today is %s%n", Instant.now());

        }
    }
}

