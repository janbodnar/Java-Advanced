package com.zetcode;

import java.util.StringJoiner;

public class RemoveStringSpaces {

    public static void main(String[] args) {

        String msg = """
                Today              is a     beautiful day.
                There     are no             clouds in          the                sky.
                """;

        String[] parts = msg.split("[\\s]{2,}");

        var joiner = new StringJoiner(" ");

        for (var cs: parts) {
            
            joiner.add(cs);
        }

        String modified = joiner.toString();
        System.out.println(modified);
        
        //        String modified = msg.replaceAll("[\\s]{2,}", " ");
        //        System.out.println(modified);

        //        System.out.println(msg);
    }
}
