package org.alive.learn.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * NioUdpServer，使用NIO的UDP Client代码也跟这个类似，不提供实现了。使用UdpEchoClient进行测试即可，或者使用nc命令。
 * 
 * <p>
 * nc -u localhost 9999
 * 
 * @author hailin84
 * @date 2017.10.10
 *
 */
public class NioUdpServer extends Thread {

	private static Logger logger = LoggerFactory.getLogger(NioUdpServer.class);

	private int port = 9999;

	private Selector selector = null;

	private volatile boolean running = true;

	// private Charset charset = Charset.forName("UTF-8");
	
	private Charset charset = Charset.defaultCharset();

	private void init() {
		try {
			DatagramChannel channel = DatagramChannel.open();
			channel.configureBlocking(false);
			channel.bind(new InetSocketAddress(port));
			channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			selector = Selector.open();
			channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			logger.info("UDP Server started on port {}", port);
		} catch (Exception e) {
			logger.error("初始化异常", e);
		}
	}

	public NioUdpServer() {
	}

	@Override
	public void run() {
		init();

		// 因为注册了一个DatagramChannel，所以select永远会立刻返回1，加个sleep降低CPU占用率
		try {
			while (running) {
				int num = selector.select(2000);
				if (num == 0) {
					continue;
				}

				Set<SelectionKey> st = selector.selectedKeys();
				Iterator<SelectionKey> it = st.iterator();

				while (it.hasNext()) {
					// SelectionKey key = (SelectionKey) it.next();
					deal(it.next());
				}
				st.clear();
				TimeUnit.MILLISECONDS.sleep(1);
			}
		} catch (IOException e) {
			logger.error("IO异常退出", e);
		} catch (InterruptedException e) {
			logger.error("线程中断退出", e);
		}
	}

	private void deal(SelectionKey key) throws IOException {
		if (!key.isValid()) {
			return;
		}
		DatagramChannel ch = (DatagramChannel) key.channel();
		if (key.isReadable()) {
			// 读完消息处理完毕后，将响应消息通过Attachment注册到selector，select循环中检测到attachment为响应，则发送响应
			ByteBuffer byteBuffer = ByteBuffer.allocate(65536);
			SocketAddress sourceAddr = ch.receive(byteBuffer);
			byteBuffer.flip();
			CharBuffer charBuffer = charset.decode(byteBuffer);
			logger.info("[server receive] remote: {} : {}", sourceAddr, charBuffer.toString());
			byteBuffer.clear();

			String echo = "This is the reply message from 服务器。";
			ByteBuffer buffer = charset.encode(echo);
			// ch.register(selector, SelectionKey.OP_READ |
			// SelectionKey.OP_WRITE, new UdpTask(buffer, sourceAddr));
			logger.info("[server send] remote: {} : {}", sourceAddr, echo);
			ch.send(buffer, sourceAddr);
		}
		/**
		 * 通过attachment的方式发送数据，客户端收不到，不知道是什么原因
		 * <code><pre>
		if (key.isWritable()) {
			Object obj = key.attachment();
			if (obj != null && obj instanceof UdpTask) {
				UdpTask task = (UdpTask) obj;
				if (task.getBuffer() == null || task.getSource() == null) {
					key.attach(null);
					return;
				}
				logger.info("[server send] remote: {} : {}", task.getSource(), charset.decode(task.getBuffer()).toString());
				ch.send(task.getBuffer(), task.getSource());
				key.attach(null);
			}
		}
		</pre></code>
		 */
	}

	public void close() {
		this.running = false;
		this.interrupt(); // 发送中断，以从sleep中唤醒线程
		if (selector != null) {
			try {
				// selector.wakeup();
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		NioUdpServer server = new NioUdpServer();
		server.start();

		System.in.read();
		server.close();
	}

}
