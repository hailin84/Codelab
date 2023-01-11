package org.alive.learn.heartbeat.biz;

import java.nio.ByteBuffer;

import org.alive.learn.heartbeat.BaseMessage;
import org.alive.learn.heartbeat.MyNioTask;
import org.alive.learn.heartbeat.SubReactorThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 实际执行业务逻辑的地方，相当于Doug Lea老爷子文档里的Handler.
 * </p>
 * 
 * @author hailin84
 * @since 2017.06.13
 */
public class BizHandler implements Runnable {

	protected static final Logger logger = LoggerFactory.getLogger(BizHandler.class);

	protected SubReactorThread subReactor;

	private BaseMessage message;

	public BizHandler(BaseMessage message, SubReactorThread subReactor) {
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
			String rspMsg = new String(message.getMessageBody(), BaseMessage.ENCODING) + " ---> 服务响应";
			this.message.setMessageBody(rspMsg.getBytes(BaseMessage.ENCODING));
			// message.getExtendChannel().setLastSendDataTime(System.currentTimeMillis());
			this.subReactor.register(new MyNioTask(message.getChannel(),
					message.getExtendChannel().getKey().interestOps(), ByteBuffer.wrap(message.composeFull())));
		} catch (Exception e) {
			logger.error("业务处理失败", e);
		}
		//

	}
}
