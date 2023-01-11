package org.alive.learn.thread;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WaitNotify {
	static boolean flag = true;

	static Object lock = new Object();

	static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public static void main(String[] args) throws Exception {
		new Thread(new Wait(), "Wait-Thread").start();
		SleepUtils.second(1);
		new Thread(new Notify(), "Notify-Thread").start();
	}
	
	static class Wait implements Runnable {

		@Override
		public void run() {
			synchronized (lock) {
				while (flag) {
					System.out.println(Thread.currentThread() + " - flag is true, wait. " + LocalDateTime.now().format(fmt));
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				System.out.println(Thread.currentThread() + " - flag is false, running. " + LocalDateTime.now().format(fmt));
			}
		}
		
	}
	
	static class Notify implements Runnable {
		@Override
		public void run() {
			synchronized (lock) {
				System.out.println(Thread.currentThread() + " - hold lock, notify. " + LocalDateTime.now().format(fmt));
				lock.notifyAll();
				flag = false;
				SleepUtils.second(5);
			}
			
			synchronized (lock) {
				System.out.println(Thread.currentThread() + " - hold lock again, sleep. " + LocalDateTime.now().format(fmt));
				SleepUtils.second(5);
			}
		}
	}

}
