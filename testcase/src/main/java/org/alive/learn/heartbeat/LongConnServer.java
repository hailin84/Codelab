package org.alive.learn.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 使用NIO实现的长连接的服务端监听线程，基于主从多主从多线程模型(Reacor)；对应的Reactor模式中的MainReactor和Acceptor，同时充当了两者的责任。
 * </p>
 * <p>
 * Reactor模型里面，Reactor线程主要是用来执行selector.select()方法的，通常情况下，监听连接需要一个selector(即将ServerSocketChannel注册到此seletor)，
 * SocketChannel建立的时候，检测连接的读、写是否就绪，需要另外的线程和selector，也即是SubReactor线程。SubReactor根据需要可以用一个或者多个，
 * 连接多的时候一般都需要多个SubReactor。
 * <ul>
 * <li>Reactor线程：初始化ServerSocketChannel和selector，将ServerSocketChannel的OP_ACCEPT事件注册到selector，循环检查selector.select()；
 * 将连接SocketChannel通过策略添加到多个SubReactor中的一个；</li>
 * <li>SubReactor：初始化Selector，管理一个SocketChannel连接集合，通过selector.select()检测OP_READ事件；将读操作提交工作线程池；</li>
 * <li>工作线程：执行具体的读、业务处理、写等的操作，通过线程池执行；</li>
 * </ul>
 * 
 * </p>
 * 
 * @author hailin84
 * 
 */
public class LongConnServer {

	public static final Logger logger = LoggerFactory.getLogger(LongConnServer.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ServerChannelThread server = new ServerChannelThread(7890);
		ChannelManagerGroup group = new ChannelManagerGroup(4, 5, 7890);
		server.setManagerGroup(group);
		server.start();
		waitToQuit(server);
	}

	private static void waitToQuit(ServerChannelThread server) throws Exception {
		logger.info("System is running, press q to quit.");
		while (server.isRunning()) {
			int b = System.in.read();
			switch (b) {
			case 'q':
				// logger.info("服务器线程{}关闭...", server.getName());
				server.close();
				break;

			case '\r':
			case '\n':
				break;
			default:
				logger.info("q -- quit.");
				break;
			}
		}
	}
}
