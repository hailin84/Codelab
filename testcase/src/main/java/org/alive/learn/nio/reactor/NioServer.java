package org.alive.learn.nio.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * Java NIO 主从多线程模型(Reacor)
 * 
 * see http://blog.csdn.net/prestigeding/article/details/55100075
 *
 */
public class NioServer {

	private static final int DEFAULT_PORT = 7890;

	public static void main(String[] args) {

		new Acceptor().start();

	}

	/**
	 * Acceptor类可以理解为建立端口监听
	 */
	private static class Acceptor {

		// main Reactor 线程池，用于处理客户端的连接请求
		// private static ExecutorService mainReactor = Executors.newSingleThreadExecutor();

		public void start() {
			// TODO Auto-generated method stub
			ServerSocketChannel ssc = null;

			try {
				ssc = ServerSocketChannel.open();
				ssc.configureBlocking(false);
				ssc.bind(new InetSocketAddress(DEFAULT_PORT));

				// 转发到 MainReactor反应堆
				dispatch(ssc);

				System.out.println("服务端成功启动。。。。。。");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void dispatch(ServerSocketChannel ssc) {
			// 启动MainReactor线程，监听连接就绪事件
			new Thread(new MainReactor(ssc), "MainReactor").start();
			// mainReactor.submit(new MainReactor(ssc));
		}
	}

}
