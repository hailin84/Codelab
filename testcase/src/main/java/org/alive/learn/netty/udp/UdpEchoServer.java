package org.alive.learn.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * <p>
 * 基于Netty实现的UDP Echo Server
 * 
 * @author hailin84
 * @date 2017.10.10
 */
public class UdpEchoServer {

	public static final int port = 9999;

	public static void main(String[] args) {

		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioDatagramChannel.class);
			b.handler(new ChannelInitializer<NioDatagramChannel>() {
				@Override
				public void initChannel(NioDatagramChannel ch) throws Exception {
					ch.pipeline().addLast(new UdpEchoServerHandler());
				}
			});

			// Start the client.
			ChannelFuture f = b.bind(port).sync(); // (5)

			// Wait until the connection is closed.
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

}
