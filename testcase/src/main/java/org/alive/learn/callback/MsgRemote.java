package org.alive.learn.callback;

/**
 * 小李
 * 
 * @author hailin84
 *
 */
public class MsgRemote {
	/**
	 * 处理消息
	 * 
	 * @param msg
	 *            接收的消息
	 * @param callBack
	 *            回调函数处理类
	 */
	public void executeMessage(String msg, MsgCallBack callBack) {
		/** 模拟远程类正在处理其他事情，可能需要花费许多时间 **/
		for (int i = 0; i < 1000000000; i++) {

		}
		/** 处理完其他事情，现在来处理消息 **/
		System.out.println("小王的问题是：" + msg);
		System.out.println("总算搞出来了，1+1等于0，厉害吧？哈哈！");
		/** 执行回调 **/
		callBack.execute("Nice to meet you~!"); // 这相当于同学执行完之后打电话给你
	}
}
