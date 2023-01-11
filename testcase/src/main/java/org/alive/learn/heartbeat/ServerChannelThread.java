package org.alive.learn.heartbeat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 使用nio创建服务器监听端口的线程，每一个此线程将会开启一个ServerSocketChannel和selector，并执行select()操作。本类的职责：
 * <ul>
 * <li>初始化ServerSocketChannel和selector，将ServerSocketChannel注册OP_CONNECT事件到selector；</li>
 * <li>循环执行selector.select()，将建立的连接SocketChannel转发到对应的SubReactor处理；</li>
 * </ul>
 * </p>
 * <p>
 * 对应Doug
 * Lea老爷子给出的Reactor主从多线程模型里的Acceptor和MainReactor；个人觉得可以精简，没必要单独写一个Acceptor类；反正思路都是差不多的，理解就行。
 * </p>
 * <p>
 * 在实现Reactor主从多线程模型时学习了这位仁兄的代码，： <a href=
 * "http://blog.csdn.net/prestigeding/article/details/55100075">线程模型前置篇Reactor反应堆设计模式实现（基于java.nio）</a>。表示感谢。
 * </p>
 * 
 * @author hailin84
 * @since 2017.06.15
 */
public class ServerChannelThread extends Thread {
	public static final Logger logger = LoggerFactory.getLogger(ServerChannelThread.class);

	private Selector selector = null;

	private ServerSocketChannel ssc = null;

	private String listenIp = "";

	private int listenPort = 7890;

	private volatile boolean running = true;

	/** 最大连接队列，此值可以通过配置获取 */
	private int backlog = 50;

	/** 通过setter传递进来 */
	private ChannelManagerGroup managerGroup = null;

	public ServerChannelThread(String listenIp, int listenPort, int backlog) {
		super();
		this.listenIp = listenIp;
		this.listenPort = listenPort;
		if (backlog > 0) {
			this.backlog = backlog;
		}
	}

	public ServerChannelThread(String listenIp, int listenPort) {
		super();
		this.listenIp = listenIp;
		this.listenPort = listenPort;
	}

	public ServerChannelThread(int listenPort) {
		super();
		this.listenPort = listenPort;
	}

	protected void init() {
		try {
			selector = Selector.open();

			// 初始化服务端套接字通道ServerSocketChannel
			ssc = ServerSocketChannel.open();
			InetSocketAddress address = null;
			if (this.listenIp != null && this.listenIp.length() > 0) {
				address = new InetSocketAddress(this.listenIp, this.listenPort);
			} else {
				address = new InetSocketAddress(this.listenPort);
			}

			/**
			 * <pre>
			 * ssc.socket().setReuseAddress(true);
			 * ssc.socket().bind(address, backlog);
			 * </pre>
			 */
			ssc.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			ssc.bind(address, backlog);

			// 配置非阻塞方式
			ssc.configureBlocking(false);

			// 向selector注册该channel的连接事件
			ssc.register(selector, SelectionKey.OP_ACCEPT);

			this.setName("Server-" + listenPort);
		} catch (IOException e) {
			logger.error("Server线程初始化失败", e);
		}
	}

	@Override
	public void run() {
		logger.info("Server线程初始化");
		init();

		try {
			while (running) {
				// 以下三种情况，selector.select会立即返回：
				// (1) 至少一个channel被选中；
				// (2) 调用selector.wakeUp；
				// (3) select所在线程收到中断请求；
				int num = selector.select(2000);
				if (num == 0) { // someone may called wakeUp somewhere
					continue;
				}

				Set<SelectionKey> st = selector.selectedKeys();
				Iterator<SelectionKey> it = st.iterator();

				while (it.hasNext()) {
					// SelectionKey key = (SelectionKey) it.next();
					accept(it.next());
				}
				st.clear();
			}
		} catch (IOException e) {
			logger.error("server.seletor异常", e);
		}

		logger.info("结束线程 {}", getName());
	}

	/**
	 * 客户端连接
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void accept(SelectionKey key) throws IOException {
		if (key.isValid() && key.isAcceptable()) {
			SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
			logger.info("Accept connection: " + sc.getRemoteAddress());
			this.managerGroup.dispatch(sc);
		}
	}

	public ChannelManagerGroup getManagerGroup() {
		return managerGroup;
	}

	public void setManagerGroup(ChannelManagerGroup managerGroup) {
		this.managerGroup = managerGroup;
	}

	/**
	 * close: stop thread, close Selector and ServerSocketChannel
	 */
	public void close() {
		logger.info("close - Server线程");
		try {
			this.running = false;
			if (this.selector != null) {
				this.selector.wakeup(); // selector.select中返回
				this.selector.close();
			}
			if (this.ssc != null) {
				this.ssc.close();
			}

		} catch (IOException e) {
			logger.error("close异常", e);
		}
		// 同时关闭managerGroup
		if (this.managerGroup != null) {
			managerGroup.close();
		}
	}

	public boolean isRunning() {
		return running;
	}
}
