package com.zetcode;

import java.util.HashMap;
import java.util.Map;

public class RomanNumeralsEx {

    public static void main(String[] args) {

        Map<Integer, String> table = new HashMap<>() {{

            put(1, "I");
            put(2, "II");
            put(3, "III");
            put(4, "IV");
            put(5, "V");
            put(6, "VI");
            put(7, "VII");
            put(8, "VIII");
            put(9, "IX");
            put(10, "X");
            put(20, "XX");
            put(30, "XXX");
            put(40, "XL");
            put(50, "L");
            put(60, "LX");
            put(70, "LXX");
            put(80, "LXXX");
            put(90, "XC");
        }};

        int val = 87;

        if (table.containsKey(val)) {

            System.out.println(table.get(val));
        } else {

            int rem = val % 10;
            int num = val - rem;

            System.out.println(table.get(num) + table.get(rem));
        }

    }
}
