package org.alive.test.core;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class TestCase {
	private String name;
	
	public TestCase() {
	}
	
	public TestCase(String name) {
		this.name = name;
	}

	public void setUp() {
	}

	public void tearDown() {
	}

	public void execute() throws Exception {
		this.setUp();
		System.out.printf("Execute testcase %0$s start...", new Object[] { name }).println();
		Method[] mds = this.getClass().getDeclaredMethods();
		for (Method method : mds) {
			if (method.getName().startsWith("test") && Modifier.isPublic(method.getModifiers())) {
				System.out.printf(":%0$s ====>", new Object[] { method.getName() }).println();
				method.invoke(this, (Object[]) null);
			}
		}
		System.out.printf("Execute testcase %0$s completed.", new Object[] { name }).println();
		this.tearDown();
	}
}
