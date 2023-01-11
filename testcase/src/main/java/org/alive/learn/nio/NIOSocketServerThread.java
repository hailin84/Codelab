package org.alive.learn.nio;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 基于Java NIO的Socket端口监听服务线程，本线程在指定端口开启监听服务，然后通过Selector循环检测Socket连接事件
 * 
 * @author hailin84
 * 
 */
public class NIOSocketServerThread extends Thread {

	private Selector selector = null;

	private ServerSocketChannel ssc = null;

	private String listenIp = "";

	private int listenPort = 0;

	private boolean running = true;

	private boolean initialized = false;

	/** 最大连接队列，此值可以通过配置获取 */
	private int backlog = 50;

	/** 接收到新Socket的回调对象 */
	private Object acceptObject = null;

	/** 接收到新Socket的回调方法 */
	private Method acceptMethod = null;

	public NIOSocketServerThread(int listenPort) {
		setListenAddress("", listenPort);
	}

	public NIOSocketServerThread(String listenIp, int listenPort) {
		setListenAddress(listenIp, listenPort);
	}

	public void setListenAddress(String listenIp, int listenPort) {
		this.listenIp = listenIp;
		this.listenPort = listenPort;
	}

	public int getBacklog() {
		return backlog;
	}

	/**
	 * 设置backlog
	 * 
	 * @param backlog
	 */
	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public synchronized void init() throws Exception {
		try {
			if (initialized) {
				return;
			}

			selector = Selector.open();

			// 初始化服务端套接字通道ServerSocketChannel
			ssc = ServerSocketChannel.open();
			InetSocketAddress address = null;
			if (this.listenIp.length() > 0) {
				address = new InetSocketAddress(this.listenIp, this.listenPort);
			} else {
				address = new InetSocketAddress(this.listenPort);
			}

			ssc.socket().setReuseAddress(true);
			ssc.socket().bind(address, backlog);

			// 配置非阻塞方式
			ssc.configureBlocking(false);

			// 向selector注册该channel的连接事件
			ssc.register(selector, SelectionKey.OP_ACCEPT);

			initialized = true;

		} catch (Exception e) {
			// TODO: 记录错误日志
			throw e;
		}
	}

	public void run() {
		try {
			// 初始化
			init();

			while (running) {
				int num = selector.select(2000);
				if (num == 0) { // someone may called wakeUp somewhere
					continue;
				}

				Iterator<SelectionKey> it = selector.selectedKeys().iterator();

				while (it.hasNext()) {
					SelectionKey key = (SelectionKey) it.next();
					try {
						doWork(key);
					} catch (IOException e) {
						e.printStackTrace();
						// TODO: 处理单个连接的异常
					}
					it.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * close: stop thread, close Selector and ServerSocketChannel
	 */
	public void close() {
		try {
			this.running = false;
			if (this.selector != null) {
				this.selector.close();
			}
			if (this.ssc != null) {
				this.ssc.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: close exception
		}
	}

	public void regAcceptMethod(Object obj, String methodName)
			throws NoSuchMethodException, SecurityException {
		this.acceptObject = obj;
		this.acceptMethod = obj.getClass().getMethod(methodName,
				new Class[] { SocketChannel.class });
	}

	private void doWork(SelectionKey key) throws IOException {
		if (key.isValid() && key.isAcceptable() && this.acceptObject != null
				&& this.acceptMethod != null) {
			SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
			System.out.println("new connection : "
					+ sc.socket().getRemoteSocketAddress());

			try {
				this.acceptMethod.invoke(this.acceptObject, new Object[] { sc });
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
