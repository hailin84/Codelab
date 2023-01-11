package org.alive.tools.socketmessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 工作线程：初始化Socket连接，启动ReadMessageTask线程准备读取响应消息，发送请求消息，必要的时候更新UI界面
 * 
 * @author hailin84
 * 
 */
public class WorkerTask implements Runnable {

	private String ip;

	private int port;

	private String request;

	private String encoding;

	private int headLen;

	private UIUpdater updater;

	public WorkerTask(String ip, int port, String request, String encoding,
			int headLen, UIUpdater updater) {
		super();
		this.ip = ip;
		this.port = port;
		this.request = request;
		this.encoding = encoding;
		this.headLen = headLen;
		this.updater = updater;
	}

	public void run() {

		SocketChannel sc = null;
		try {
			sc = SocketChannel.open(new InetSocketAddress(ip, port));
			sc.configureBlocking(false);
			sc.socket().setTcpNoDelay(true);
			sc.socket().setReuseAddress(true);
			Selector selector = Selector.open();
			sc.register(selector, SelectionKey.OP_READ);
			// 先启动读取响应线程
			new Thread(new ReadMessageTask(selector, updater, encoding), "ReadMessageThread").start();

			updater.update("statusMsg", new Object[] { "已连接.." });
			// 发送消息，注意是在先启动了ReadMessageTask线程之后，以防止ReadMessageTask线程还没启动，响应消息就发送回来
			byte[] data = request.getBytes(encoding);
			// 将消息长度放到消息头部，类似于00001342<xml ...>
			String msgHead = String.valueOf(data.length);
			for (int i = msgHead.length(); i < headLen; i++) {
				msgHead = "0" + msgHead;
			}
			updater.update("lengthMsg", new Object[] { "消息长度" + msgHead });

			ByteBuffer writeBuffer = ByteBuffer.allocate(headLen + data.length);
			if (headLen > 0) {
				byte[] head = msgHead.getBytes(encoding);
				writeBuffer.put(head);
			}
			writeBuffer.put(data);
			writeBuffer.flip(); // flip，准备发送数据
			sc.write(writeBuffer);

			updater.update("statusMsg", new Object[] { "已发送请求.." });
			// SwingUtilities.invokeLater(new Runnable() {
			// public void run() {
			// statusMsg.setText("已发送请求..");
			// }
			// });
		} catch (IOException e) {
			e.printStackTrace();
			updater.update("statusMsg", new Object[] { "异常" + e.getMessage() });
		}
	}
}
