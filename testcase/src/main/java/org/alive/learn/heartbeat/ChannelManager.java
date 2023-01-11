package org.alive.learn.heartbeat;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * ChannelManager类，通过此类管理一组SocketChannel(即连接)、SocketChannel注册的Selector(SubReactor线程)、负责给这此SocketChannel发送心跳的线程
 * </p>
 * 
 * @author hailin84
 *
 */
public class ChannelManager {

	private static Logger logger = LoggerFactory.getLogger(ChannelManager.class);

	/** 执行任务的线程池，如果每个ChannelManager使用单独的线程池，则可以在这里定义 */
	// private ExecutorService service;

	/** 计数器，表示有多少个此类的实例 */
	private static AtomicInteger instanceCounter = new AtomicInteger(0);

	/** 本类管理的所有连接的集合 */
	private Map<SocketChannel, ExtendSocketChannel> channelMap = new ConcurrentHashMap<>();

	/** SubReactor线程 */
	private SubReactorThread subReactor;

	/** 心跳线程 */
	private HeartBeatThread heartBeat;

	private ChannelManagerGroup parentGroup;

	public ChannelManager(ExecutorService service, int port, ChannelManagerGroup parentGroup) {
		instanceCounter.incrementAndGet();
		// this.service = service;
		subReactor = new SubReactorThread(service, this);
		subReactor.setName("SubReactor-" + port + "-" + instanceCounter.get());
		subReactor.start();
		heartBeat = new HeartBeatThread(channelMap, this, null);
		heartBeat.setName("HeartBeat-" + port + "-" + instanceCounter.get());
		heartBeat.start();

		this.parentGroup = parentGroup;
	}

	/**
	 * 添加任务
	 * 
	 * @param task
	 */
	public void register(MyNioTask task) {
		this.subReactor.register(task);
	}

	public Map<SocketChannel, ExtendSocketChannel> getChannelMap() {
		return channelMap;
	}

	public ExtendSocketChannel getExtendChannel(SocketChannel sc) {
		return sc != null ? this.channelMap.get(sc) : null;
	}

	/**
	 * 将SelectionKey设置到SocketChannel对应的ExtendSocketChannel对象中，如果此时channelMap中并不存在对应的数据，则创建一个
	 * 
	 * @param sc
	 * @param key
	 */
	public void setKeyToExtendChannel(SocketChannel sc, SelectionKey key) {
		if (sc == null) {
			return;
		}
		ExtendSocketChannel es = this.channelMap.get(sc);
		if (es != null) {
			es.setKey(key);
		} else {
			es = new ExtendSocketChannel(sc, true);
			es.setNode(MyNode.buildFrom(sc)); // Node信息保存
			es.setKey(key);
			this.channelMap.put(sc, es);
		}
	}

	/**
	 * <p>
	 * 关闭channel，并将对应的SelectionKey
	 * cancel掉，供心跳线程和SubReactor线程调用，从channelMap移除对应的key/value不需要在这里进行，心跳线程会处理
	 * 
	 * @param sc
	 */
	public void closeChannel(SocketChannel sc) {
		ExtendSocketChannel es = this.channelMap.get(sc);
		es.getKey().cancel();
		try {
			sc.close();
		} catch (IOException e) {
			logger.error("{} 关闭channel出错 {}", es.getNode(), e);
		}
	}

	public boolean isClientSide() {
		return parentGroup.isClientSide();
	}

	public void close() {
		instanceCounter.decrementAndGet();
		this.heartBeat.close();
		this.subReactor.close();

		if (channelMap.size() > 0) {
			for (Iterator<Entry<SocketChannel, ExtendSocketChannel>> iterator = channelMap.entrySet()
					.iterator(); iterator.hasNext();) {
				Map.Entry<SocketChannel, ExtendSocketChannel> entry = iterator.next();
				SocketChannel sc = entry.getKey();
				ExtendSocketChannel es = entry.getValue();
				try {
					logger.info("关闭连接 {}", es.getNode());
					sc.close();
					es.getKey().cancel();
				} catch (IOException e) {
					logger.error("{} 关闭channel出错 {}", es.getNode(), e);
				}
				iterator.remove();
			}
		}
	}
}
