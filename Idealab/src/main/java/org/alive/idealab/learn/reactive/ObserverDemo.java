package org.alive.idealab.learn.reactive;

import java.util.Observable;

/**
 * @Description: 响应式编程模式
 * @author: xuhailin
 * @date: 2023/3/10 10:08
 */
public class ObserverDemo extends Observable {
    public static void main(String[] args) {
        ObserverDemo demo = new ObserverDemo();
        demo.addObserver((o, arg) -> {
            System.out.println(Thread.currentThread().getName() + " - A");
        });
        demo.addObserver((o, arg) -> {
            System.out.println(Thread.currentThread().getName() + " - B");
        });

        demo.setChanged();
        demo.notifyObservers();

    }
}
