package org.alive.test.testcase;

import org.alive.test.core.TestCase;

/**
 * Test graceful shutdown.
 * 
 * @author hailin84
 * 
 */
public class ShutDownTestCase extends TestCase {

	public ShutDownTestCase(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void testShutDownHook() throws Exception {
		// 注册第一个hook
		Runtime.getRuntime().addShutdownHook(new Thread() {

			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("clean task1 completed.");
			}
		});
		// 注册第二个hook
		Runtime.getRuntime().addShutdownHook(new Thread() {

			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("clean task2 completed");
			}
		});
		// 启动子线程
		new Thread() {

			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						System.out.println("sub thread is running");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		// 程序退出
		System.out.println("调用System.exit(0);");
		System.exit(0);
	}
}
