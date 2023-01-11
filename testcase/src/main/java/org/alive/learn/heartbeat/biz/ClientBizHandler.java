package org.alive.learn.heartbeat.biz;

import org.alive.learn.heartbeat.BaseMessage;
import org.alive.learn.heartbeat.ClientStarter;
import org.alive.learn.heartbeat.SubReactorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 供Client端使用的biz handler
 * 
 * @author hailin84
 * @since 2017.09.13
 */
public class ClientBizHandler implements Runnable {
	protected static final Logger logger = LoggerFactory.getLogger(BizHandler.class);

	protected SubReactorThread subReactor;

	private BaseMessage message;

	public ClientBizHandler(BaseMessage message, SubReactorThread subReactor) {
		super();
		this.message = message;
		this.subReactor = subReactor;
	}

	@Override
	public void run() {
		if (message == null || message.getMessageBody() == null) {
			return;
		}
		// FIXME: do your business
		try {
			logger.info("执行业务处理...");
			// 客户端更新UI界面上的responseMessage文本框
			ClientStarter.getFirst().updater.update("responseMessage",
					new Object[] { message.bodyToString() });
			ClientStarter.getFirst().updater.update("statusMsg", new Object[] { "已收到响应..." });
		} catch (Exception e) {
			logger.error("业务处理失败", e);
		}
		//
	}
}
