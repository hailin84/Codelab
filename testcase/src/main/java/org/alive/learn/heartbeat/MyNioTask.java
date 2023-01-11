package org.alive.learn.heartbeat;

import java.nio.channels.SocketChannel;

/**
 * NioTask类，用于描述运行过程中的IO事件，如：Write事件
 * 
 * @author hailin84
 * @since 2017.06.15
 */
public class MyNioTask {
	private SocketChannel channel;
	private int op;
	private Object data;

	public MyNioTask(SocketChannel sc, int op) {
		this.channel = sc;
		this.op = op;
	}

	public MyNioTask(SocketChannel sc, int op, Object data) {
		this(sc, op);
		this.data = data;
	}

	public SocketChannel getChannel() {
		return channel;
	}

	public void setChannel(SocketChannel channel) {
		this.channel = channel;
	}

	public int getOp() {
		return op;
	}

	public void setOp(int op) {
		this.op = op;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
