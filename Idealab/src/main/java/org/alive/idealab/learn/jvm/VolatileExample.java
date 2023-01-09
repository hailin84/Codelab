package org.alive.idealab.learn.jvm;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class VolatileExample {
    private static volatile int value = 0;

    private static int THREAD_NUM = 20;

    private static CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
    public static void incr() {
        value++;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[THREAD_NUM];
        for (Thread t : threads) {
            t = new Thread(() -> {
                for (int i = 0; i < 10000; i++) {
                    incr();
                }
                countDownLatch.countDown();
            });
            t.start();
        }
        while (!countDownLatch.await(2, TimeUnit.SECONDS)) {
            //
        }

        System.out.println(value);
    }
}
