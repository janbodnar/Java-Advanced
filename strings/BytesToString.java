package com.zetcode;

import java.util.Base64;

public class BytesToString {

    public static void main(String[] args) {

        byte[] data = {(byte) 0x47, (byte) 0x6f, (byte) 0x6c, (byte) 0x64,
                (byte) 0x65, (byte) 0x6e, (byte) 0x20, (byte) 0x65,
                (byte) 0x61, (byte) 0x67, (byte) 0x6c, (byte) 0x65};

        var text = new String(data);
        System.out.println(text);
    }
}
