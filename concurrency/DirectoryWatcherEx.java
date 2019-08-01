package com.zetcode;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

class DirectoryWatcher implements Runnable {

    private Path path;

    public DirectoryWatcher(Path path) {

        this.path = path;
    }

    // print the events and the affected file
    private void printEvent(WatchEvent<?> event) {

        Kind<?> kind = event.kind();

        if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {

            Path pathCreated = (Path) event.context();
            System.out.printf("Entry created: %s%n", pathCreated);
        } else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {

            Path pathDeleted = (Path) event.context();
            System.out.printf("Entry deleted: %s%n", pathDeleted);
        } else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {

            Path pathModified = (Path) event.context();
            System.out.printf("Entry modified: %s%n", pathModified);
        }
    }

    @Override
    public void run() {

        try (WatchService watchService = path.getFileSystem().newWatchService()) {

            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

            // loop forever to watch directory
            while (true) {

                WatchKey watchKey;

                try {
                    watchKey = watchService.take(); // this call is blocking until events are present
                } catch (InterruptedException e) {
                    return;
                }

                // poll for file system events on the WatchKey
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    printEvent(event);
                }

                // if the watched directed gets deleted, get out of run method
                watchKey.reset();
            }

        } catch (IOException e) {

            e.printStackTrace(); // log framework in production
        }
    }
}

public class DirectoryWatcherEx {

    public static void main(String[] args) throws InterruptedException {

        Path pathToWatch = FileSystems.getDefault().getPath("C:/Users/Jano/tmp/");
        
        var dirWatcher = new DirectoryWatcher(pathToWatch);
        var dirWatcherThread = new Thread(dirWatcher);
        dirWatcherThread.start();

        // interrupt the program after 10 seconds to stop it.
        Thread.sleep(10000);
        dirWatcherThread.interrupt();
    }
}
