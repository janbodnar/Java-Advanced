package com.zetcode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class Task implements Runnable {

    public void run() {

        System.out.println("Doing task");
    }
}

// ExecutorService schedules commands to run after a given
// delay, or to execute periodically.

public class ScheduledExecutorServiceEx {

    public static void main(final String[] arguments)  {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        ScheduledFuture<?> taskHandler =
                scheduler.scheduleAtFixedRate(new Task(), 2, 2, TimeUnit.SECONDS);

        scheduler.schedule(() -> {
            taskHandler.cancel(true);
            scheduler.shutdown();
        }, 10, TimeUnit.SECONDS);

        System.out.println("Launching tasks");
    }
}
