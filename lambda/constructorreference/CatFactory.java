package com.zetcode;

@FunctionalInterface
public interface CatFactory {

    Cat getCat(String name, Integer age);
}
