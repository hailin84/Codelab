package org.alive.learn.callback;

/**
 * Send message to MsgRemote, and wait for being called by MsgRemote(that's
 * callback).
 * 
 * 小王问小李问题案例中的小王
 * @author hailin84
 * 
 */
public class MsgLocal implements MsgCallBack {
	
	/** 对小李的引用  */
	private MsgRemote remote;

	/**
	 * 发送出去的消息
	 */
	private String message;

	public MsgLocal(MsgRemote remote, String message) {
		super();
		this.remote = remote;
		this.message = message;
	}

	/**
	 * 发送消息，通过这个方法向MsgRemote问问题
	 */
	public void sendMessage() {
		/** 当前线程的名称 **/
		System.out.println(Thread.currentThread().getName());
		
		/** 创建一个新的线程发送消息，新线程里等待对方的回调  **/
		System.out.println("问小李问题：");
		new Thread(new Runnable() {
			public void run() {
				/**
				 * 小王调用小李中的方法，在这里注册回调接口
				 * 这就相当于A类调用B的方法C
				 * 这相当于给同学打电话，打完电话之后，这个线程就可以去做其他事情了，
				 * 只不过等到你的同学打回电话给你的时候你要做出响应
				 */
				remote.executeMessage(message, MsgLocal.this); 
			}
		}).start();
		/** 当前线程继续执行，可以继续做想做的事情  **/
		System.out.println("Message has been sent by Local~!");
		// continue to do anything you want
		hangout();
	}

	public void execute(Object... objects) {
		/** 打印返回的消息 **/
		System.out.println(objects[0]);
		/** 打印发送消息的线程名称 **/
		System.out.println(Thread.currentThread().getName());
		/** 中断发送消息的线程 **/
		Thread.interrupted();
	}
	
	public void hangout(){
		System.out.println("我要逛街去了");
	}
}
