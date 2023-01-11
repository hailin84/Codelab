package org.alive.learn.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 简单的UDP Client
 * 
 * @author hailin84
 * @since 2017.07.07
 */
public class UDPClient {
	private static final int DEFAULT_PORT = 40043;

	public static void main(String[] args) {
		String hostname = "localhost";
		int port = DEFAULT_PORT;
		try {
			InetAddress server = InetAddress.getByName(hostname);
			BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
			// 创建UDP客户端
			DatagramSocket client = new DatagramSocket();
			while (true) {
				String inline = userIn.readLine();
				// 输入包含dot，退出
				if (inline.indexOf('.') != -1)
					break;
				byte[] data = inline.getBytes("UTF-8");

				// 数据报
				DatagramPacket thepacket = new DatagramPacket(data, data.length, server, port);
				client.send(thepacket);
			}
			client.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
