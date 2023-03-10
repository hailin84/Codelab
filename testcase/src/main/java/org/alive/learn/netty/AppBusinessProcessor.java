package org.alive.learn.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 业务逻辑处理抽象基类,服务端跟客户端分别实现自己的处理逻辑
 * 
 * @author hailin84
 * @date 2017.09.26
 *
 */
public abstract class AppBusinessProcessor {
	protected static Logger logger = LoggerFactory.getLogger(AppBusinessProcessor.class);

	/**
	 * 执行业务处理，参数是包含的是请求消息，处理完毕后，将响应消息设置到message对象中
	 * 
	 * @param message
	 *            请求/响应消息载体
	 */
	public abstract void process(NettyMessage message);
}
