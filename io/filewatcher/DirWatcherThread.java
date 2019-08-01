package com.zetcode;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class DirWatcherThread implements Runnable {

    private final Path dir;
    private final WatchService watcher;
    private final WatchKey key;

    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    public DirWatcherThread(Path dir) throws IOException {
        this.dir = dir;
        this.watcher = FileSystems.getDefault().newWatchService();
        this.key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
    }

    public void run() {

        while (true) {

            // wait for key to be signalled
            WatchKey key;

            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                return;
            }

            if (this.key != key) {
                System.err.println("WatchKey not recognized!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent<Path> ev = cast(event);
                System.out.format("%s: %s\n", ev.kind(), dir.resolve(ev.context()));
            }

            // reset key
            key.reset();
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {

        Path dir = Paths.get("C:/Users/Jano/tmp/");

        var watcher = new DirWatcherThread(dir);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(watcher);

        // Shutdown after 20 seconds
        executor.awaitTermination(20, TimeUnit.SECONDS);
        // abort watcher
        future.cancel(true);

        executor.awaitTermination(1, TimeUnit.SECONDS);
        executor.shutdownNow();
    }
}
