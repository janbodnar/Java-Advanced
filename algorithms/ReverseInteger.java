package com.zetcode;

public class ReverseInteger {

    public static void main(String[] args) {

        int val = 73230;

        var str = String.valueOf(val);
        byte[] data = str.getBytes();

        byte[] data2 = new byte[data.length];

        for (int i = 0; i < data.length; i++) {

            data2[i] = data[data.length - i - 1];
        }

        var str2 = new String(data2);
        int val2 = Integer.parseInt(str2);

        System.out.println(val2);
    }
}
