package org.alive.idealab.learn.thread;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 示范线程使用
 */
public class ThreadDemo {

    public static void main(String[] args) {
        newCallBackThread();
        threadUncaughtException();
    }


    public static void newCallBackThread() {
        // Callable接口可以返回执行结果
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName());
            return UUID.randomUUID().toString();
        });
        new Thread(futureTask).start();
        try {
            System.out.println(futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void threadUncaughtException() {
        Thread t = new Thread(() -> {
            Integer.parseInt("ABC");
        });
        t.setUncaughtExceptionHandler((t1, e) -> {
            // 这个方法也是在抛出异常的线程中执行
            System.out.println(t1 == Thread.currentThread());
            System.out.println(Thread.currentThread().getName());
            System.out.println(t1.getName() + " occurs exception ");
            e.printStackTrace();
        });

        t.start();
    }
}
