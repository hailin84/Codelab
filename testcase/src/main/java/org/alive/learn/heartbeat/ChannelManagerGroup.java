package org.alive.learn.heartbeat;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * <p>
 * ChannelManagerGroup，里面包含多个ChannelManager类，此类提供一些便利方法进行管理.
 * 作用类似于Netty的NioEventLoopGroup.
 * </p>
 * 
 * @author hailin84
 * @since 2017.06.15
 *
 */
public class ChannelManagerGroup {
	public static final Logger logger = LoggerFactory.getLogger(ChannelManagerGroup.class);

	/** 默认SubReactor线程数,一般根据CPU个数来定，也可以通过参数设置 */
	private static final int DEFAULT_NIO_THREAD_COUNT;

	static {
		int cpus = Runtime.getRuntime().availableProcessors();
		DEFAULT_NIO_THREAD_COUNT = cpus > 1 ? 2 * (cpus - 1) : 2;
	}

	/** 具体的ChannelManager */
	private ChannelManager[] managers;

	/** 业务线程池，共用  */
	private ExecutorService bizExecutorService;

	/** Selector(SubReactor)线程数，也即ChannelManager个数 */
	private int nioThreadCount = DEFAULT_NIO_THREAD_COUNT;

	/** 默认情况下，业务线程池大小 */
	private int bizThreadCount = 10;

	/** 下标指示器，用于将SocketChannel平均分配到每一个ChannelManager，即分配给不同的selector */
	private int indexer = 0;

	/** 是否客户端 */
	private boolean clientSide = false;

	public ChannelManagerGroup(int nioThreadCount, int bizThreadCount, int port) {
		super();
		this.nioThreadCount = nioThreadCount;
		this.bizThreadCount = bizThreadCount;
		bizExecutorService = Executors.newFixedThreadPool(bizThreadCount);
		this.managers = new ChannelManager[nioThreadCount];
		for (int i = 0; i < nioThreadCount; i++) {
			managers[i] = new ChannelManager(bizExecutorService, port, this);
		}
	}

	public ChannelManagerGroup() {
		bizExecutorService = Executors.newFixedThreadPool(bizThreadCount);
	}

	public void dispatch(SocketChannel socketChannel) throws IOException {
		if (socketChannel != null) {
			// 设置SocketChannel参数
			socketChannel.configureBlocking(false);
			socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
			socketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			// SocketChannel首次注册到Selector，注册读和写事件，在运行过程中不需要修改所注册的事件
			next().register(new MyNioTask(socketChannel, SelectionKey.OP_READ | SelectionKey.OP_WRITE));
		}
	}

	/**
	 * 向SocketChannel发送消息data
	 * 
	 * @param socketChannel
	 * @param data
	 * @throws IOException
	 */
	public void sendMessage(SocketChannel socketChannel, ByteBuffer data) throws IOException {
		if (socketChannel != null) {
			next().register(new MyNioTask(socketChannel, SelectionKey.OP_READ | SelectionKey.OP_WRITE, data));
		}
	}

	private ChannelManager next() {
		// indexer从0到99循环
		if (indexer + 1 == 100) {
			indexer = 0;
		}
		return managers[indexer++ % nioThreadCount];
	}

	public boolean isClientSide() {
		return clientSide;
	}

	/**
	 * 如果是客户端程序，需要设置此方法
	 * 
	 * @param clientSide
	 */
	public void setClientSide(boolean clientSide) {
		this.clientSide = clientSide;
	}

	public void close() {
		logger.info("close - ChannelManagerGroup");
		if (managers != null) {
			for (ChannelManager m : managers) {
				if (m != null) {
					m.close();
				}
			}
		}
		bizExecutorService.shutdown();
	}
}
