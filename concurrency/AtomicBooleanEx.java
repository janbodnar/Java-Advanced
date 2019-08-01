package com.zetcode;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanEx {

    public static void main(final String[] arguments) {

        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        var t1 = new Thread("Thread 1") {

            public void run() {

                while (true) {

                    System.out.printf("%s Waiting for Thread 2 to set Atomic var to true %n",
                            Thread.currentThread().getName(), atomicBoolean.get());

                    if (atomicBoolean.compareAndSet(true, false)) {

                        System.out.println("Finished");
                        break;
                    }
                }
            }
        };

        t1.start();

        var t2 = new Thread("Thread 2") {

            public void run() {

                System.out.printf("%s Atomic var: %s%n", Thread.currentThread().getName(),
                        atomicBoolean.get());

                System.out.printf("%s is setting the Atomic var to true %n",
                        Thread.currentThread().getName());

                atomicBoolean.set(true);

                System.out.printf("%s Atomic var: %s%n", Thread.currentThread().getName(),
                        atomicBoolean.get());
            }
        };

        t2.start();
    }
}
