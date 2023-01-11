package org.alive.learn.heartbeat;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * 扩展SocketChannel，增加一些属性，用于发送心跳、流量统计等
 * 
 * @author hailin84
 * @since 2017.06.08
 *
 */
public class ExtendSocketChannel {
	private static final int DEFAULT_BUFFER_SIZE = 1024;
	
	/** 底层真正的SocketChannel */
	private SocketChannel channel;

	/** 是否是长链接 */
	private boolean longConnection = true;

	/** 最后发送数据时间 */
	private long lastSendDataTime = System.currentTimeMillis();

	/** 最后接收数据时间 */
	private long lastRecvDataTime = System.currentTimeMillis();

	/** 已发送心跳次数，从上一次收到消息开始计 */
	private int sendHeartBeatCount = 0;

	/** 读数据的时候用到的锁 */
	// public final byte[] readLock = new byte[0];

	/** 写数据的时候用到的锁 */
	// public final byte[] writeLock = new byte[0];

	/** 检查连接时间间隔,即发送心跳时间间隔 */
	private int idleCheckInterval = 20;

	/** 连接超时断开时间 */
	private int idleTimeOut = 60;

	/** SocketChannel注册到Selector的SelectionKey */
	private SelectionKey key;

	private MyNode node = null;
	
	/** 缓存数据Buffer，用以多次读取到足够的数据后做分包处理 */
	private CircleByteBuffer readBuffer = null;
	
	public ExtendSocketChannel() {
		this.readBuffer = new CircleByteBuffer(DEFAULT_BUFFER_SIZE);
	}

	public ExtendSocketChannel(SocketChannel sc, boolean longConnection) {
		super();
		this.channel = sc;
		this.longConnection = longConnection;
		this.readBuffer = new CircleByteBuffer(DEFAULT_BUFFER_SIZE);
	}
	
	public ExtendSocketChannel(SocketChannel sc, boolean longConnection, int bufferSize) {
		super();
		this.channel = sc;
		this.longConnection = longConnection;
		this.readBuffer = new CircleByteBuffer(bufferSize);
	}

	public boolean isLongConnection() {
		return longConnection;
	}

	public void setLongConnection(boolean longConnection) {
		this.longConnection = longConnection;
	}

	public long getLastSendDataTime() {
		return lastSendDataTime;
	}

	public void setLastSendDataTime(long lastSendDataTime) {
		this.lastSendDataTime = lastSendDataTime;
	}

	public long getLastRecvDataTime() {
		return lastRecvDataTime;
	}

	public void setLastRecvDataTime(long lastRecvDataTime) {
		this.lastRecvDataTime = lastRecvDataTime;
	}

	public int getIdleCheckInterval() {
		return idleCheckInterval;
	}

	public void setIdleCheckInterval(int idleCheckInterval) {
		this.idleCheckInterval = idleCheckInterval;
	}

	public int getIdleTimeOut() {
		return idleTimeOut;
	}

	public void setIdleTimeOut(int idleTimeOut) {
		this.idleTimeOut = idleTimeOut;
	}

	public void resetHeartBeatCount() {
		this.sendHeartBeatCount = 0;
	}

	public void increaseHeartBeatCount(int count) {
		this.sendHeartBeatCount += count;
	}

	public int getSendHeartBeatCount() {
		return sendHeartBeatCount;
	}

	public SocketChannel getChannel() {
		return channel;
	}

	public void setChannel(SocketChannel channel) {
		this.channel = channel;
	}

	public SelectionKey getKey() {
		return key;
	}

	public void setKey(SelectionKey key) {
		this.key = key;
	}

	public MyNode getNode() {
		return node;
	}

	public void setNode(MyNode node) {
		this.node = node;
	}
	
	public CircleByteBuffer getReadBuffer() {
		return readBuffer;
	}

	public void setReadBuffer(CircleByteBuffer readBuffer) {
		this.readBuffer = readBuffer;
	}
}
