package org.alive.learn.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * <p>
 * 负责业务处理的Handler，运行在业务线程组<code>DefaultEventExecutorGroup</code>中，以免任务太耗时而导致NIO线程阻塞；
 * 
 * @author hailin84
 * @date 2017.09.26
 *
 */
public class NettyBusinessDuplexHandler extends ChannelDuplexHandler {
	private static Logger logger = LoggerFactory.getLogger(NettyBusinessDuplexHandler.class);

	private AppBusinessProcessor bizProcessor = null;

	public NettyBusinessDuplexHandler(AppBusinessProcessor appBizHandler) {
		super();
		this.bizProcessor = appBizHandler;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage bizMsg = (NettyMessage) msg; // 拆分好的消息

		if (bizMsg.getMessageType() == NettyMessage.MESSAGE_TYPE_HB) {
			logger.info("收到心跳  -- {}", bizMsg.toString());
		} else {
			// 处理业务消息
			logger.info("收到消息  -- {}", bizMsg.toString());
			bizProcessor.process(bizMsg);
			// 如果接收到的是请求，则需要写回响应消息
			if (bizMsg.getFlag() == 0) {
				bizMsg.setFlag((byte) 1);
				logger.info("写回消息  -- {}", bizMsg.toString());
				ByteBuf rspMsg = Unpooled.copiedBuffer(bizMsg.composeFull());
				ctx.writeAndFlush(rspMsg);
			}
		}
		// 继续传递给Pipeline下一个Handler
		// super.channelRead(ctx, msg);
		// ctx.fireChannelRead(msg);
	}
}
