package org.alive.learn.netty;

/**
 * <p>
 * 服务端业务逻辑处理
 * 
 * @author hailin84
 * @date 2017.09.26
 *
 */
public class ServerBusinessProcessor extends AppBusinessProcessor {
	
	public void process(NettyMessage message) {
		logger.info("服务端执行业务处理...");
		String req = message.bodyToString();
		// TODO: biz goes here
		String rsp = req + " ---> 服务响应";
		message.setMessageBody(rsp);
	}
}
