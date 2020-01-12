package com.zetcode;

public class ConcatStrings {

    public static void main(String[] args) {

        int age = 34;
        String name = "Peter";

        System.out.println(name + " is " + age + " years old");
        System.out.printf("%s is %d years old%n", name, age);
        System.out.println(String.format("%s is %d years old", name, age));

        var sb = new StringBuilder(name);
        sb.append(" is ").append(age).append(" years old");
        System.out.println(sb);

        System.out.println(name.concat(" is ").concat(String.valueOf(age)).concat(" years old"));
    }
}
