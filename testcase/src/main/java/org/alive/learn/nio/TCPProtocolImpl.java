package org.alive.learn.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TCPProtocolImpl implements TCPProtocol {

	private int bufferSize;

	public TCPProtocolImpl(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public void handleAccept(SelectionKey key) throws IOException {
		SocketChannel clientChannel = ((ServerSocketChannel) key.channel())
				.accept();
		clientChannel.configureBlocking(false);
		clientChannel.register(key.selector(), SelectionKey.OP_READ);
	}

	public void handleRead(SelectionKey key) throws IOException {
		// 获得与客户端通信的信道
		SocketChannel clientChannel = (SocketChannel) key.channel();
		// Charset cs = Charset.forName("UTF-8");

		List<ByteBuffer> buffers = new ArrayList<ByteBuffer>();
		ByteBuffer buffer = null;
		int bytesRead = 0;
		int bytesTotalRead = 0;
		// 循环读取，可以读取变长报文
		while (true) {
			buffer = ByteBuffer.allocate(bufferSize);
			bytesRead = clientChannel.read(buffer);

			if (bytesRead > 0) {
				buffers.add(buffer);
				bytesTotalRead += bytesRead;
			} else if (bytesRead == 0) {
				// 0表示读取完了但SocketChannel没有关闭
				break;
			} else { // -1表示读取完了而且客户端SocketChannel关闭
				clientChannel.close();
				break;
			}
		}
		byte[] data = new byte[bytesTotalRead];
		int startIndex = 0;
		for (ByteBuffer bf : buffers) {
			bf.flip();
			System.arraycopy(bf.array(), 0, data, startIndex, bf.remaining());
			startIndex += bf.remaining();
		}

		String message = new String(data, "UTF-8");

		// 控制台打印出来
		System.out.println("接收到来自"
				+ clientChannel.socket().getRemoteSocketAddress() + "的信息:"
				+ message);

		// 准备发送的文本
		String sendString = "你好,客户端. @" + new Date().toString() + "，已经收到你的信息"
				+ message;
		buffer = ByteBuffer.wrap(sendString.getBytes("UTF-8"));
		clientChannel.write(buffer);

	}

	public void handleWrite(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub

	}
}