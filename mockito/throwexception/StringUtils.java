package com.zetcode.util;

import java.util.Stack;

public class StringUtils {

    // uses Stack to reverse a string
    public String reverse(String original) {

        if (original == null) {

            throw new IllegalArgumentException("The word cannot be null");
        }

        char[] data = original.toCharArray();

        Stack<Character> stack = new Stack<>();

        for (char c: data) {

            stack.push(c);
        }

        char[] data2 = new char[data.length];

        int len = stack.size();

        for (int i = 0; i < len; i++) {

            data2[i] = stack.pop();
        }

        return new String(data2);
    }
}
