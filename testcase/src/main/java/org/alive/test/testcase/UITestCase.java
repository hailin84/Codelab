package org.alive.test.testcase;

import org.alive.learn.ui.swing.*;
import org.alive.test.core.TestCase;

public class UITestCase extends TestCase {

	public UITestCase() {
		super();
	}

	public UITestCase(String name) {
		super(name);
	}
	
	public void testSwing() throws Exception {
		// new GridBagLayoutDemo();
		// new GroupBoxDemo();
		new JComboBoxJTable();
	}
}
