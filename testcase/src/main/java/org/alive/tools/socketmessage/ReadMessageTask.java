package org.alive.tools.socketmessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 读取消息线程，因为使用的非阻塞式IO，所以需要在单独的线程中读取；同时也为了方便更新UI
 * 
 * @author hailin84
 * 
 */
public class ReadMessageTask implements Runnable {

	private Selector selector;

	private UIUpdater updater;

	private String encoding;

	private boolean running = true;

	private int timeout = 0;

	public ReadMessageTask(Selector selector, UIUpdater updater, String encoding) {
		super();
		this.selector = selector;
		this.updater = updater;
		this.encoding = encoding;
	}

	public void run() {
		int timeoutCount = 60;
		while (running && timeout < timeoutCount) { // 超时时间设置为60S
			try {
				int num = selector.select(1000);
				if (num == 0) {
					timeout++;
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
					SocketChannel sc = null;
					try {
						sc = (SocketChannel) key.channel();
						int bufferSize = 1024 * 10; // 10KB read buffer
						ByteBuffer buf = ByteBuffer.allocate(bufferSize);
						List<ByteBuffer> buffers = new ArrayList<ByteBuffer>();

						int bytesRead = 0;
						int totalBytesRead = 0;

						bytesRead = sc.read(buf);
						while (bytesRead > 0) {
							buffers.add(buf);
							buf = ByteBuffer.allocate(bufferSize);
							totalBytesRead += bytesRead;
							bytesRead = sc.read(buf);
						}

						final byte[] data = new byte[totalBytesRead];
						int startIndex = 0;
						for (ByteBuffer bf : buffers) {
							bf.flip();
							System.arraycopy(bf.array(), 0, data, startIndex,
									bf.remaining());
							startIndex += bf.remaining();
						}

						String receivedString = new String(data, encoding);
						// 更新UI
						updater.update("responseMessage",
								new Object[] { receivedString });
						updater.update("statusMsg", new Object[] { "已收到响应.." });

						/**
						 * int iReadBytes = sc.read(buffer); if (iReadBytes !=
						 * -1) { buffer.flip();
						 * 
						 * // 将字节转化为为UTF-8的字符串 final String receivedString =
						 * Charset .forName("UTF-8").newDecoder()
						 * .decode(buffer).toString();
						 * 
						 * // 更新UI SwingUtilities.invokeLater(new Runnable() {
						 * public void run() {
						 * responseMessage.setText(receivedString);
						 * statusMsg.setText("已收到响应.."); } }); }
						 */
					} catch (IOException e) {
						e.printStackTrace();
						updater.update("statusMsg", new Object[] { "异常" + e.getMessage() });
					} finally {
						if (sc != null) {
							try {
								sc.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						running = false;
					}
				}

				// remove
				keyIter.remove();
			}
		}
		
		// 关闭Selector
		if (this.selector != null) {
			try {
				this.selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (timeout == timeoutCount) {
			updater.update("statusMsg", new Object[] { "请求超时.." });
		}
	}
}
