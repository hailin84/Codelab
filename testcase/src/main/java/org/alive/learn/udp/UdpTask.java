package org.alive.learn.udp;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * <p>
 * UDP Task
 * 
 * @author hailin84
 * @date 2017.10.10
 */
public class UdpTask {
	private ByteBuffer buffer = null;
	
	private SocketAddress source = null;

	public UdpTask(ByteBuffer buffer, SocketAddress source) {
		this.buffer = buffer;
		this.source = source;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public SocketAddress getSource() {
		return source;
	}

	public void setSource(SocketAddress source) {
		this.source = source;
	}
}
