package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilesListCount {

    public static void main(String[] args) throws IOException {

        var homeDir = System.getProperty("user.home")
                + System.getProperty("file.separator") + "Downloads";

        var nOfPdfFiles = Files.list(Paths.get(homeDir)).filter(path -> path.toString()
                .endsWith(".pdf")).count();

        System.out.printf("There are %d PDF files", nOfPdfFiles);
    }
}
