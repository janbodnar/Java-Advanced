package com.zetcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class JavaInputStreamURL {

    public static void main(String[] args) throws IOException {

        String webSite = "http://www.something.com";
        URL url = new URL(webSite);

        try (InputStream is = url.openStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is))) {

            br.lines().forEach(System.out::println);            
        }
    }
}
