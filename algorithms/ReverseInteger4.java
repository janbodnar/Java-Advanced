package com.zetcode;

public class ReverseInteger4 {

    public static void main(String[] args) {

        int value = 73203000;

        var str = String.valueOf(value);
        var sb = new StringBuilder(str).reverse();

        // removing possible leading zeros
        int i = 0;

        while (sb.charAt(i) == '0') {
            // does not work with deleteCharAt
            // val2.deleteCharAt(0);
            i++;
        }

        if (i > 0) {

            sb.delete(0, i);
        }

        System.out.println(sb.toString());
    }
}
