package org.alive.learn.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {
	public static String message = "Hello, Bozo.";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// serverOne();

		// serverTwo();

		serverThree();
	}

	/**
	 * 演示如何使用ServerSocketChannel非阻塞的accept 测试方法：
	 * 
	 * 调用serverOne(); 本机通过命令行运行telnet连接测试： telnet localhost 8989
	 * 
	 * @throws Exception
	 */
	public static void serverOne() throws Exception {

		int port = 8989;
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ServerSocket ss = ssc.socket();
		ss.bind(new InetSocketAddress(port));
		// set no blocking
		ssc.configureBlocking(false);

		ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
		boolean print = true;
		while (true) {
			if (print) {
				System.out.println("[Server#1] wait for connection ……");
			}
			SocketChannel sc = ssc.accept(); // 非阻塞模式下不会阻塞

			if (sc == null) {
				print = false;
				// no connections, snooze a while ...
				Thread.sleep(1000);
			} else {
				print = true;
				System.out.println("Incoming connection from "
						+ sc.socket().getRemoteSocketAddress());
				buffer.rewind();
				// write msg to client
				sc.write(buffer);
				Thread.sleep(2000); // 2秒后关闭连接
				sc.close();
			}
		}
	}

	public static void serverTwo() throws Exception {
		ServerSocket ss = new ServerSocket(8989);
		while (true) {
			System.out.println("[Server#2] wait for connection ……");
			Socket s = ss.accept(); // 阻塞直到连接
			System.out.println("Incoming connection from "
					+ s.getRemoteSocketAddress());
			s.getOutputStream().write(message.getBytes());
			s.close();
		}
	}

	/**
	 * 使用一个Selector管理多个ServerSocketChannel
	 * 
	 * @throws Exception
	 */
	public static void serverThree() throws Exception {
		Selector selector = Selector.open();
		ServerSocketChannel ssc1 = ServerSocketChannel.open();
		ssc1.socket().setReuseAddress(true);
		ssc1.socket().bind(new InetSocketAddress(8989));
		ssc1.configureBlocking(false);
		ssc1.register(selector, SelectionKey.OP_ACCEPT);

		ServerSocketChannel ssc2 = ServerSocketChannel.open();
		ssc2.socket().setReuseAddress(true);
		ssc2.socket().bind(new InetSocketAddress(8990));
		ssc2.configureBlocking(false);
		ssc2.register(selector, SelectionKey.OP_ACCEPT);

		ServerSocketChannel ssc3 = ServerSocketChannel.open();
		ssc3.socket().setReuseAddress(true);
		ssc3.socket().bind(new InetSocketAddress(8991));
		ssc3.configureBlocking(false);
		ssc3.register(selector, SelectionKey.OP_ACCEPT); // ServerSocketChannel只需要注册连接事件OP_ACCEPT

		while (true) {
			System.out.println("[Server#3] wait for connection ……");
			int num = selector.select();
			if (num == 0) { // some where may called wakeUp
				continue;
			}
			
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();

			while (it.hasNext()) {
				SelectionKey key = (SelectionKey) it.next();
				work(key);
				it.remove();
			}
		}
	}

	private static void work(SelectionKey key) throws Exception {

		// 可以将SocketChannel注册到另一个Selector，然后处理其他的事件
		if (key.isValid() && key.isAcceptable()) {
			System.out.println("isAcceptable");
			int port = ((ServerSocketChannel) key.channel()).socket().getLocalPort();
			SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
			String msg = message + " --- "
					+ sc.socket().getRemoteSocketAddress() + " --- " + port;
			sc.write(ByteBuffer.wrap(msg.getBytes()));
		}
		
		if(key.isAcceptable()) {
	        // a connection was accepted by a ServerSocketChannel.

	    } else if (key.isConnectable()) {
	        // a connection was established with a remote server.

	    } else if (key.isReadable()) {
	        // a channel is ready for reading

	    } else if (key.isWritable()) {
	        // a channel is ready for writing
	    }
	}
}
