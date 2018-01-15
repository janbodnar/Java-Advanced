package com.zetcode;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleEx {

    static public void main(String[] args) {

        Locale[] locales = {
            Locale.GERMAN,
            new Locale("sk", "SK"),
            Locale.ENGLISH
        };

        System.out.println("w1:");

        for (Locale locale : locales) {

            getWord(locale, "w1");
        }

        System.out.println("w2:");

        for (Locale locale : locales) {

            getWord(locale, "w2");
        }
    }

    static void getWord(Locale curLoc, String key) {

        ResourceBundle words
                = ResourceBundle.getBundle("resources/words", curLoc);

        String value = words.getString(key);

        System.out.printf("Locale: %s, Value: %s %n", curLoc.toString(), value);

    }
}
