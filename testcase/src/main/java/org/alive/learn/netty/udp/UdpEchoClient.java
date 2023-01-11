package org.alive.learn.netty.udp;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * <p>
 * 基于Netty实现的UDP Echo Client，定时向服务端发送数据，并接收服务端响应
 * 
 * @author hailin84
 * @date 2017.10.10
 */
public class UdpEchoClient {

	private static String serverIp = "127.0.0.1";
	private static int serverPort = 9999;

	private static Charset charset = Charset.defaultCharset();

	public static void main(String[] args) {
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.channel(NioDatagramChannel.class);
			bootstrap.group(workerGroup);
			bootstrap.handler(new ChannelInitializer<NioDatagramChannel>() {

				@Override
				protected void initChannel(NioDatagramChannel ch) throws Exception {
					ch.pipeline().addLast(new SimpleChannelInboundHandler<DatagramPacket>() {

						@Override
						protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
							// System.out.println(msg.sender() + " --> " +
							// msg.recipient());
							System.out.println("[client] receive：" + msg.content().copy().toString(charset));
						}

					});
				}
			});
			// 监听端口，客户端绑定到0，使用随机端口，也可以显示指定端口
			ChannelFuture sync = bootstrap.bind("127.0.0.1", 0).sync();
			Channel udpChannel = sync.channel();

			Thread t = new Thread(new Sender(udpChannel), "SenderThread");

			t.start();
			t.join();

			System.out.println("UDP Client退出");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

	static class Sender implements Runnable {

		Channel channel = null;

		public Sender(Channel channel) {
			this.channel = channel;
		}

		@Override
		public void run() {
			int count = 5;
			for (int i = 0; i < count; i++) {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String data = "你好，UDP" + i;
				System.out.println("[client] send：" + data);
				channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(data.getBytes(charset)),
						new InetSocketAddress(serverIp, serverPort)));
			}
		}
	}
}
