package org.alive.test.testcase;

import org.alive.test.core.TestCase;

public class MultiThreadTestCase extends TestCase {

	public MultiThreadTestCase(String name) {
		super(name);
	}

	/**
	 * 线程会等待，说明synchronized方法使用的是锁是this.
	 * 
	 * @throws Exception
	 */
	public void mtestSynObject() throws Exception {
		final MultiThreadSync thisObj = new MultiThreadSync();
		new Thread() {
			@Override
			public void run() {
				try {
					thisObj.m1();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();

		new Thread() {
			@Override
			public void run() {
				try {
					thisObj.m2();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void testThreadCost() {
		new Thread("loop-no-sleep-1") {
			@Override
			public void run() {
				while(true) {
					// 没有sleep会导致CPU使用率高
				}
			}
		}.start();
		new Thread("loop-sleep") {
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
}

class MultiThreadSync {
	public synchronized void m1() throws InterruptedException {
		System.out.println("m1 call");
		Thread.sleep(2000);
		System.out.println("m1 call done");
	}

	public void m2() throws InterruptedException {
		synchronized (this) {
			System.out.println("m2 call");
			Thread.sleep(2000);
			System.out.println("m2 call done");
		}
	}
}

