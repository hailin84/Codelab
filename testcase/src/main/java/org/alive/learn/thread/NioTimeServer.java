package org.alive.learn.thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>
 * 简单TimeServer，测试方法：telnet localhost 7890
 * 
 * @author hailin84
 * @date 2017.08.30
 *
 */
public class NioTimeServer extends Thread {

	private Selector selector = null;

	private ServerSocketChannel ssc = null;

	private int port = 7890;

	/** running标志 */
	// private volatile boolean running = true;

	/** 日期格式 */
	private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public NioTimeServer() {
	}

	public NioTimeServer(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	protected void init() {
		try {
			selector = Selector.open();

			// 初始化服务端套接字通道ServerSocketChannel
			ssc = ServerSocketChannel.open();
			InetSocketAddress address = new InetSocketAddress(this.port);

			ssc.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			ssc.bind(address);

			// 配置非阻塞方式
			ssc.configureBlocking(false);

			// 向selector注册该channel的连接事件
			ssc.register(selector, SelectionKey.OP_ACCEPT);

			this.setName("Server-" + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		init();

		try {
			// 如果while循环使用running，那么close方法里可以增加selector.wakeUp方法调用，使selector.select方法立即返回
			// 这样子，就完全不需要使用中断
			// while (running) {

			// while循环使用running和线程的中断状态判读是否应该结束
			// while (running && !Thread.currentThread().isInterrupted()) {

			// while循环条件只使用中断状态
			while (!isInterrupted()) {
				// 以下三种情况，selector.select会立即返回：
				// (1) 至少一个channel被选中；
				// (2) 调用selector.wakeUp；
				// (3) select所在线程收到中断请求；
				int num = selector.select(2000);
				if (num == 0) {
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
			e.printStackTrace();
		}

		System.out.println("Server线程退出");
	}

	private void accept(SelectionKey key) throws IOException {
		if (key.isAcceptable()) {
			SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
			sc.configureBlocking(false);
			// sc.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			sc.setOption(StandardSocketOptions.TCP_NODELAY, true);
			sc.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			sc.register(selector, SelectionKey.OP_WRITE); // 只注册写事件，不注册读事件
			System.out.println("new connection : " + sc.socket().getRemoteSocketAddress());
		}

		if (key.isValid() && key.isWritable()) {
			SocketChannel sc = (SocketChannel) key.channel();
			String msg = "Now: " + LocalDateTime.now().format(fmt);
			System.out.println("Send msg: " + msg);
			sc.write(ByteBuffer.wrap(msg.getBytes()));
			sc.close();
		}
	}

	public static void main(String[] args) throws IOException {
		// 启动Server线程
		NioTimeServer server = new NioTimeServer();
		server.setName("Server-" + server.getPort());
		server.start();

		// main线程继续接受键盘输入，按q退出程序
		waitToQuit(server);
	}

	public void close() {
		// while条件只使用running标志
		// this.running = false;
		// this.selector.wakeup();
		
		// while条件使用running和中断
		// this.running = false;
		// this.interrupt();
		
		// while条件使用中断
		this.interrupt(); // 这里调用interrupt发送中断

		try {
			this.selector.close();
			this.ssc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void waitToQuit(NioTimeServer server) throws IOException {
		System.out.printf("Server is running on port %s, press q to quit.\n", server.getPort());
		boolean input = true;
		while (input) {
			int b = System.in.read();
			switch (b) {
			case 'q':
				System.out.println("NioTimeServer关闭...");
				server.close();
				input = false;
				break;
			case '\r':
			case '\n':
				break;
			default:
				System.out.println("q -- quit.");
				break;
			}
		}
	}
}
