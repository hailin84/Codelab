package org.alive.test.testcase;

import org.alive.test.core.TestCase;

/**
 * Show how to use wait/notify in multithreads.
 * 
 * @author hailin84
 * 
 */
public class WaitNotifyTestCase extends TestCase {

	public WaitNotifyTestCase(String name) {
		super(name);
	}

	public void testDemo() throws Exception {
		TestThread testThread1 = new TestThread();
		TestThread testThread2 = new TestThread();
		TestThread testThread3 = new TestThread();

		testThread1.start();
		testThread2.start();
		testThread3.start();

		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException e) {
			System.out.println("Main Thread Interrupted");
		}

		System.out.println("Resume By Notify");

		testThread1.resumeByNotify();

		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException e) {
			System.out.println("Main Thread Interrupted");
		}

		System.out.println("Resume By NotifyAll");

		testThread1.resumeByNotifyAll();
	}

}

class TestThread extends Thread {

	private static Object obj = new Object();

	@Override
	public void run() {
		System.out.println(getName() + " Before Wait");

		synchronized (obj) {
			try {
				obj.wait();
			} catch (InterruptedException e) {
				System.out.println(getName() + " Test Thread Interrupted");
			}
		}

		System.out.println(getName() + " After Wait");
	}

	public void resumeByNotify() {
		synchronized (obj) {
			obj.notify();
		}
	}

	public void resumeByNotifyAll() {
		synchronized (obj) {
			obj.notifyAll();
		}
	}

}