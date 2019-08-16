package com.zetcode;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

class Task {

    private final int duration;

    public Task(int duration) {
        this.duration = duration;
    }

    public int doTask() {
        System.out.printf("%s %n", Thread.currentThread().getName());

        try {
            Thread.sleep(duration * 1000);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }

        return duration;
    }
}

public class RunTasksParrallel {

    public static void main(String[] args) {

        long start = System.nanoTime();
        
        List<Task> tasks = IntStream.range(0, 10)
                .mapToObj(i -> new Task(1))
                .collect(toList());

        List<Integer> result = tasks.parallelStream()
                .map(Task::doTask)
                .collect(toList());

        long end = System.nanoTime();

        long duration = (end - start) / 1_000_000;

        System.out.printf("Run %d tasks in %d millis\n", tasks.size(), duration);
        System.out.println(result);
    }
}
