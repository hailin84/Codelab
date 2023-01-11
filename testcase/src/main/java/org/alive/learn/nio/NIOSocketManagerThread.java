package org.alive.learn.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * NIO Socket管理线程，负责管理Socket连接(SocketChannel)
 * 
 * @author hailin84
 * 
 */
public class NIOSocketManagerThread extends Thread {

	private Selector selector = null;

	private boolean running = true;

	/** 本线程管理的连接集合 */
	private List<SocketChannel> scList = Collections
			.synchronizedList(new ArrayList<SocketChannel>(200));

	private long lastSendHeartBeatTime = System.currentTimeMillis();

	private long heartBeatRate = 10; // 10S

	public NIOSocketManagerThread() {
		try {
			selector = Selector.open();
		} catch (IOException e) {
			// TODO: 处理异常
			e.printStackTrace();
		}
	}

	public void addSocketChannel(SocketChannel sc) {
		try {
			sc.configureBlocking(false);
			sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			synchronized (scList) {
				scList.add(sc);
			}
			selector.wakeup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {

			while (running) {
				int num = selector.select(2000);
				if (num == 0) { // someone may called wakeUp somewhere
					continue;
				}

				Iterator<SelectionKey> it = selector.selectedKeys().iterator();

				while (it.hasNext()) {
					SelectionKey key = (SelectionKey) it.next();
					try {
						doWork(key);
					} catch (IOException e) {
						e.printStackTrace();
						// TODO: 处理单个连接的异常
					}
					it.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void close() {
		try {
			this.running = false;
			
			Iterator<SocketChannel> iter = scList.iterator();
			while (iter.hasNext()) {
				iter.next().close();
			}
			
			if (this.selector != null) {
				this.selector.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: close exception
		}
	}

	private void doWork(SelectionKey key) throws IOException {
		if (key.isValid() && key.isWritable()) {
			SocketChannel sc = (SocketChannel) key.channel();

			long now = System.currentTimeMillis();
			if (now - lastSendHeartBeatTime > heartBeatRate * 1000) {
				System.out.println("发送心跳包...");
				String heartBeatMsg = "HeartBeat: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\r\n";
				ByteBuffer buffer = ByteBuffer.wrap(heartBeatMsg
						.getBytes("UTF-8"));
				sc.write(buffer);
				
				lastSendHeartBeatTime = now;
			}
		}
	}
}
