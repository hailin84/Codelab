package org.alive.test.testcase;

import org.alive.learn.callback.MsgLocal;
import org.alive.learn.callback.MsgRemote;
import org.alive.test.core.TestCase;

public class CallBackTestCase extends TestCase {

	public CallBackTestCase(String name) {
		super(name);
	}

	public void testCallback() {
		MsgLocal local = new MsgLocal(new MsgRemote(),"小李，问你下1+1等于几？");    
        
        local.sendMessage(); 
	}
}
