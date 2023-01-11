package org.alive.test.testcase;

import org.alive.test.core.TestCase;

public class ThreadLocalTestCase extends TestCase {
	
	
	public ThreadLocalTestCase() {
		super();
	}

	public ThreadLocalTestCase(String name) {
		super(name);
	}

	/**
	 * 多个线程访问同一实例，对实例属性进行修改和读取，存在覆盖现象
	 * @throws Exception
	 */
	public void TestThreadLocal() throws Exception {
		Action action = new SomeSingletonAction();
		new RequestThread(action, "Jordan").start();
		new RequestThread(action, "Kobe").start();
	}
	
	/**
	 * 多个线程访问同一实例，使用threadlocal，不会覆盖。
	 * @throws Exception
	 */
	public void TestThreadLocalTwo() throws Exception {
		Action action = new SomeSingletonActionTwo();
		new RequestThread(action, "Jordan").start();
		new RequestThread(action, "Kobe").start();
	}
	
	public void testThreadLocalPass() throws Exception {
		new First();
		new Second();
	}
	
}

class RequestThread extends Thread {
	
	private Action action;
	private String name;
	
	public RequestThread(Action action, String name) {
		super();
		this.action = action;
		this.name = name;
	}

	public void run() {
		try {
			action.execute(name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

abstract class  Action {
	public abstract void execute(String args) throws Exception;
}

class SomeSingletonAction extends Action {
	
	private String name;
	
	public void execute(String args) throws Exception {
		String tName = Thread.currentThread().getName();
		name = args;
		System.out.println(tName + " set = " + args);
		Thread.sleep(5 * 1000);
		
		System.out.println(tName + " get = " + name);
	}
}

class SomeSingletonActionTwo extends Action {
	
	// private String name;
	/** ThreadLocal一般定义为Static，也可以不定义为Static */
	static ThreadLocal<String> threadLocal = new ThreadLocal<String>();
	
	public void execute(String args) throws Exception {
		String tName = Thread.currentThread().getName();
		threadLocal.set(args);
		System.out.println(tName + " set = " + args);
		Thread.sleep(5 * 1000);
		
		String name = threadLocal.get();
		System.out.println(tName + " get = " + name);
	}
}

class First {
	static ThreadLocal<String> store = new ThreadLocal<String>();
	
	public First() {
		store.set("My name is First.");
		System.out.println("set: My name is First.");
	}
}

class Second {
static ThreadLocal<String> store = new ThreadLocal<String>();
	
	public Second() {
		// store.set("My name is First.");
		String name = store.get();
		System.out.println("get: " + name);
	}
}