package org.alive.test.testcase;

import org.alive.learn.event.CusEventListener;
import org.alive.learn.event.EventSourceObject;
import org.alive.test.core.TestCase;

public class EventTestCase extends TestCase {

	public EventTestCase(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void testEvent() {
		EventSourceObject object = new EventSourceObject();  
        //×¢²á¼àÌýÆ÷  
        object.addCusListener(new CusEventListener());  
        //´¥·¢ÊÂ¼þ  
        object.setName("eric");
	}
}
