package org.alive.test.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Test suite, include one or more testcases.
 * 
 * @author hailin84
 *
 */
public class TestSuite {

	private List<TestCase> cases = new ArrayList<TestCase>();
	
	public void addTestCase(TestCase t) {
		this.cases.add(t);
	}
	
	public void run() {
		for (TestCase t : cases) {
			try {
				t.execute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
