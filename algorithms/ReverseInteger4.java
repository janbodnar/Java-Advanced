package com.zetcode;

public class ReverseInteger4 {

    public static void main(String[] args) {

        var value = "73203000";
        var sb = new StringBuilder(value).reverse();

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
