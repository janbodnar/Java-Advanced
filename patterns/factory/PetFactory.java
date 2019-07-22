package com.zetcode;

public class PetFactory {

    public static Pet create(Class clazz, String name, String type) {

        if (clazz.equals(Cat.class)) {

            return new Cat(name, type);
        }

        if (clazz.equals(Dog.class)) {

            return new Dog(name, type);
        }

        if (clazz.equals(Parrot.class)) {

            return new Parrot(name, type);
        }

        throw new IllegalArgumentException("Invalid Pet");
    }
}
