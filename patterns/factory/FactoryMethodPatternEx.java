package com.zetcode;

public class FactoryMethodPatternEx {

    public static void main(String[] args) {

        var cat = PetFactory.create(Cat.class, "Micky", "Persian cat");
        System.out.println(cat);

        var dog = PetFactory.create(Dog.class, "Max", "Dalmatian");
        System.out.println(dog);

        var parrot = PetFactory.create(Parrot.class, "Frisky", "Gray parrot");
        System.out.println(parrot);
    }
}
