package org.alive.learn.thread;

/**
 * <p>
 * 测试Java线程interrupt
 * 
 * @author hailin84
 * @date 2017.08.30
 */
public class InterruptTest {

	public static void main(String[] args) {
		int pause = 2000;
		testBasic();
		sleep(pause);
		testSleep();
		sleep(pause);
		testWait();
		sleep(pause);
		testJoin();
	}

	// 基本的中断方法调用
	public static void testBasic() {
		System.out.println("InterruptTest.testBasic()");
		Thread t = Thread.currentThread();
		String name = t.getName();
		System.out.println(name + " " + t.isInterrupted()); // false
		// 调用interrupt，设置中断状态(interrupt status)为true
		t.interrupt();
		System.out.println(name + " " + t.isInterrupted()); // true

		// Thread.interrupted()，返回当前线程的中断状态，同时清除设置的中断状态
		System.out.println(name + " " + Thread.interrupted()); // true
		System.out.println(name + " " + t.isInterrupted()); // false
	}

	// 中断sleep状态的线程
	public static void testSleep() {
		System.out.println("InterruptTest.testSleep()");
		SleepThread t = new SleepThread("SleepThread");
		t.start();
		try {
			Thread.sleep(7 * 1000);
		} catch (InterruptedException e) {
			// ignore
		}

		// 调用interrupt，SleepThread从sleep状态抛出InterruptedException，处理后结束线程
		t.interrupt();
	}

	static class SleepThread extends Thread {

		public SleepThread(String name) {
			super(name);
		}

		@Override
		public void run() {
			while (true) {
				try {
					System.out.printf("%s loop...", getName()).println();
					Thread.sleep(3 * 1000);
				} catch (InterruptedException e) {
					// 收到中断后退出循环，结束线程；某些时候需要将中断状态继续向上层暴露时，则需要再次调用interrupt进行中断。
					// Thread.currentThread().interrupt();
					System.out.printf("%s 中断，退出loop...", getName()).println();
					break;
				}
			}
			System.out.printf("%s 结束", getName()).println();
		}
	}

	// 中断wait状态的线程
	public static void testWait() {
		System.out.println("InterruptTest.testWait()");
		WaitThread t = new WaitThread("WaitThread");
		t.start();
		try {
			Thread.sleep(5 * 1000);
		} catch (InterruptedException e) {
			// ignore
		}
		t.interrupt();
		// System.out.println();
	}

	static class WaitThread extends Thread {
		private Object waitObject = new Object();

		public WaitThread(String name) {
			super(name);
		}

		@Override
		public void run() {
			synchronized (waitObject) {
				try {
					System.out.printf("%s 进入wait阻塞...", getName()).println();
					waitObject.wait();
					System.out.printf("%s 结束wait阻塞...", getName()).println();
				} catch (InterruptedException e) {
					System.out.printf("%s Interrupted...", getName()).println();
				}
				System.out.printf("%s Business after wait...", getName()).println();
			}
			System.out.printf("%s 结束", getName()).println();
		}
	}

	public static void testJoin() {
		System.out.println("InterruptTest.testJoin()");
		JoinThread jt = new JoinThread("JoinThread", Thread.currentThread());
		jt.start();
		
		try {
			jt.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.printf("%s 被中断，从join中返回...", Thread.currentThread().getName()).println();
		}
	}

	static class JoinThread extends Thread {
		// 调用jointhread.join的线程，需要在这里对其进行中断
		private Thread callJoinThread;

		public JoinThread(String name, Thread callJoinThread) {
			super(name);
			this.callJoinThread = callJoinThread;
		}

		@Override
		public void run() {
			int max = 1000000;
			for (int i = 0; i < max; i++) {
				//
			}
			System.out.printf("%s step1...", getName()).println();
			callJoinThread.interrupt(); // 中断阻塞在join的线程
			for (int i = 0; i < max; i++) {
				//
			}
			System.out.printf("%s step2...", getName()).println();
			System.out.printf("%s 结束", getName()).println();
		}
	}
	
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
