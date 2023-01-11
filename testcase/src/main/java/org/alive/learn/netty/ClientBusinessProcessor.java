package org.alive.learn.netty;

/**
 * <p>
 * 客户服务端业务逻辑处理
 * 
 * @author hailin84
 * @date 2017.09.27
 *
 */
public class ClientBusinessProcessor extends AppBusinessProcessor {

	public void process(NettyMessage message) {
		NettyClientHelper helper = NettyClientHelper.getFirst();
		if (helper == null) {
			return;
		}
		logger.info("客户端执行业务处理...");
		
		// TODO: biz goes here
		
		// 更新界面
		helper.getUpdater().update("responseMessage", new Object[] { message.bodyToString() });
		helper.getUpdater().update("statusMsg", new Object[] { "已收到响应..." });
	}

}
