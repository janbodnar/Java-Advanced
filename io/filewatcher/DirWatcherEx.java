package com.zetcode;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class DirWatcherEx {

    public static void main(String[] args) throws IOException {

        var watchDir = "C:/Users/Jano/tmp/";

        Path filePath = Paths.get(watchDir);

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {

            // listen for create, delete and modify event kinds
            filePath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {

                WatchKey key;

                try {
                    // the calling thread blocks until a key is signalled
                    key = watchService.take();
                } catch (InterruptedException x) {
                    return;
                }

                // retrieve all the accumulated events
                for (WatchEvent<?> event : key.pollEvents()) {

                    WatchEvent.Kind<?> kind = event.kind();

                    System.out.printf("Event: %s ", kind.name());
                    Path path = (Path) event.context();
                    System.out.printf("Path: %s %n", path.toString());
                }

                // resetting the key goes back to ready state
                key.reset();
            }
        }
    }
}
