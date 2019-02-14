package com.zetcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// This program creates and outputs an environment variable
// It runs a Windows command

public class ProcessBuilderEnvEx2 {

    public static void main(String[] args) throws IOException {

        var pb = new ProcessBuilder();
        var env = pb.environment();

        env.put("mode", "development");

        pb.command("cmd.exe", "/c", "echo", "%mode%");

        var process = pb.start();

        try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {

                System.out.println(line);
            }
        }
    }
}
