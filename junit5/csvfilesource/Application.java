package com.zetcode;

import com.zetcode.utils.MathUtils;

public class Application {

    public static void main(String[] args) {

        int x = 2;
        int y = 3;
        int z = 4;

        int sum = MathUtils.sum(x, y, z);

        System.out.printf("%d + %d + %d = %d%n",x,y, z, sum );
    }
}
