package com.zetcode.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.validator.routines.UrlValidator;

public class WebPageReader {

    private String webpage;
    private String content;

    public WebPageReader setWebPageName(String name) {

        webpage = name;
        return this;
    }

    public String getWebPageContent() {

        try {

            boolean valid = validateUrl(webpage);

            if (!valid) {

                content = "Invalid URL; use http(s)://www.example.com format";
                return content;
            }

            URL url = new URL(webpage);

            try (InputStream is = url.openStream();
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(is, StandardCharsets.UTF_8))) {

                content = br.lines().collect(
                      Collectors.joining(System.lineSeparator()));
            }

        } catch (IOException ex) {

            content = String.format("Cannot read webpage %s", ex);
            Logger.getLogger(WebPageReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return content;
    }

    private boolean validateUrl(String webpage) {

        UrlValidator urlValidator = new UrlValidator();

        return urlValidator.isValid(webpage);
    }
}
