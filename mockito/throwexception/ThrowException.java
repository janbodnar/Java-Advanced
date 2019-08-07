package com.zetcode;

import com.zetcode.util.StringUtils;

public class ThrowException {

    public static void main(String[] args) {

        var word = "falcon";

        var reversed = new StringUtils().reverse(word);
        System.out.println(reversed);
    }
}
