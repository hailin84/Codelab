package org.alive.idealab.learn.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalDemo {

    private static final AtomicInteger nextId = new AtomicInteger(0);
    private static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(nextId::getAndIncrement);
    public static void main(String[] args) {
        Thread[] threads = new Thread[10];
        for (Thread t : threads) {
            t = new Thread(new Runnable() {
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " -- " + threadLocal.get());
                }
            });
            t.start();
        }

        System.out.println("Done");
    }
}
