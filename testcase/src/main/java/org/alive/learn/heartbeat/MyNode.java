package org.alive.learn.heartbeat;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * Node节点，保存一个连接双方的IP和端口，用于日志追踪
 * 
 * @author hailin84
 * @since 2017.06.20
 *
 */
public class MyNode {

	private String local;

	private String remote;

	public MyNode(String local, String remote) {
		super();
		this.local = local;
		this.remote = remote;
	}

	public static MyNode buildFrom(SocketChannel sc) {
		MyNode n = null;
		try {
			n = new MyNode(sc.getLocalAddress().toString(), sc.getRemoteAddress().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return n;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

	@Override
	public String toString() {
		// return "Local(" + local + ") <--> Remote(" + remote + ")";
		return remote;
	}
}
