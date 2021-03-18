package com.zetcode;

import java.util.List;

public class StringBlank {

    public static void main(String[] args) {

        var data = List.of("sky", "\n\n", "  ", "blue", "\t\t", "", "sky");

        for (int i=0; i<data.size(); i++) {

            var e = data.get(i);

            if (e.isBlank()) {
                System.out.printf("element with index %d is blank%n", i);
            } else {

                System.out.println(data.get(i));
            }
        }
    }
}
