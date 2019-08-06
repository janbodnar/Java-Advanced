package com.zetcode;

public class ReverseInteger {

    public static void main(String[] args) {

        int val = 7320300;
        var str = String.valueOf(val);

        var val2 = str.chars().mapToObj(i -> (char) i).collect(StringBuilder::new,
                (b, c) -> b.insert(0, (char) c), (b1, b2) -> b1.insert(0, b2));


        // removing possible leading zeros
        int i = 0;

        while (val2.charAt(i) == '0') {
            // does not work with deleteCharAt
            // val2.deleteCharAt(0);
            i++;
        }

        if (i > 0) {

            val2.delete(0, i);
        }

        System.out.println(val2.toString());
    }
}
