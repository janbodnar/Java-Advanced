package com.zetcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

// This program works with redirecting the error stream
// It runs a Linux command

public class ProcessBuilderRedirectEx {

    public static void main(String[] args) throws IOException {

        String[] list = {"ls", "-l", "test"};

        var pb = new ProcessBuilder(list);
//        pb.redirectErrorStream(true);

        var process = pb.start();


        var reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String line;

        try (var writer = Files.newBufferedWriter(Paths.get("error.txt"))) {

            while ((line = reader.readLine()) != null) {

                writer.append(line);
                writer.newLine();
            }
        }
    }
}
