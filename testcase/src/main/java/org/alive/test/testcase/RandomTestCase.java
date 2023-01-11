package org.alive.test.testcase;

import org.alive.test.core.TestCase;
import org.alive.util.RandomUtil;

public class RandomTestCase extends TestCase {

	public RandomTestCase(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void testRandomString() {
		for (int i = 0; i < 5; i++) {
			System.out.println(RandomUtil.randomString(15));
		}
	}

}
