package com.zetcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JavaInputStreamReaderEx3 {

    public static void main(String[] args) throws MalformedURLException, IOException {

        StringBuilder sb;

        URL url = new URL("https://www.wikipedia.org");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8))) {
            String line;

            sb = new StringBuilder();

            while ((line = br.readLine()) != null) {

                sb.append(line);
                sb.append(System.lineSeparator());
            }
        }

        System.out.println(sb.toString());
    }
}
