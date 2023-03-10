package org.alive.learn.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

public class AioDemoClient {

	public static void main(String[] args) throws Exception {
		AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
		Future<Void> future = client.connect(new InetSocketAddress("127.0.0.1", 5000));
		future.get();
		ByteBuffer buffer = ByteBuffer.allocate(100);
		client.read(buffer, null, new CompletionHandler<Integer, Void>() {
			@Override
			public void completed(Integer result, Void attachment) {
				System.out.println(Thread.currentThread().getName());
				System.out.println("client received: " + new String(buffer.array(), 0, buffer.limit()));
			}

			@Override
			public void failed(Throwable exc, Void attachment) {
				exc.printStackTrace();
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		Thread.sleep(10000);
	}

}
