package org.alive.learn.heartbeat;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 心跳线程，采用ping-ping模式，间隔20S，即服务器跟客户端双方都每隔20S发送一次心跳，收到心跳后无需响应。
 * <p>
 * 默认心跳包为12字节消息头，可以通过构造函数提供自定义心跳。
 * 
 * @author hailin84
 *
 */
public class HeartBeatThread extends Thread {

	public static final Logger logger = LoggerFactory.getLogger(HeartBeatThread.class);

	private volatile boolean running = true;

	/** 心跳线程负责发送心跳的SocketChannel集合，来自所属的ChannelManager */
	private Map<SocketChannel, ExtendSocketChannel> channelMap = null;

	private BaseMessage heartBeatMsg = BaseMessage.HEATBEAT_MSG;

	private ChannelManager parent;

	public HeartBeatThread(Map<SocketChannel, ExtendSocketChannel> channelMap, ChannelManager parent,
			BaseMessage heartBeatMsg) {
		super();
		this.channelMap = channelMap;
		this.parent = parent;
		// 可以通过构造函数提供自定义的心跳包
		if (heartBeatMsg != null) {
			this.heartBeatMsg = heartBeatMsg;
		}
	}

	@Override
	public void run() {

		if (channelMap == null) {
			return;
		}

		while (running) {
			for (Iterator<Map.Entry<SocketChannel, ExtendSocketChannel>> iterator = channelMap.entrySet()
					.iterator(); iterator.hasNext();) {
				Map.Entry<SocketChannel, ExtendSocketChannel> entry = iterator.next();
				SocketChannel sc = entry.getKey();
				ExtendSocketChannel es = entry.getValue();
				if (processOne(es, sc)) {
					iterator.remove(); // 移出已被关闭掉的SocketChannel
				}
			}

			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// ignore
				logger.error("心跳线程异常", e);
			}
		}

		logger.info("结束线程 {}", Thread.currentThread().getName());
	}

	private boolean processOne(ExtendSocketChannel es, SocketChannel sc) {
		MyNode node = es.getNode();
		// 如果相应的Socket因其他原因关闭，如对方主动断开连接，则返回true
		if (!sc.isConnected() || !es.getKey().isValid()) {
			logger.info("{} 连接已关闭", node);
			// readTask.isStop = true;
			return true;
		}

		long now = System.currentTimeMillis();
		long duringRecvTime = (now - es.getLastRecvDataTime()) / 1000;
		long duringSendTime = (now - es.getLastSendDataTime()) / 1000;

		if (duringRecvTime >= es.getIdleTimeOut()) {
			logger.info("{} timeout, 关闭SocketChannel", node);
			parent.closeChannel(sc);
			/**
			 * <pre>
			 * try {
			 * 	sc.close();
			 * 	es.getKey().cancel();
			 * } catch (IOException e) {
			 * 	e.printStackTrace();
			 * }
			 * </pre>
			 */
			return true;
		}

		if (duringSendTime >= es.getIdleCheckInterval()) {
			// 打印日志的时候，心跳实际上还没发出去，所以有时候会看到已经关闭了连接后继续打印日志
			logger.info("{} 发送心跳{}", node, es.getSendHeartBeatCount() + 1);
			// es.setLastSendDataTime(now);
			es.increaseHeartBeatCount(1);
			// 心跳线程不直接写数据到SocketChannel，而是通过提交带心跳ByteBuffer数据的NioTask到该SocketChannel对应的SubReactor线程；
			// 这里即使提交了Task，也有可能不会发送
			SelectionKey key = es.getKey();
			if (key.isValid()) {
				parent.register(new MyNioTask(sc, key.interestOps(), ByteBuffer.wrap(heartBeatMsg.getMessageHead())));
			}

		}
		return false;

	}

	public void close() {
		logger.info("close - 心跳线程");
		this.running = false;
	}
}
