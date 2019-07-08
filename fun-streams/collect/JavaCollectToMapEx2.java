package com.zetcode;

import java.nio.CharBuffer;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// creating an ASCII table from a sequence of characters
public class JavaCollectToMapEx2 {

    public static void main(String[] args) {
    
        char[] letters = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        Map<Integer, Character> asciiMap = CharBuffer.wrap(letters)
                .chars()
                .mapToObj(e -> (char) e)
                .collect(Collectors.toMap(Object::hashCode, Function.identity()));

        System.out.println(asciiMap);
    }
}
