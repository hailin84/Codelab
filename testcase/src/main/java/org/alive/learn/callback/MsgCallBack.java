package org.alive.learn.callback;

/**
 * 
 * @author hailin84
 * 
 */
public interface MsgCallBack {
	/**
	 * 回调方法
	 * 
	 * @param objects
	 *            将处理后的结果作为参数返回给回调方法
	 */
	public void execute(Object... objects);
}
