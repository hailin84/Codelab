package org.alive.learn.netty.udp;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class UdpEchoServerHandler extends SimpleChannelInboundHandler<DatagramPacket>{

	private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	// private Charset charset = Charset.forName("UTF-8"); // CharsetUtil.UTF_8
	
	private Charset charset = Charset.defaultCharset();
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		ByteBuf buf = msg.copy().content();
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		
		String reqStr = new String(req, charset);
		System.out.println("[server] receive: " + reqStr);
		
		String rspStr = "当前时间为" + LocalDateTime.now().format(fmt);
		
		System.out.println("[server] send: " + rspStr);
		ByteBuf timeBuf = Unpooled.copiedBuffer(rspStr, charset);
		ctx.writeAndFlush(new DatagramPacket(timeBuf, msg.sender())).sync();
	}

}
