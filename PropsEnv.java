package com.zetcode;

public class PropsEnv {

    public static void main(String[] args) {

        var path = System.getenv("PATH");

        String[] directories = path.split(";");

        for (var directory : directories) {

            System.out.println(directory);
        }

        System.out.printf("There are %d items in the PATH variable%n", directories.length);

        String os_version = System.getProperty("os.name");
        String java_version = System.getProperty("java.version");
        String java_home = System.getProperty("java.home");

        System.out.println(os_version);
        System.out.println(java_version);
        System.out.println(java_home);
    }
}
