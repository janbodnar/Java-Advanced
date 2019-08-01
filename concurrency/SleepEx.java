package com.zetcode;

class Task implements Runnable {

    private int delay;
    private String name;

    public Task(String name, int delay) {

        this.name = name;
        this.delay = delay;
    }

    @Override
    public void run() {

        try {

            Thread.sleep(delay);
            System.out.printf("finishing task %s%n", this.name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class SleepEx {

    public static void main(String[] args) {

        var task1 = new Task("Task 2000", 2000);
        var t1 = new Thread(task1);
        t1.start();

        var task2 = new Task("Task 1000", 1000);
        var t2 = new Thread(task2);
        t2.start();

        var task3 = new Task("Task 500", 500);
        var t3 = new Thread(task3);
        t3.start();

        System.out.println("tasks launched");
    }
}
