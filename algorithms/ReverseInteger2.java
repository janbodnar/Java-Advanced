package com.zetcode;

public class ReverseInteger2 {

    public static void main(String[] args) {

        int val = 7320300;

        var str = String.valueOf(val);

        char[] data = str.toCharArray();
        char[] data2 = new char[data.length];

        for (int i = 0; i < data.length; i++) {

            data2[i] = data[data.length - i - 1];
        }

        // remove possible leading zeros
        int j = 0;
        while (data2[j] == '0') {

            data2[j] = ' ';
            j++;
        }

        var str2 = new String(data2);
        System.out.println(str2.stripLeading());
    }
}
