package org.alive.learn.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class TCPClientReadThread extends Thread {

	private Selector selector;

	public TCPClientReadThread(Selector selector) {
		this.selector = selector;
	}

	public void run() {

		while (true) {
			try {
				int num = selector.select(3000);
				if (num == 0) {
					continue;
				}
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			// 取得迭代器.selectedKeys()中包含了每个准备好某一I/O操作的信道的SelectionKey
			Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();

			while (keyIter.hasNext()) {
				SelectionKey key = keyIter.next();
				if (key.isReadable()) {
					try {
						SocketChannel sc = (SocketChannel) key.channel();
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						int iReadBytes = sc.read(buffer);
						if (iReadBytes != -1) {
							buffer.flip();

							// 将字节转化为为UTF-16的字符串
							String receivedString = Charset.forName("UTF-8")
									.newDecoder().decode(buffer).toString();

							// 控制台打印出来
							System.out.println("接收到来自服务器"
									+ sc.socket().getRemoteSocketAddress()
									+ "的信息:" + receivedString);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				// remove
				keyIter.remove();
			}
		}
	}
}
