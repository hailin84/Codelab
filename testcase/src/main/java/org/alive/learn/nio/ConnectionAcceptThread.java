package org.alive.learn.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 接收TCP连接处理线程。
 * 
 * @author hailin84
 * 
 */
public class ConnectionAcceptThread extends Thread {

	/** ServerSocketChannel注册连接事件的选择器  */
	private Selector selector = null;
	
	/** 用来监测SocketChannel的Selector */
	private Selector socketChannelSelector = null;

	private boolean running = true;

	private int timeOut = 3000;

	public ConnectionAcceptThread(Selector selector) {
		super();
		this.selector = selector;
		try {
			this.socketChannelSelector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (running) {
			try {
				int num = selector.select(timeOut);
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

				if (key.isAcceptable()) {
					
					try {
						SocketChannel sc = ((ServerSocketChannel) key.channel())
								.accept();
						sc.configureBlocking(false);
						sc.register(socketChannelSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
						
						// TODO: Add SocketChannel to some queue,
						
						// Wakeup SocketChannel Selector
						socketChannelSelector.wakeup();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// remove
				keyIter.remove();
			}

		}
	}

}
