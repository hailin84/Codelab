package org.alive.learn.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * JDK AIO server
 * 
 * @author hailin84
 * 
 * @since 2017.05.22
 *
 */
public class AioDemoServer {

	static boolean running = true;

	static int port = 5000;

	private static Charset charset = Charset.forName("US-ASCII");
	private static CharsetEncoder encoder = charset.newEncoder();

	public static void main(String[] args) throws Throwable {
		AsynchronousChannelGroup gp = AsynchronousChannelGroup.withCachedThreadPool(Executors.newCachedThreadPool(),
				10);
		final AsynchronousServerSocketChannel ssc = AsynchronousServerSocketChannel.open(gp)
				.bind(new InetSocketAddress(port));

		// server.
		ssc.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {

			public void completed(AsynchronousSocketChannel asc, Void att) {
				// accept the next connection
				ssc.accept(null, this);

				// handle this connection
				handle(asc);
			}

			public void failed(Throwable exc, Void att) {
				// ssc.accept(null, this);
				System.out.println("failed: " + exc);
			}
		});

		System.out.println("System is running, press q to quit.");
		while (running) {
			int b = System.in.read();
			switch (b) {
			case 'q':
				running = false;
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

	public static void handle(AsynchronousSocketChannel asc) {
		try {
			// ByteBuffer buffer = encoder.encode(CharBuffer.wrap(now + "\r\n"));
			System.out.println(Thread.currentThread().getName());
			String now = new Date().toString();
			ByteBuffer buffer = encoder.encode(CharBuffer.wrap(now + "\r\n"));
			// result.write(buffer, null, new
			// CompletionHandler<Integer,Void>(){...}); //callback or
			Future<Integer> f = asc.write(buffer);
			f.get();
			System.out.println("sent to client: " + now);
			asc.close();
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}

class DemoAcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {

	@Override
	public void completed(AsynchronousSocketChannel result, Object attachment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failed(Throwable exc, Object attachment) {
		// TODO Auto-generated method stub
		
	}
	
}
