package org.alive.learn.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1";
		int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }

        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        try {
        	Bootstrap b = new Bootstrap();
        	b.group(workerGroup);
        	b.channel(NioSocketChannel.class);
        	b.option(ChannelOption.SO_KEEPALIVE, true);
        	b.handler(new ChannelInitializer<SocketChannel>() {
        		@Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeDecoder(),new TimeClientHandler());
                }
        	} );
        	
        	// Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
	}
}
