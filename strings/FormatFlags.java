package com.zetcode;

public class FormatFlags {

    public static void main(String[] args) {
        
        System.out.format("%+d%n", 553);
        System.out.format("%010d%n", 553);
        System.out.format("%10d%n", 553);
        System.out.format("%-10d%n", 553);
        System.out.format("%d%n", -553);
        System.out.format("%(d%n", -553); 
    }
}
