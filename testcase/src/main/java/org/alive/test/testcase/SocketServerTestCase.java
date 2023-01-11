package org.alive.test.testcase;

import org.alive.learn.nio.NIOSocketManagerThread;
import org.alive.learn.nio.NIOSocketServerThread;
import org.alive.test.core.TestCase;

/**
 * 测试NIOSocketServerThread
 * 
 * @author hailin84
 *
 */
public class SocketServerTestCase extends TestCase {

	public SocketServerTestCase(String name) {
		super(name);
	}

	/**
	 * 执行此TestCase，命令行telnet localhot 9999连接SocketServer
	 * @throws Exception
	 */
	public void testServer() throws Exception {
		NIOSocketManagerThread manager = new NIOSocketManagerThread();
		manager.start();
		
		NIOSocketServerThread server = new NIOSocketServerThread(8999);
		server.regAcceptMethod(manager, "addSocketChannel");
		server.start();
		
		Thread.sleep(60 * 1000);
		
		manager.close();
		server.close();
	}
}
