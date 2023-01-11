package org.alive.learn.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * 简易HTTP Server实现，《Java并发编程艺术》例子
 * 
 * @author hailin84
 * @date 2017.08.30
 */
public class SimpleHttpServer {

	private ThreadPool<HttpRequestHandler> pool = new DefaultThreadPool<>();

	private String basePath;

	private ServerSocket serverSocket;

	private int port = 8080;

	private volatile boolean running = true;
	
	/** 请求计数器 */
	private AtomicLong reqCount = new AtomicLong();

	public SimpleHttpServer(String basePath, int port) {
		this.basePath = basePath;
		this.port = port;
	}

	public void start() throws Exception {
		serverSocket = new ServerSocket(port);
		Socket s = null;
		try {
			while ((s = serverSocket.accept()) != null && running) {
				pool.execute(new HttpRequestHandler(s, basePath));
				long count = reqCount.incrementAndGet();
				if (count == Long.MAX_VALUE) {
					reqCount.set(0L);
				}
				System.out.printf("%d\taccept new connection : %s\n", count, s.getRemoteSocketAddress());
			}
		} catch (SocketException e) {
			System.out.println("ServerSocket已经关闭");
		}
	}

	public void stop() {
		running = false;
		pool.shutdown();
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isRunning() {
		return running;
	}

	public String getBasePath() {
		return basePath;
	}

	public int getPort() {
		return port;
	}

	public static void main(String[] args) throws Exception {
		// basePath设置为classpath根目录下面的simpleweb目录
		String basePath = SimpleHttpServer.class.getResource("/").toURI().getPath() + "simpleweb";
		int port = 8080;
		final SimpleHttpServer server = new SimpleHttpServer(basePath, port);

		// 新的线程启动SimpleHttpServer，以便通过main线程继续接收输入
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					server.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "Simple-http-server").start();

		// 主线程输入q，结束SimpleHttpServer线程
		waitToQuit(server);
	}

	private static void waitToQuit(SimpleHttpServer server) throws IOException {
		System.out.printf("SimpleHttpServer is running on port %s, press q to quit.", server.getPort());
		while (server.isRunning()) {
			int b = System.in.read();
			switch (b) {
			case 'q':
				System.out.println("SimpleHttpServer关闭...");
				server.stop();
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
