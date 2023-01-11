package org.alive.learn.heartbeat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import org.alive.tools.socketmessage.UIUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client starter
 * 
 * @author hailin84
 * @since 2017.06.16
 */
public final class ClientStarter {

	/** 保存本类的实例集合 */
	private static final List<ClientStarter> instances = new ArrayList<>();

	public static final Logger logger = LoggerFactory.getLogger(ClientStarter.class);

	private SocketChannel channel;

	private String ip;

	private int port;

	public UIUpdater updater;

	ChannelManagerGroup group = null;

	private boolean connected = false;

	public ClientStarter(UIUpdater updater) {
		super();
		this.updater = updater;
		group = new ChannelManagerGroup(1, 2, port);
		// 设置为client模式
		group.setClientSide(true);

		instances.add(this);
	}

	public ClientStarter(String ip, int port, UIUpdater updater) {
		super();
		this.ip = ip;
		this.port = port;
		this.updater = updater;
		group = new ChannelManagerGroup(1, 2, port);
		group.setClientSide(true);
		instances.add(this);
	}

	public void connect() {
		try {
			channel = SocketChannel.open(new InetSocketAddress(ip, port));
			channel.configureBlocking(false);
			group.dispatch(channel);
			connected = true;
			updater.update("connectOrDisconnect", new Object[] { "断开" });
			logger.info("连接成功!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void disConnect() {
		// channel = null;
		// group.close();
		// connected = false;
		if (channel != null && channel.isConnected()) {
			try {
				channel.close();
				channel = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			updater.update("connectOrDisconnect", new Object[] { "连接" });
			logger.info("连接断开!");
		}
	}

	public void sendMessage(String message) {
		if (!connected) {
			return;
		}
		BaseMessage msg = new BaseMessage(message);
		try {
			group.sendMessage(channel, ByteBuffer.wrap(msg.composeFull()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updater.update("statusMsg", new Object[] { "已发送..." });
	}

	public UIUpdater getUpdater() {
		return updater;
	}

	public boolean isConnected() {
		return connected;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public static void stopAll() {
		for (ClientStarter s : instances) {
			// if (s.connected) {
			// s.disConnect();
			// }
			s.group.close();
		}
	}

	public static ClientStarter getFirst() {
		return instances.get(0);
	}
}
