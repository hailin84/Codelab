package org.alive.learn.netty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.alive.tools.socketmessage.UIUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * <p>
 * 客户端工具类，用于解耦UI和NettyClient，本例保存相应的对象实例，协助参数传递，并提供连接、断开、发送消息等入口方法。
 * 
 * @author hailin84
 * @date 2017.09.27
 */
public final class NettyClientHelper {
	private static Logger logger = LoggerFactory.getLogger(NettyClientHelper.class);
	
	private static final List<NettyClientHelper> instances = new ArrayList<>();

	private volatile NettyClient client;

	private volatile UIUpdater updater;

	public NettyClientHelper() {
		instances.add(this);
	}

	public NettyClient getClient() {
		return client;
	}

	public void setClient(NettyClient client) {
		this.client = client;
	}

	public UIUpdater getUpdater() {
		return updater;
	}

	public void setUpdater(UIUpdater updater) {
		this.updater = updater;
	}

	public static NettyClientHelper getFirst() {
		return instances.size() > 0 ? instances.get(0) : null;
	}

	public void setRemoteHost(String ip, int port) {
		client.setIp(ip);
		client.setPort(port);
	}

	public void connect() {
		client.connect();
		// connectOrDisconnect为界面组件在UIUpdater中注册的名字
		updater.update("connectOrDisconnect", new Object[] { "断开" });
	}

	public void disConnect() {
		client.disConnect();
		updater.update("connectOrDisconnect", new Object[] { "连接" });
	}

	public void stopAll() {
		for (NettyClientHelper helper : instances) {
			if (helper.getClient() != null) {
				helper.getClient().close();
			}
		}
	}

	public void sendMessage(String message) {
		NettyMessage bizMsg = new NettyMessage(message);
		bizMsg.setLogId(newLogId());
		logger.info("发送消息  -- {}", bizMsg.toString());
		ByteBuf buf = Unpooled.copiedBuffer(bizMsg.composeFull());
		client.future.channel().writeAndFlush(buf);
		// ReferenceCountUtil.release(buf);
	}

	/**
	 * 随机生成logId
	 * 
	 * @return logId 介于[1000000,10000000]的随机奇数
	 */
	private int newLogId() {
		int logId = randomInt(1000000, 10000000);
		if (logId % 2 == 0) {
			logId -= 1;
		}

		return logId;
	}

	/**
	 * 生成介于min，max之间的随机整数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randomInt(int min, int max) {
		Random random = new Random(System.currentTimeMillis());
		// int x = (int) (Math.random() * max + min);
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}
}
