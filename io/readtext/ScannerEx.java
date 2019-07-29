package com.zetcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ScannerEx {

    public static void main(String[] args) throws FileNotFoundException {

        var fileName = "src/resources/thermopylae.txt";

        try (var scanner = new Scanner(new File(fileName))) {

            while (scanner.hasNext()) {

                String line = scanner.nextLine();
                System.out.println(line);
            }
        }
    }
}
