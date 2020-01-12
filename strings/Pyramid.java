package com.zetcode;

public class Pyramid {

    public static void main(String[] args) {

        String val = "0";
        String empty = " ";

        int len = 30;

        for (int i = 1; i < len; i += 2) {

            System.out.printf("%s%s%n", empty.repeat(len-i / 2), val.repeat(i));
        }
    }
}
