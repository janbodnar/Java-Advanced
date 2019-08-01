package com.zetcode;

import java.util.Arrays;

public class GetBytes {

    public static void main(String[] args) {

        var text = "Wuthering Heights";

        byte[] bytes = text.getBytes();
        System.out.println(Arrays.toString(bytes));

        var text2 = new String(bytes);
        System.out.println(text2);
    }
}
