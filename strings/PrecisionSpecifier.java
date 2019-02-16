package com.zetcode;

public class PrecisionSpecifier {

    public static void main(String[] args) {
    
        System.out.format("%.3g%n", 0.0000006);
        System.out.format("%.3f%n", 54.34263);
        System.out.format("%.3s%n", "ZetCode");
    }
}
