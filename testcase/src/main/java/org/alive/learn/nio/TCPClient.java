package org.alive.learn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * NIO TCP client. 
 * 
 * @author hailin84
 * 
 */
public class TCPClient {
	
	private Selector selector;

	// 与服务器通信的信道
	SocketChannel socketChannel;

	// 要连接的服务器Ip地址
	private String serverIp;

	// 要连接的远程服务器在监听的端口
	private int serverPort;

	/**
	 * 构造函数
	 * 
	 * @param ip
	 * @param port
	 * @throws IOException
	 */
	public TCPClient(String ip, int port) throws IOException {
		this.serverIp = ip;
		this.serverPort = port;

		// 打开监听信道并设置为非阻塞模式
		socketChannel = SocketChannel.open(new InetSocketAddress(serverIp,
				serverPort));
		socketChannel.configureBlocking(false);

		selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_READ);

		// 启动读取线程
		new TCPClientReadThread(selector).start();
	}

	/**
	 * 发送字符串到服务器
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMsg(String message) throws IOException {
		ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("UTF-8"));
		socketChannel.write(writeBuffer);
	}

	public static void main(String[] args) throws IOException {
		TCPClient client = new TCPClient("127.0.0.1", 1978);

		client.sendMsg("公元2014年11月27日：Say hello from nio. Length is more than buffer..");
	}
}
