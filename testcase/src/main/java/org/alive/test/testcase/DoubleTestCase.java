package org.alive.test.testcase;

import org.alive.test.core.TestCase;

/**
 * Test double calculate
 * 
 * @author hailin84
 * 
 */
public class DoubleTestCase extends TestCase {

	public DoubleTestCase(String name) {
		super(name);
	}

	public void testCalculate() {
		double d = 1e16d;

		double d2 = d + 1.0d - d;

		// 以下代码输出0.0，而不是1.0
		System.out.println(d2);
	}
}
