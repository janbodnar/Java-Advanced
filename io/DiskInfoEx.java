package com.zetcode;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

public class DiskInfoEx {

    public static void main(String[] args) {

        FileSystem fileSystem = FileSystems.getDefault();

        System.out.printf("%30s | %10s | %23s | %20s \n", "", "Type", "Total space", "Free space");
        System.out.println("-------------------------------------------------"
                + "----------------------------------------------------------");

        Iterable<FileStore> it = fileSystem.getFileStores();

        it.forEach(fileStore -> {
            try {
                System.out.printf("%30s | %10s | %20s GB | %20s GB\n", fileStore, fileStore.type(),
                        (fileStore.getTotalSpace() / 1073741824f),
                        (fileStore.getUsableSpace() / 1073741824f));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

