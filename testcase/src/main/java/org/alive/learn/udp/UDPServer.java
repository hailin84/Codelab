package org.alive.learn.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 简单的UDP Server
 * 
 * @author hailin84
 * @since 2017.07.07
 */
public class UDPServer {
	private static final int DEFAULT_PORT = 40043;

	private static final int MAX_PACKET_SIZE = 65507;

	private static boolean running = true;

	public static void main(String[] args) {
		int port = DEFAULT_PORT;
		byte[] buffer = new byte[MAX_PACKET_SIZE];
		DatagramSocket server = null;
		try {
			server = new DatagramSocket(port);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

			final DatagramSocket tmpServer = server;

			new Thread(new Runnable() {

				@Override
				public void run() {
					while (running) {
						try {
							tmpServer.receive(packet);
							String s = new String(packet.getData(), packet.getOffset(), packet.getLength(), "UTF-8");
							System.out.println(packet.getAddress() + " at port:" + packet.getPort() + " says: " + s);
							// 设置以后需要接受的长度
							packet.setLength(buffer.length);
						} catch (SocketException e1) {
							// DatagramSocket关闭后，receive方法会抛出此异常
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}
			}, "AcceptorThread").start();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		try {
			System.in.read();
			running = false;
			if (server != null) {
				server.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
